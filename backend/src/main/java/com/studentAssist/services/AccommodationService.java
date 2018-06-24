
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
import java.util.*;
import java.util.stream.Collectors;

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

        return getRAccommodationAdds(accommmodationDAO.getUserPosts(userId, position), null, -1);

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
        return accommmodationDAO
                .getAllApartmentNames(currentUser)
                .stream()
                .map(apartment -> new RApartmentNames(apartment.getApartmentName()))
                .collect(Collectors.toList());
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

        Map<Integer, UniversityAccommodationDTO> perUnivListing = new HashMap<>();

        for (RAccommodationAdd add : accommodationAdds) {

            if (perUnivListing.containsKey(add.getUniversityId())) {

                UniversityAccommodationDTO univAdd = perUnivListing.get(add.getUniversityId());
                univAdd.getAccommodationAdds().add(add);

            } else {
                List<RAccommodationAdd> addsList = new ArrayList<>();
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
        return accommmodationDAO.getApartmentNames(apartmentType)
                .stream().map(apartment -> new RApartmentNames(apartment.getApartmentName()))
                .collect(Collectors.toList());
    }

    public List<ApartmentDTO> getApartmentNamesWithType(List<Integer> universityIds) throws Exception {

        return accommmodationDAO
                .getApartmentNamesWithType(universityIds)
                .stream()
                .map(apartment ->
                        new ApartmentDTO() {{
                            setApartmentName(apartment.getApartmentName());
                            setApartmentType(apartment.getApartmentType());
                            setUniversityId(apartment.getUniversity().getUniversityId());
                            setApartmentId(apartment.getId());
                        }}).collect(Collectors.toList());
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

    private List<Long> getUserVisitedAdds(Users user) throws Exception {

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

        List<RAccommodationAdd> rAdds = new ArrayList<>();
        Set<Long> addIdsSet = new HashSet<>();
        if (addIds != null)
            addIdsSet.addAll(addIds);


        return accommodationAdds.stream().map(add -> {

            if (add.getNoOfRooms() != null) {
                add.setNoOfRooms(add.getNoOfRooms().replaceAll("\\s+", ""));
            }

            String universityPhoto = SAConstants.EmptyText;
            List<UniversityPhotos> photos = add.getUniversity().getUniversityPhotos();

            if (photoPriority != -1) {
                universityPhoto = photos.stream().filter(photo -> photo.getPhotoPriority() == photoPriority).findFirst()
                        .map(UniversityPhotos::getPhotoUrl).orElse(null);
            }
            boolean userVisited = addIdsSet.contains(add.getAddId());

            Users user = add.getUser();
            return new RAccommodationAdd(add.getVacancies(), add.getGender(), add.getNoOfRooms(),
                    add.getCost(), add.getFbId(), add.getNotes(), user.getUserId(),
                    add.getApartment().getApartmentName(), user.getFirstName(), user.getLastName(),
                    user.getEmail(), user.getPhoneNumber(), add.getAddId(), userVisited,
                    new SimpleDateFormat("dd MMM").format(add.getDatePosted()), add.getAddPhotoIds(),
                    add.getUniversity().getUniversityId(),
                    add.getUniversity().getUniversityName(), universityPhoto,
                    add.getUniversity().getUnivAcronym(), add.getApartment().getCity(),
                    add.getApartment().getState(), add.getApartment().getZip(),
                    add.getApartment().getAddr_line(),
                    SAConstants.apartmentTypeCodeMap.get(add.getApartment().getApartmentType()),
                    add.getApartment().getId(), add.getPostedTill());
        }).collect(Collectors.toList());

    }

    public int addNewApartment(ApartmentDTO apartmentDto, Users user) throws Exception {

        Apartments apartment = mapper.map(apartmentDto, Apartments.class);
        apartment.setUniversity(new Universities() {{
            setUniversityId(apartmentDto.getUniversityId());
        }});
        validateNewApartment(apartment, user);
        apartment.setAddedDate(Utilities.getDate());
        apartment.setCreatedUser(user);
        return accommmodationDAO.addNewApartment(apartment);
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

    private void validateNewApartment(Apartments apartment, Users user) throws BadStudentRequestException {

        if (apartment == null || apartment.getUniversity().getUniversityId() < 1) {
            throw new BadStudentRequestException();
        }

        if (apartment.getZip() < 1 || apartment.getApartmentName() == null || apartment.getApartmentName().isEmpty()) {
            throw new BadStudentRequestException();
        }
        List<Apartments> apartments = getApartmentsByUser(user);
        if (!userService.checkAdminUserId(user.getUserId()) && apartments.size() > 1) {
            throw new BadStudentRequestException(SAConstants.APARTMENT_ALREADY_ADDED + apartments.get(0).getApartmentName());
        }
    }

    public RAccommodationAdd getAccommodationFromId(int addId) {
        return getRAccommodationAdds(Arrays.asList(accommmodationDAO.getAccommodationFromId(addId)),
                null, 2).get(0);

    }

    private List<Apartments> getApartmentsByUser(Users user) {
        return accommmodationDAO.getApartmentsByUser(user);
    }

    public String editAccommodationAdd(RAccommodationAdd screenAdd, long userId) throws Exception {

        AccommodationAdd accommodationAdd = mapper.map(screenAdd, AccommodationAdd.class);

        accommodationAdd.setDatePosted(Utilities.getDate());
        accommodationAdd.setUser(new Users() {{
            setUserId(userId);
        }});
        accommodationAdd.setApartment(new Apartments() {{
            setId(screenAdd.getApartmentId());
        }});
        accommodationAdd.setUniversity(new Universities() {{
            screenAdd.getUniversityId();
        }});
        validatePostAccommodation(userId, accommodationAdd, screenAdd.getUniversityId());

        return accommmodationDAO.updateAccommodationAdd(accommodationAdd);
    }

    public boolean validateActiveAccommodationByUser(long userId) {
        return accommmodationDAO.validateActiveAccommodationByUser(userId);
    }
}
