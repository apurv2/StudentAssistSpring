
package com.studentAssist.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.studentAssist.entities.Apartments;
import com.studentAssist.entities.Universities;
import com.studentAssist.entities.Users;
import com.studentAssist.response.RUniversity;

@Repository
@Transactional
public class UniversitiesDAO extends AbstractDao {

    /**
     * 1. here apartment is name of join column in AccommodationAdd JOINS
     * Apartments table
     *
     * @param leftSpinner
     * @param rightSpinner
     * @param session
     * @return
     */
    public List<Object[]> getUniversityNames(Users currentUser) throws Exception {

        StringBuilder sb = new StringBuilder();
        //
        // String queryStr = "select NEW package.RUniversity( a.field1,
        // b.field2,
        // c.field3, c.field4) from a left outer join b on a.id=b.fk left outer
        // join c
        // on b.id=c.fk";
        //
        //
        // TypedQuery<RUniversity> query =
        // getSession().createQuery(queryStr, RUniversity.class);
        //
        // List<RUniversity> results = query.getResultList();

        sb.append("SELECT");
        sb.append("  d.*,");
        sb.append("  (SELECT");
        sb.append("    COUNT(*)");
        sb.append("  FROM AccommodationAdd a,");
        sb.append("       Apartments b,");
        sb.append("       Universities c");
        sb.append("  WHERE a.Apartment_id = b.id");
        sb.append("  AND b.universityId = c.universityId");
        sb.append("  AND c.universityId = d.universityId)");
        sb.append("  AS listings,");
        sb.append("  (SELECT");
        sb.append("	u.photoUrl");
        sb.append("  FROM UniversityPhotos u");
        sb.append("  WHERE u.universityId = d.universityId");
        sb.append("  and u.photoPriority=1)");
        sb.append("  AS url,");
        sb.append("  (SELECT");
        sb.append("    COUNT(*)");
        sb.append("  FROM user_university");
        sb.append("  WHERE universityId = d.universityId)");
        sb.append("  AS user_count");
        sb.append(" FROM Universities d;");

        Query query = getSession().createSQLQuery(sb.toString());
        List<Object[]> list = query.list();
        return list;
    }

    public boolean doesUserHaveUnivs(Users currentUser) {

        Users user = getByKey(Users.class, currentUser.getUserId());

        if (user != null && null != user.getUniversities() && !user.getUniversities().isEmpty()) {
            return true;
        } else {
            return false;
        }

    }

    public Users getUser(Users currentUser) {
        return getByKey(Users.class, currentUser.getUserId());
    }

    public List<Universities> getUserUniversities(Users currentUser) {
        Users user = getByKey(Users.class, currentUser.getUserId());

        List<Universities> dbUnivs;
        dbUnivs = user.getUniversities();
        return dbUnivs != null && !dbUnivs.isEmpty() ? dbUnivs : new ArrayList();

    }

    @SuppressWarnings("unchecked")
    public List<String> getAllUniversityNames() {

        List<String> universities;

        universities = getCriteria(Universities.class).setProjection(Projections.property("universityName")).list();

        return universities;

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

    public List<Object[]> getUniversityNamesWithId(Users user) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select universityId,universityName from Universities a where ");
        stringBuilder.append("a.universityId in (select universityId from user_university where userId = ");
        stringBuilder.append(user.getUserId());
        stringBuilder.append(")");
        String queryString = stringBuilder.toString();

        Query query = getSession().createSQLQuery(queryString.toString());
        List<Object[]> list = query.list();

        return list;

    }

    public List<Universities> getUniversitiesByName(String searchString) {

        Query query = getSession().createQuery("From Universities univ where univ.universityName  like ?");
        query.setString(0, "%" + searchString + "%");

        List<Universities> list = query.list();

        return list;

    }

    public Universities getUniversityDetails(int universityId) {
        return getByKey(Universities.class, universityId);
    }
}
