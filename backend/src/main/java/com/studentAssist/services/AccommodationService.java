
package com.studentAssist.services;

import com.studentAssist.dao.AccommodationDAO;
import com.studentAssist.entities.*;
import com.studentAssist.exception.BadStudentRequestException;
import com.studentAssist.response.*;
import com.studentAssist.util.SAConstants;
import com.studentAssist.util.Utilities;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccommodationService {

    @Autowired
    AccommodationDAO accommmodationDAO;

    @Autowired
    UserService userService;

    @Autowired
    private Mapper mapper;

    public String createAccommodationAddFromFacebook(AccommodationAdd add, Users user, int apartmentId)
            throws Exception {

        user.setRegisteredDate(Utilities.getDate());
        add.setDatePosted(Utilities.getDate());
        return accommmodationDAO.createAccommodationAddFromFacebook(user, add, apartmentId);

    }

    public List<RAccommodationAdd> getAccommodationNotifications(Users user, int position) throws Exception {

        List<AccommodationAdd> accommodationAdds;
        accommodationAdds = accommmodationDAO.getAccommodationNotifications(user, position);

        List<Long> addIds = getUserVisitedAdds(user);
        return getRAccommodationAdds(accommodationAdds, addIds, -1);
    }

    public String createAccommodationAdd(AccommodationAdd add, long userId, int apartmentId) throws Exception {

        add.setDatePosted(Utilities.getDate());

        validatePostAccommodation(userId, add, apartmentId);

        return accommmodationDAO.createAccommodationAdd(userId, add, apartmentId);
    }

    public String deleteAccommodationAdd(int addId, Users users) throws Exception {

        AccommodationAdd add = accommmodationDAO.getByKey(AccommodationAdd.class, addId);
        long userId = add != null ? add.getUser().getUserId() : 0;

        if (userId > 0 && (userId == users.getUserId() || userService.checkAdminUserId(userId))) {

            accommmodationDAO.deleteAccommodationAdd(add);

            return SAConstants.RESPONSE_SUCCESS;

        }

        throw new BadStudentRequestException();
    }

    public List<RAccommodationAdd> getUserPosts(long userId, int position) throws Exception {

        List<AccommodationAdd> userAdds = accommmodationDAO.getUserPosts(userId, position);
        return getRAccommodationAdds(userAdds, null, -1);

    }

    /**
     * gets all Apartment Names for the User's chosen Universities. usage:
     * simpleSearch, advancedSearch, PostAccommodation.
     *
     * @param currentUser
     * @return
     * @throws Exception
     */
    public List<RApartmentNames> getAllApartmentNames(Users currentUser) throws Exception {

        List<Apartments> apartments = accommmodationDAO.getAllApartmentNames(currentUser);

        List<RApartmentNames> rApartments = new ArrayList<>();

        for (Apartments apt : apartments) {
            rApartments.add(new RApartmentNames(apt.getApartmentName()));
        }
        return rApartments;
    }

    /**
     * @param apartmentName
     * @param gender
     * @param universityId
     * @param position
     * @return
     * @throws Exception
     */
    public List<RAccommodationAdd> getAdvancedAdvertisements(String apartmentName, String gender, int universityId,
                                                             int position) throws Exception {

        List<AccommodationAdd> advancedSearchAdds = accommmodationDAO.getAdvancedAdvertisements(apartmentName, gender,
                universityId, position);

        // List<Long> addIds = getUserVisitedAdds(list);

        return getRAccommodationAdds(advancedSearchAdds, null, -1);

    }

    public List<UniversityAccommodationDTO> getSimpleSearchAdds(AccommodationSearchDTO accommodationSearch, Users user)
            throws Exception {

        List<AccommodationAdd> simpleSearchAdds = accommmodationDAO.getSimpleSearchAdds(
                accommodationSearch.getLeftSpinner(), accommodationSearch.getRightSpinner(),
                accommodationSearch.getUniversityIds());

        List<Long> userVisitedAdds = null;
        if (user != null) {
            userVisitedAdds = getUserVisitedAdds(user);
        }

        List<RAccommodationAdd> accommodationAdds = getRAccommodationAdds(simpleSearchAdds, userVisitedAdds, 2);

        Map<Integer, UniversityAccommodationDTO> perUnivListing = new HashMap<Integer, UniversityAccommodationDTO>();

        for (RAccommodationAdd add : accommodationAdds) {

            if (perUnivListing.containsKey(add.getUniversityId())) {

                UniversityAccommodationDTO univAdd = perUnivListing.get(add.getUniversityId());
                univAdd.getAccommodationAdds().add(add);

            } else {
                List<RAccommodationAdd> addsList = new ArrayList<RAccommodationAdd>();
                UniversityAccommodationDTO univAcc = new UniversityAccommodationDTO();

                addsList.add(add);
                univAcc.setAccommodationAdds(addsList);
                univAcc.setUniversityId(add.getUniversityId());
                univAcc.setUniversityName(add.getUniversityName());
                univAcc.setUrls(add.getUniversityPhotoUrl());

                perUnivListing.put(add.getUniversityId(), univAcc);
            }
        }

        List<UniversityAccommodationDTO> univAddsList = new ArrayList();
        perUnivListing.entrySet().stream().forEach(listing -> univAddsList.add(listing.getValue()));
        return univAddsList;

    }

    public List<RAccommodationAdd> getSimpleSearchAddsPagination(String leftSpinner, String rightSpinner, int position,
                                                                 int universityId, Users users) throws Exception {

        List<AccommodationAdd> simpleSearchAdds = accommmodationDAO.getSimpleSearchAddsPagination(leftSpinner,
                rightSpinner, position, universityId);

        List<Long> userVisitedAdds = null;
        if (users != null) {
            userVisitedAdds = getUserVisitedAdds(users);
        }

        return getRAccommodationAdds(simpleSearchAdds, userVisitedAdds, -1);
    }

    public List<RApartmentNames> getApartmentNames(String apartmentType) throws Exception {

        List<Apartments> apartments = accommmodationDAO.getApartmentNames(apartmentType);
        List<RApartmentNames> rApartments = new ArrayList<>();
        for (Apartments apt : apartments) {
            rApartments.add(new RApartmentNames(apt.getApartmentName()));
        }
        return rApartments;
    }

    public List<RApartmentNamesWithType> getApartmentNamesWithType(List<Integer> universityIds) throws Exception {

        List<Apartments> apartments = accommmodationDAO.getApartmentNamesWithType(universityIds);
        List<RApartmentNamesWithType> rApartments = new ArrayList<>();

        for (Apartments apartment : apartments) {
            rApartments.add(new RApartmentNamesWithType(apartment.getApartmentName(), apartment.getApartmentType(),
                    apartment.getUniversity().getUniversityId(), apartment.getId()));
        }
        return rApartments;
    }

    public String setUserVisitedAdds(Users user, int addId) throws Exception {

        UserVisitedAdds userVisitedAdds = new UserVisitedAdds();
        AccommodationAdd accommodationAdd = new AccommodationAdd();
        accommodationAdd.setAddId(addId);

        userVisitedAdds.setUser(user);
        userVisitedAdds.setCreateDate(Utilities.getDate());
        userVisitedAdds.setAccommodationAdd(accommodationAdd);

        // doubt them
        user.getUserVisitedAdds().add(userVisitedAdds);
        accommodationAdd.getUserVisitedAdds().add(userVisitedAdds);

        return accommmodationDAO.setUserVisitedAdds(userVisitedAdds);
    }

    public List<Long> getUserVisitedAdds(Users user) throws Exception {

        return accommmodationDAO.getUserVisitedAdds(user);

    }

    public List<RAccommodationAdd> getRecentlyViewed(Users user, int position) {

        List<AccommodationAdd> accommodationAdds;

        accommodationAdds = accommmodationDAO.getRecentlyViewed(user, position);

        return getRAccommodationAdds(accommodationAdds, null, -1);

    }

    /**
     * priority => 1. Logo 2. Building Image
     *
     * @param accommodationAdds
     * @param addIds
     * @return
     */
    public List<RAccommodationAdd> getRAccommodationAdds(List<AccommodationAdd> accommodationAdds, List<Long> addIds,
                                                         int photoPriority) {

        List<RAccommodationAdd> rAdds = new ArrayList<RAccommodationAdd>();

        for (AccommodationAdd add : accommodationAdds) {

            System.out.println("apurv here hereeererere");
            if (add.getNoOfRooms() != null) {
                add.setNoOfRooms(add.getNoOfRooms().replaceAll("\\s+", ""));
            }

            String universityPhoto = SAConstants.EmptyText;
            List<UniversityPhotos> photos = add.getUniversity().getUniversityPhotos();

            if (photoPriority != -1) {
                universityPhoto = photos.stream().filter(photo -> photo.getPhotoPriority() == photoPriority).findFirst()
                        .map(photo -> photo.getPhotoUrl()).orElse(null);// get();

            }

            Users user = add.getUser();
            if (addIds != null) {

                if (addIds.contains(add.getAddId())) {
                    rAdds.add(new RAccommodationAdd(add.getVacancies(), add.getGender(), add.getNoOfRooms(),
                            add.getCost(), add.getFbId(), add.getNotes(), user.getUserId(),
                            add.getApartment().getApartmentName(), user.getFirstName(), user.getLastName(),
                            user.getEmail(), user.getPhoneNumber(), add.getAddId(), true,
                            new SimpleDateFormat("dd MMM").format(add.getDatePosted()), add.getAddPhotoIds(),
                            add.getApartment().getUniversity().getUniversityId(),
                            add.getApartment().getUniversity().getUniversityName(), universityPhoto,
                            add.getApartment().getUniversity().getUnivAcronym(), add.getApartment().getCity(),
                            add.getApartment().getState(), add.getApartment().getZip(),
                            add.getApartment().getAddr_line(),
                            SAConstants.apartmentTypeCodeMap.get(add.getApartment().getApartmentType()),
                            add.getApartment().getId(), add.getPostedTill()));
                } else {
                    rAdds.add(new RAccommodationAdd(add.getVacancies(), add.getGender(), add.getNoOfRooms(),
                            add.getCost(), add.getFbId(), add.getNotes(), user.getUserId(),
                            add.getApartment().getApartmentName(), user.getFirstName(), user.getLastName(),
                            user.getEmail(), user.getPhoneNumber(), add.getAddId(), false,
                            new SimpleDateFormat("dd MMM").format(add.getDatePosted()), add.getAddPhotoIds(),
                            add.getApartment().getUniversity().getUniversityId(),
                            add.getApartment().getUniversity().getUniversityName(), universityPhoto,
                            add.getApartment().getUniversity().getUnivAcronym(), add.getApartment().getCity(),
                            add.getApartment().getState(), add.getApartment().getZip(),
                            add.getApartment().getAddr_line(),
                            SAConstants.apartmentTypeCodeMap.get(add.getApartment().getApartmentType()),
                            add.getApartment().getId(), add.getPostedTill()));
                }
            } else {

                rAdds.add(new RAccommodationAdd(add.getVacancies(), add.getGender(), add.getNoOfRooms(), add.getCost(),
                        add.getFbId(), add.getNotes(), user.getUserId(), add.getApartment().getApartmentName(),
                        user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNumber(), add.getAddId(),
                        false, new SimpleDateFormat("dd MMM").format(add.getDatePosted()), add.getAddPhotoIds(),
                        add.getApartment().getUniversity().getUniversityId(),
                        add.getApartment().getUniversity().getUniversityName(), universityPhoto,
                        add.getApartment().getUniversity().getUnivAcronym(), add.getApartment().getCity(),
                        add.getApartment().getState(), add.getApartment().getZip(), add.getApartment().getAddr_line(),
                        SAConstants.apartmentTypeCodeMap.get(add.getApartment().getApartmentType()),
                        add.getApartment().getId(), add.getPostedTill()));

            }

        }

        return rAdds;

    }

    public List<UniversityAccommodationDTO> getSimpleSearchAddsNg(AccommodationSearchDTO accommodationSearch) {

        accommmodationDAO.getSimpleSearchAddsNg(accommodationSearch);

        return null;
    }

    public int addNewApartment(Apartments apartment, int universityId) throws Exception {

        validateNewApartment(apartment, universityId);
        return accommmodationDAO.addNewApartment(apartment, universityId);
    }

    private void validatePostAccommodation(long userId, AccommodationAdd advertisement, int apartmentId)
            throws BadStudentRequestException {

        if (userId < 1 || advertisement == null || apartmentId < 1) {
            throw new BadStudentRequestException();
        }

        LocalDate _30daysLocalDate = LocalDate.now().plusDays(30);
        LocalDate postedTill = advertisement.getPostedTill().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (!(postedTill.isBefore(_30daysLocalDate) || postedTill.isAfter(Utilities.getLocaleDate()))) {
            throw new BadStudentRequestException();
        }

        if (advertisement.getCost() < 1) {
            throw new BadStudentRequestException();
        }

        if (advertisement.getAddPhotoIds().size() > 3) {
            throw new BadStudentRequestException();
        }
    }

    private void validateNewApartment(Apartments apartment, int universityId) throws BadStudentRequestException {

        if (apartment == null || universityId < 1) {
            throw new BadStudentRequestException();
        }

        if (apartment.getZip() < 1 || apartment.getApartmentName() == null || apartment.getApartmentName().isEmpty()) {
            throw new BadStudentRequestException();
        }
    }

    public RAccommodationAdd getAccommodationFromId(int addId) {
        System.out.println("apurv came here second place");

        List<AccommodationAdd> adds = new ArrayList<>();
        adds.add(accommmodationDAO.getAccommodationFromId(addId));

        return getRAccommodationAdds(adds, null, 2).get(0);

    }

    public String editAccommodationAdd(AccommodationAdd add, long userId, int apartmentId) throws Exception {

        add.setDatePosted(Utilities.getDate());
        validatePostAccommodation(userId, add, apartmentId);

        AccommodationAdd dbAdd = accommmodationDAO.getAccommodationFromId(add.getAddId());
        dbAdd = mapper.map(add, AccommodationAdd.class);
        return accommmodationDAO.updateAccommodationAdd(dbAdd, apartmentId);
    }
}
