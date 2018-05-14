
package com.studentAssist.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.studentAssist.entities.AccommodationAdd;
import com.studentAssist.entities.Apartments;
import com.studentAssist.entities.NotificationSettings;
import com.studentAssist.entities.Universities;
import com.studentAssist.entities.UserAccommodationNotifications;
import com.studentAssist.entities.UserVisitedAdds;
import com.studentAssist.entities.Users;
import com.studentAssist.response.AccommodationSearchDTO;
import com.studentAssist.util.SAConstants;
import com.studentAssist.util.Utilities;

import javassist.bytecode.stackmap.TypeData.ClassName;

@Repository
@Transactional
public class AccommodationDAO extends AbstractDao {

    @Autowired
    NotificationsDAO notificationsDAO;

    /**
     * @param user
     * @param advertisement
     * @param apartmentName
     * @return
     * @throws Exception
     */
    public String createAccommodationAddFromFacebook(Users user, AccommodationAdd advertisement, int apartmentId)
            throws Exception {

        saveOrUpdate(user);
        Apartments apartment = getByKey(Apartments.class, apartmentId);
        this.addAccommodationAddToApartment(apartment, advertisement);
        this.addAccommodationToUser(user, advertisement);
        addUniversityToAdd(apartment.getUniversity(), advertisement);

        persist(advertisement);
        // notificationsDAO.sendNotification(apartment, advertisement, user);
        return SAConstants.RESPONSE_SUCCESS;

    }

    public String createAccommodationAdd(long userId, AccommodationAdd advertisement, int apartmentId)
            throws Exception {

        Users user = getByKey(Users.class, userId);
        Apartments apartment = getByKey(Apartments.class, apartmentId);

        advertisement.setDatePosted(Utilities.getDate());

        addAccommodationAddToApartment(apartment, advertisement);
        addAccommodationToUser(user, advertisement);
        addUniversityToAdd(apartment.getUniversity(), advertisement);

        persist(advertisement);

        // sendNotification(apartment, advertisement, user);
        return SAConstants.RESPONSE_SUCCESS;
    }

    public void deleteAccommodationAdd(AccommodationAdd add) throws Exception {
        add.setDelete_sw(true);
        saveOrUpdate(add);
    }

    public List<AccommodationAdd> getUserPosts(long userId, int position) throws Exception {
        Query query = getSession().createQuery("from AccommodationAdd where user.userId=" + userId
                + " and delete_sw= 0 and (postedTill > sysdate()" + " or postedTill is null)");

        // for pagination
        query.setFirstResult(position);
        query.setMaxResults(SAConstants.PAGE_SIZE);

        List userAdds = query.list();
        lazyLoadAdds(userAdds);

        return userAdds;
    }

    public List<Apartments> getAllApartmentNames(Users currentUser) throws Exception {

        Users dbUser = getByKey(Users.class, currentUser.getUserId());

        List<Integer> universityIds = new ArrayList<Integer>();
        for (Universities university : dbUser.getUniversities()) {

            universityIds.add(university.getUniversityId());
        }

        if (universityIds.isEmpty()) {
            return new ArrayList<>();
        }

        return getCriteria(Apartments.class).add(Restrictions.in("university.universityId", universityIds)).list();

    }

    public List<AccommodationAdd> getAdvancedAdvertisements(String apartmentName, String gender, int universityId,
                                                            int position) throws Exception {
        List<AccommodationAdd> adds = null;

        Criteria criteria = getCriteria(AccommodationAdd.class, "add").setFetchMode("apartment", FetchMode.JOIN)
                .createAlias("apartment", "a").add((Criterion) Restrictions.eq("university.universityId", universityId))
                .add((Criterion) Restrictions.eq((String) "a.apartmentName", apartmentName))
                .add(Restrictions.eq("add.gender", gender));

        criteria.add(Restrictions.eq("add.delete_sw", false));
        criteria.add(Restrictions.or(Restrictions.gt("add.postedTill", Utilities.getDate()),
                Restrictions.isNull("add.postedTill")));

        criteria.addOrder(Order.desc("add.datePosted"));

        // for pagination
        criteria.setFirstResult(position);
        criteria.setMaxResults(SAConstants.PAGE_SIZE);

        adds = criteria.list();
        lazyLoadAdds(adds);

        return adds;
    }

