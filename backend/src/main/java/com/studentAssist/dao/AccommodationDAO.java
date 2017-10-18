
package com.studentAssist.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.studentAssist.entities.AccommodationAdd;
import com.studentAssist.entities.Apartments;
import com.studentAssist.entities.Universities;
import com.studentAssist.entities.UserAccommodationNotifications;
import com.studentAssist.entities.UserVisitedAdds;
import com.studentAssist.entities.Users;
import com.studentAssist.util.SAConstants;

@Repository
@Transactional
public class AccommodationDAO extends AbstractDao {

	@Autowired
	NotificationsDAO notificationsDAO;

	/**
	 * 
	 * @param user
	 * @param advertisement
	 * @param apartmentName
	 * @return
	 * @throws Exception
	 */
	public String createAccommodationAddFromFacebook(Users user, AccommodationAdd advertisement, String apartmentName)
			throws Exception {

		// session.saveOrUpdate((Object) user);
		saveOrUpdate(user);

		Apartments examplApartment = new Apartments();

		// fetch apartement type using apartmentName and update it in
		// Apartments class
		examplApartment.setApartmentName(apartmentName);
		Example example = Example.create((Object) examplApartment);
		Criteria criteria = getSession().createCriteria((Class) Apartments.class).add((Criterion) example);
		// Criteria criteria = getCriteria(Apartments.class);
		List apartments = criteria.list();

		// add the user and accommodation to each other
		Apartments apartment = (Apartments) apartments.get(0);
		this.addAccommodationAddToApartment(apartment, advertisement);
		this.addAccommodationToUser(user, advertisement);

		// session.save((Object) advertisement);
		persist(advertisement);
		notificationsDAO.sendNotification(apartment, advertisement, user);

		return SAConstants.RESPONSE_SUCCESS;

	}

	public String createAccommodationAdd(String userId, AccommodationAdd advertisement, String apartmentName)
			throws Exception {

		// Users user = (Users) session.get((Class) Users.class, (Serializable)
		// ((Object) userId));
		Users user = getByKey(Users.class, userId);
		Apartments examplApartment = new Apartments();

		examplApartment.setApartmentName(apartmentName);
		Example example = Example.create((Object) examplApartment);
		Criteria criteria = getCriteria(Apartments.class).add((Criterion) example);
		List apartments = criteria.list();

		Apartments apartment = (Apartments) apartments.get(0);
		this.addAccommodationAddToApartment(apartment, advertisement);
		this.addAccommodationToUser(user, advertisement);

		// persist(advertisement);

		// sendNotification(apartment, advertisement, user);
		return SAConstants.RESPONSE_SUCCESS;
	}

	public void deleteAccommodationAdd(AccommodationAdd add) throws Exception {

		delete(add);
		Hibernate.initialize(add.getAddPhotoIds());
	}

