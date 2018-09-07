
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

import javax.persistence.criteria.CriteriaBuilder;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;

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

        List<AccommodationAdd> accommodationAdds = accommmodationDAO.getAccommodationNotifications(user, position);

        List<Integer> addIds = getUserVisitedAdds(user);
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

        List<Integer> userVisitedAdds = null;
        if (user != null) {
            userVisitedAdds = getUserVisitedAdds(user);
        }

        return getRAccommodationAdds(simpleSearchAdds, userVisitedAdds, 2)
                .stream()
                .collect(collectingAndThen(groupingBy(RAccommodationAdd::getUniversityId),
                        m -> m.entrySet()
                                .stream()
                                .map(univ -> new UniversityAccommodationDTO() {
                                    {
                                        setUniversityId(univ.getKey());
                                        setUniversityName(univ.getValue().get(0).getUniversityName());
                                        setUrls(univ.getValue().get(0).getUniversityPhotoUrl());
                                        setAccommodationAdds(univ.getValue());
                                    }
                                })
                                .collect(Collectors.toList())));

    }

    public List<RAccommodationAdd> getSimpleSearchAddsPagination(String leftSpinner, String rightSpinner, int position,
                                                                 int universityId, Users users) throws Exception {

        List<AccommodationAdd> simpleSearchAdds = accommmodationDAO.getSimpleSearchAddsPagination(leftSpinner,
                rightSpinner, position, universityId);

        List<Integer> userVisitedAdds = null;
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

    private List<Integer> getUserVisitedAdds(Users user) throws Exception {

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
    public List<RAccommodationAdd> getRAccommodationAdds(List<AccommodationAdd> accommodationAdds, List<Integer> addIds,
                                                         int photoPriority) {

        Set<Integer> addIdsSet = new HashSet<>(addIds != null ? addIds : Collections.emptyList());
        return accommodationAdds.stream().map(add -> {

            if (add.getNoOfRooms() != null) {
                add.setNoOfRooms(add.getNoOfRooms().replaceAll("\\s+", ""));
            }

            Users user = add.getUser();
            return new RAccommodationAdd() {{
                setVacancies(add.getVacancies());
                setGender(add.getGender());
                setNoOfRooms(add.getNoOfRooms());
                setCost(add.getCost());
                setFbId(add.getFbId());
                setNotes(add.getNotes());
                setAddId(add.getAddId());
                setApartmentName(add.getApartment().getApartmentName());
                setAddPhotoIds(add.getAddPhotoIds());
                setUniversityId(add.getUniversity().getUniversityId());
                setUniversityName(add.getUniversity().getUniversityName());
                setUnivAcronym(add.getUniversity().getUnivAcronym());
                setCity(add.getApartment().getCity());
                setState(add.getApartment().getState());
                setZip(add.getApartment().getZip());
                setAddrLine(add.getApartment().getAddr_line());
                setApartmentId(add.getApartment().getId());
                setPostedTill(add.getPostedTill());
                setUserVisitedSw(addIdsSet.contains(add.getAddId()));
                setUniversityPhotoUrl(add
                        .getUniversity()
                        .getUniversityPhotos()
                        .stream()
                        .filter(photo -> photo.getPhotoPriority() == photoPriority)
                        .findFirst()
                        .map(UniversityPhotos::getPhotoUrl)
                        .orElse(SAConstants.EmptyText));

                setUserId(user.getUserId());
                setFirstName(user.getFirstName());
                setLastName(user.getLastName());
                setEmailId(user.getEmail());
                setPhoneNumber(user.getPhoneNumber());
                setCreateDate(new SimpleDateFormat("dd MMM").format(add.getDatePosted()));
                setApartmentType(SAConstants.apartmentTypeCodeMap.get(add.getApartment().getApartmentType()));
            }};
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