    /**
     * 1. here apartment is name of join column in AccommodationAdd JOINS
     * Apartments table
     *
     * @param leftSpinner
     * @param rightSpinner
     * @param session
     * @return
     */
    public List<AccommodationAdd> getSimpleSearchAdds(String leftSpinner, String rightSpinner,
                                                      List<Integer> universityIds) throws Exception {

        String secondParameter = "";
        List list1 = new ArrayList<>();

        List<AccommodationAdd> accommodationAddsList = new ArrayList<>();

        if (SAConstants.APARTMENT_TYPE.equals(leftSpinner)) {
            secondParameter = "a.apartmentType";
        } else if (SAConstants.APARTMENT_NAME.equals(leftSpinner)) {
            secondParameter = "a.apartmentName";
        }

        for (int universityId : universityIds) {
            Criteria criteria = getCriteria(AccommodationAdd.class, "add");

            if (leftSpinner.equals("gender")) {
                criteria.setFetchMode("apartment", FetchMode.SELECT).createAlias("apartment", "a")
                        .add(Restrictions.eq("a.university.universityId", universityId))
                        .add(Restrictions.eq("add.gender", rightSpinner));
            } else {
                criteria.setFetchMode("apartment", FetchMode.SELECT).createAlias("apartment", "a")
                        .add(Restrictions.eq(secondParameter, rightSpinner)).createAlias("a.university", "b")
                        .add(Restrictions.eq("b.universityId", universityId));

            }
            criteria.add(Restrictions.eq("add.delete_sw", false));
            criteria.add(Restrictions.or(Restrictions.gt("add.postedTill", Utilities.getDate()),
                    Restrictions.isNull("add.postedTill")));

            criteria.addOrder(Order.desc("add.datePosted"));
            criteria.setFirstResult(0);
            criteria.setMaxResults(SAConstants.PAGE_SIZE);
            list1.addAll(criteria.list());

            if (!list1.isEmpty() && SAConstants.APARTMENT_NAME.equals(leftSpinner)) {
                break;
            }

        }
        accommodationAddsList.addAll(list1);
        lazyLoadAdds(accommodationAddsList);
        return accommodationAddsList;
    }

    public List<Apartments> getApartmentNames(String apartmentType) throws Exception {
        List apartments = null;

        Apartments exampleApartments = new Apartments();
        exampleApartments.setApartmentType(apartmentType);
        Example example = Example.create((Object) exampleApartments);
        Criteria criteria = getCriteria(Apartments.class).add((Criterion) example);
        apartments = criteria.list();

        return apartments;
    }

    public List<AccommodationAdd> getAccommodationNotifications(Users user, int position) {

        List<UserAccommodationNotifications> userAccommodationNotifications;

        UserAccommodationNotifications userNotifications = new UserAccommodationNotifications();
        userNotifications.setUser(user);

        Example example = Example.create((Object) userNotifications);
        Criteria criteria = getCriteria(UserAccommodationNotifications.class, "userAdds").add((Criterion) example)
                .createAlias("userAdds.accommodationAdd", "add")
                .add(Restrictions.or(Restrictions.gt("add.postedTill", Utilities.getDate()),
                        Restrictions.isNull("add.postedTill")))
                .add(Restrictions.eq("add.delete_sw", false));
        criteria.setFetchMode("AccommodationAdd", FetchMode.JOIN);

        criteria.setFirstResult(position);
        criteria.setMaxResults(SAConstants.PAGE_SIZE);
        userAccommodationNotifications = criteria.list();

        List<AccommodationAdd> adds = new ArrayList();
        for (UserAccommodationNotifications notification : userAccommodationNotifications) {
            adds.add(notification.getAccommodationAdd());
        }
        lazyLoadAdds(adds);
        return adds;
    }