	public List<AccommodationAdd> getUserPosts(String userId, int position) throws Exception {
		List userAdds = null;

		// Users user = (Users) session.get((Class) Users.class, (Serializable)
		// ((Object) userId));
		// userAdds = user.getAccommodationAdd();

		Query query = getSession().createQuery("from AccommodationAdd where user.userId=" + userId);

		// for pagination
		query.setFirstResult(position);
		query.setMaxResults(SAConstants.PAGE_SIZE);

		userAdds = query.list();

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

	public List<AccommodationAdd> getAdvancedAdvertisements(String apartmentName, String gender, int position)
			throws Exception {
		List<AccommodationAdd> adds = null;

		AccommodationAdd add = new AccommodationAdd();
		Apartments apartment = new Apartments();
		apartment.setApartmentName(apartmentName);
		this.addAccommodationAddToApartment(apartment, add);
		add.setGender(gender);
		Example example = Example.create((Object) add);
		// getCriteria(AccommodationAdd.class)
		Criteria criteria = getCriteria(AccommodationAdd.class, "add").add((Criterion) example)
				.setFetchMode("apartment", FetchMode.JOIN).createAlias("apartment", "a")
				.add((Criterion) Restrictions.eq((String) "a.apartmentName", (Object) apartmentName));

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
	public List<AccommodationAdd> getSimpleSearchAdds(String leftSpinner, String rightSpinner, Users currentUser)
			throws Exception {

		Users user = getByKey(Users.class, currentUser.getUserId());
		List<Universities> universities = new ArrayList<>();
		Apartments apt = new Apartments();
		String secondParameter = "";
		AccommodationAdd exampleAccAdd = new AccommodationAdd();
		List list1 = new ArrayList<>();

		List<AccommodationAdd> accommodationAddsList = new ArrayList<>();
		// return null if the user does not have universities.
		universities = user.getUniversities();
		if (universities.isEmpty()) {
			return new ArrayList();
		}

		if (SAConstants.APARTMENT_TYPE.equals(leftSpinner)) {
			apt.setApartmentType(rightSpinner);
			secondParameter = "a.apartmentType";
		} else if (SAConstants.APARTMENT_NAME.equals(leftSpinner)) {
			apt.setApartmentName(rightSpinner);
			secondParameter = "a.apartmentName";
		} else if (leftSpinner.equals("gender")) {
			exampleAccAdd.setGender(rightSpinner);
		}

		this.addAccommodationAddToApartment(apt, exampleAccAdd);
		Example example = Example.create((Object) exampleAccAdd);

		// we want the join to Apartments table only for "Apt type" and "Apt
		// Name"

		for (Universities university : user.getUniversities()) {
			Criteria criteria = getCriteria(AccommodationAdd.class, "add").add((Criterion) example);
			if (leftSpinner.equals("gender")) {
				criteria.setFetchMode("apartment", FetchMode.SELECT).createAlias("apartment", "a")
						.add(Restrictions.eq("a.university.universityId", university.getUniversityId()));
			} else {
				criteria.setFetchMode("apartment", FetchMode.SELECT).createAlias("apartment", "a")
						.add(Restrictions.eq(secondParameter, rightSpinner)).createAlias("a.university", "b")
						.add(Restrictions.eq("b.universityId", university.getUniversityId()));

			}

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
		Criteria criteria = getCriteria(UserAccommodationNotifications.class).add((Criterion) example);
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

	public String addNewApartment(String apartmentName, String apartmentType) throws Exception {

		Apartments apartments = new Apartments();

		apartments.setApartmentName(apartmentName);
		apartments.setApartmentType(apartmentType);
		persist(apartments);
		return SAConstants.RESPONSE_SUCCESS;
	}

	public List<Apartments> getApartmentNamesWithType() throws Exception {

		List apartmentNames = null;

		Query query = getSession().createQuery("from Apartments");
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

		List<UserVisitedAdds> userVisitedAdds;

		return getCriteria(UserVisitedAdds.class, "userVisited").createAlias("userVisited.user", "user")
				.setProjection(Projections.property("userVisited.accommodationAdd.addId"))
				.add(Restrictions.eq("userVisited.user.userId", user.getUserId()))
				.setFetchMode("AccommodationAdd", FetchMode.JOIN).list();

	}

	public List<AccommodationAdd> getRecentlyViewed(Users user, int position) {

		List<Object[]> list;

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("FROM UserVisitedAdds u join u.accommodationAdd a");
		stringBuilder.append(" where a.addId= u.accommodationAdd.addId and u.user.userId = '");
		stringBuilder.append(user.getUserId());
		stringBuilder.append("' order by u.createDate desc");
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
		apartment.getAccommodationAdd().add(add);
		add.setApartment(apartment);
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

		Apartments apt = new Apartments();
		String secondParameter = "";
		AccommodationAdd exampleAccAdd = new AccommodationAdd();
		List simpleSearchAddsList = new ArrayList<>();

		List<AccommodationAdd> accommodationAddsList = new ArrayList<>();

		if (SAConstants.APARTMENT_TYPE.equals(leftSpinner)) {
			apt.setApartmentType(rightSpinner);
			secondParameter = "a.apartmentType";
		} else if (SAConstants.APARTMENT_NAME.equals(leftSpinner)) {
			apt.setApartmentName(rightSpinner);
			secondParameter = "a.apartmentName";
		} else if (leftSpinner.equals("gender")) {
			exampleAccAdd.setGender(rightSpinner);
		}

		this.addAccommodationAddToApartment(apt, exampleAccAdd);
		Example example = Example.create((Object) exampleAccAdd);

		// we want the join to Apartments table only for "Apt type" and "Apt
		// Name"
		Criteria criteria = getCriteria(AccommodationAdd.class, "add").add((Criterion) example);
		if (leftSpinner.equals("gender")) {
			criteria.setFetchMode("apartment", FetchMode.SELECT).createAlias("apartment", "a")
					.add(Restrictions.eq("a.university.universityId", universityId));
		} else {
			criteria.setFetchMode("apartment", FetchMode.SELECT).createAlias("apartment", "a")
					.add(Restrictions.eq(secondParameter, rightSpinner)).createAlias("a.university", "b")
					.add(Restrictions.eq("b.universityId", universityId));

		}

		criteria.addOrder(Order.desc("add.datePosted"));
		criteria.setFirstResult(position);
		criteria.setMaxResults(SAConstants.PAGE_SIZE);
		simpleSearchAddsList.addAll(criteria.list());

		accommodationAddsList.addAll(simpleSearchAddsList);
		lazyLoadAdds(accommodationAddsList);

		return accommodationAddsList;

	}

}