    public int addNewApartment(Apartments apartment, int universityId) throws Exception {
        Universities univ = new Universities();
        univ.setUniversityId(universityId);
        apartment.setUniversity(univ);
        apartment.setAddedDate(Utilities.getDate());
        return save(apartment);
    }

    public List<Apartments> getApartmentNamesWithType(List<Integer> universityIds) throws Exception {

        List apartmentNames = null;
        String univsList = StringUtils.join(universityIds.toArray(), ",");

        Query query = getSession().createQuery("from Apartments where university.universityId in(" + univsList + ")");
        apartmentNames = query.list();

        return apartmentNames;

    }

    public List<AccommodationAdd> recentListChecker(Integer[] accommodationAdds) throws Exception {
        List adds = new ArrayList<AccommodationAdd>();

        Criteria criteria = getCriteria(AccommodationAdd.class)
                .add(Restrictions.in((String) "addId", (Object[]) accommodationAdds));
        adds = criteria.list();

        return adds;
    }

    public String setUserVisitedAdds(UserVisitedAdds userVisitedAdds) throws Exception {

        saveOrUpdate(userVisitedAdds);

        return SAConstants.RESPONSE_SUCCESS;

    }

    /**
     * not a Rest endpoint method. Only used for the tick mark on
     * searchAccommodation page
     *
     * @param session
     * @param user
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public List<Long> getUserVisitedAdds(Users user) throws Exception {

        Criteria crit = getCriteria(UserVisitedAdds.class, "userVisited").createAlias("userVisited.user", "user")
                .createAlias("userVisited.accommodationAdd", "accommodationAdd")
                .setProjection(Projections.property("userVisited.accommodationAdd.addId"))
                .add(Restrictions.eq("user.userId", user.getUserId()))
                .add(Restrictions.eq("accommodationAdd.delete_sw", false))
                .add(Restrictions.or(Restrictions.gt("accommodationAdd.postedTill", Utilities.getDate()),
                        Restrictions.isNull("accommodationAdd.postedTill")))
                .setFetchMode("AccommodationAdd", FetchMode.JOIN);

        return crit.list();

    }

    public List<AccommodationAdd> getRecentlyViewed(Users user, int position) {

        List<Object[]> list;

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("FROM UserVisitedAdds u join u.accommodationAdd a");
        stringBuilder.append(" where a.addId= u.accommodationAdd.addId and u.user.userId = '");
        stringBuilder.append(user.getUserId());
        stringBuilder.append("' and a.delete_sw= 0 and (postedTill > sysdate()"
                + " or postedTill is null) order by u.createDate desc");
        Query query = getSession().createQuery(stringBuilder.toString());

        // for pagination
        query.setFirstResult(position);
        query.setMaxResults(SAConstants.PAGE_SIZE);

        list = query.list();
        List<AccommodationAdd> userVisited_AccommodationAdds = new ArrayList<AccommodationAdd>();

        for (Object[] obj : list) {
            userVisited_AccommodationAdds.add((AccommodationAdd) obj[1]);

        }
        lazyLoadAdds(userVisited_AccommodationAdds);

        return userVisited_AccommodationAdds;

    }

    private void addAccommodationToUser(Users user, AccommodationAdd add) {
        user.getAccommodationAdd().add(add);
        add.setUser(user);
    }

    private void addAccommodationAddToApartment(Apartments apartment, AccommodationAdd add) {
        add.setApartment(apartment);
    }

    private void addUniversityToAdd(Universities university, AccommodationAdd add) {
        add.setUniversity(university);
    }

    private void lazyLoadAdds(List<AccommodationAdd> adds) {

        for (AccommodationAdd add : adds) {
            Hibernate.initialize(add.getAddPhotoIds());
            Hibernate.initialize(add.getApartment().getUniversity().getUniversityPhotos());
        }

    }

    @SuppressWarnings("unchecked")
    public List<Apartments> getApartmentNamesWithTypeAndUniv(Users currentUser) {

        Users dbUser = getByKey(Users.class, currentUser.getUserId());

        List<Integer> universityIds = new ArrayList();
        for (Universities university : dbUser.getUniversities()) {

            universityIds.add(university.getUniversityId());
        }

        if (universityIds.isEmpty()) {
            return new ArrayList<>();
        }

        return getCriteria(Apartments.class).add(Restrictions.in("university.universityId", universityIds))
                .setFetchMode("Universities", FetchMode.JOIN).addOrder(Order.desc("university.universityId")).list();

    }

    @SuppressWarnings("unchecked")
    public List<AccommodationAdd> getSimpleSearchAddsPagination(String leftSpinner, String rightSpinner, int position,
                                                                int universityId) {

        String secondParameter = "";
        List simpleSearchAddsList = new ArrayList<>();

        List<AccommodationAdd> accommodationAddsList = new ArrayList<>();

        if (SAConstants.APARTMENT_TYPE.equals(leftSpinner)) {
            secondParameter = "a.apartmentType";
        } else if (SAConstants.APARTMENT_NAME.equals(leftSpinner)) {
            secondParameter = "a.apartmentName";
        }

        Criteria criteria = getCriteria(AccommodationAdd.class, "add");
        if (leftSpinner.equals("gender")) {
            criteria.setFetchMode("apartment", FetchMode.SELECT).createAlias("apartment", "a")
                    .add(Restrictions.eq("a.university.universityId", universityId))
                    .add(Restrictions.eq("add.gender", rightSpinner));

        } else {
            criteria.setFetchMode("apartment", FetchMode.SELECT).createAlias("apartment", "a")
                    .add(Restrictions.eq(secondParameter, rightSpinner)).createAlias("a.university", "b")
                    .add(Restrictions.eq("b.universityId", universityId));

        }

        criteria.add(Restrictions.eq("add.delete_sw", false));
        criteria.add(Restrictions.or(Restrictions.gt("add.postedTill", Utilities.getDate()),
                Restrictions.isNull("add.postedTill")));

        criteria.addOrder(Order.desc("add.datePosted"));
        criteria.setFirstResult(position);
        criteria.setMaxResults(SAConstants.PAGE_SIZE);
        simpleSearchAddsList.addAll(criteria.list());

        accommodationAddsList.addAll(simpleSearchAddsList);
        lazyLoadAdds(accommodationAddsList);

        return accommodationAddsList;

    }

    public void getSimpleSearchAddsNg(AccommodationSearchDTO accommodationSearch) {

        StringBuilder simpleSearchSql = new StringBuilder();

        simpleSearchSql.append("  SELECT");
        simpleSearchSql.append("    add2.ADD_ID addId,");
        simpleSearchSql.append("    univ.universityName univName,");
        simpleSearchSql.append("    apts.apartmentName apartmentName,");
        simpleSearchSql.append("    univ.universityId,");
        simpleSearchSql.append("    CASE");
        simpleSearchSql.append("      WHEN EXISTS (SELECT");
        simpleSearchSql.append("          NULL");
        simpleSearchSql.append("        FROM UserVisitedAdds uva");
        simpleSearchSql.append("        WHERE uva.USER_ID = 1118294135");
        simpleSearchSql.append("        AND uva.ADD_ID = add2.ADD_ID) THEN 'Y'");
        simpleSearchSql.append("      ELSE 'N'");
        simpleSearchSql.append("    END AS userVisited");
        simpleSearchSql.append("  FROM AccommodationAdd AS add1");
        simpleSearchSql.append("  INNER JOIN AccommodationAdd AS add2");
        simpleSearchSql.append("    ON add1.UNIVERSITY_ID = add2.UNIVERSITY_ID");
        simpleSearchSql.append("    AND add1.datePosted <= add2.datePosted");
        simpleSearchSql.append("    AND add2.UNIVERSITY_ID = 3");
        simpleSearchSql.append("  INNER JOIN Apartments apts");
        simpleSearchSql.append("    ON apts.id = add2.APARTMENT_ID");
        simpleSearchSql.append("    AND apts.apartmentType = 'on'");
        simpleSearchSql.append("  LEFT JOIN Universities univ");
        simpleSearchSql.append("    ON univ.universityId = apts.universityId");
        simpleSearchSql.append("    AND univ.universityId = add2.UNIVERSITY_ID");
        simpleSearchSql.append("  GROUP BY add1.ADD_ID");
        simpleSearchSql.append("  HAVING COUNT(*) <= 10");
        simpleSearchSql.append("  ORDER BY add1.apartment_Id, add1.datePosted DESC");

        SQLQuery query = getSession().createSQLQuery(simpleSearchSql.toString());
        query.addScalar("addId", StandardBasicTypes.LONG);
        // query.addScalar("univName", StandardBasicTypes.STRING);
        query.addEntity("AccommodationAdd", AccommodationAdd.class);

        @SuppressWarnings("unchecked")
        List<Object[]> obj = query.list();

        for (Object[] accommodationAdd : obj) {
            System.out.println(accommodationAdd[0]);
        }
    }

    public AccommodationAdd getRecentAccommodationAdd() {

        DetachedCriteria innerCriteria = DetachedCriteria.forClass(AccommodationAdd.class, "add")
                .setProjection(Projections.projectionList().add(Projections.max("add.datePosted")));

        innerCriteria.add(Restrictions.eq("add.delete_sw", false));
        innerCriteria.add(Restrictions.or(Restrictions.gt("add.postedTill", Utilities.getDate()),
                Restrictions.isNull("add.postedTill")));

        Criteria crit = getSession().createCriteria(AccommodationAdd.class, "outer");
        crit.add(Subqueries.propertyEq("outer.datePosted", innerCriteria));
        crit.add(Restrictions.eq("outer.delete_sw", false));
        crit.add(Restrictions.or(Restrictions.gt("outer.postedTill", Utilities.getDate()),
                Restrictions.isNull("outer.postedTill")));

        List<AccommodationAdd> adds = crit.list();

        AccommodationAdd add = adds.get(0);
        return add;

    }

    public List<AccommodationAdd> getAccommodationCardsByUniversityId(int selectedUniversityID, int numberOfCards) {

        AccommodationAdd add = new AccommodationAdd();
        add.setUniversity(new Universities(selectedUniversityID));

        Criteria criteria = getCriteria(AccommodationAdd.class, "add")
                .add(Restrictions.eq("add.university.universityId", selectedUniversityID)).addOrder(Order.asc("cost"))
                .add(Restrictions.eq("add.delete_sw", false))
                .add(Restrictions.or(Restrictions.gt("add.postedTill", Utilities.getDate()),
                        Restrictions.isNull("add.postedTill")))
                .setMaxResults(numberOfCards);

        List adds = criteria.list();
        lazyLoadAdds(adds);

        return adds;
    }

    @Cacheable(value = "accommodationAdd")
    public AccommodationAdd getAccommodationFromId(int addId) {

        AccommodationAdd add = getByKey(AccommodationAdd.class, addId);
        Hibernate.initialize(add.getAddPhotoIds());
        Hibernate.initialize(add.getApartment().getUniversity().getUniversityPhotos());

        return add;

    }

    public String updateAccommodationAdd(AccommodationAdd add) {
        saveOrUpdate(add);
        return SAConstants.RESPONSE_SUCCESS;
    }
}
