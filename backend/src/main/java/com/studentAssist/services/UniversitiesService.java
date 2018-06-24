package com.studentAssist.services;

import com.studentAssist.dao.AccommodationDAO;
import com.studentAssist.dao.UniversitiesDAO;
import com.studentAssist.entities.AccommodationAdd;
import com.studentAssist.entities.Universities;
import com.studentAssist.entities.Users;
import com.studentAssist.response.*;
import com.studentAssist.util.SAConstants;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UniversitiesService {

    @Autowired
    UniversitiesDAO universitiesDAO;

    @Autowired
    AccommodationDAO accommodationDAO;

    @Autowired
    AccommodationService accommodationService;

    @Autowired
    AirportService airportService;

    @Autowired
    private Mapper mapper;

    public List<RUniversity> getUniversityNames(Users currentUser) throws Exception {

        List<Object[]> universities = universitiesDAO.getUniversityNames(currentUser);
        return getRuniversity(universities);
    }

    private List<RUniversity> getRuniversity(List<Object[]> universityObjects) {

        List<RUniversity> rUniv = new ArrayList<RUniversity>();

        for (Object[] universityObj : universityObjects) {
            rUniv.add(new RUniversity((int) universityObj[0], (String) universityObj[8], (String) universityObj[4],
                    (String) universityObj[11], ((BigInteger) universityObj[12]).intValue(),
                    (String) universityObj[2] + ", " + (String) universityObj[6],
                    ((Integer) universityObj[5]).intValue(), ((BigInteger) universityObj[10]).intValue(),
                    (String) universityObj[7]));
        }
        return rUniv;
    }

    public String doesUserHaveUnivs(Users currentUser) {

        return universitiesDAO.doesUserHaveUnivs(currentUser) ? SAConstants.USER_HAS_UNIVS
                : SAConstants.USER_DOES_NOT_HAVE_UNIVS;

    }

    public List<RUniversity> getUserUniversities(Users currentUser) throws Exception {

        List<RUniversity> allUniversities = getRuniversity(universitiesDAO.getUniversityNames(currentUser));

        Users dbUser = universitiesDAO.getUser(currentUser);

        if (dbUser != null) {
            populateUserSelectedUniversities(allUniversities, universitiesDAO.getUserUniversities(currentUser));
        }
        return allUniversities;

    }

    public List<RUniversity> getUserSelectedUniversities(Users currentUser) throws Exception {

        Users dbUser = universitiesDAO.getUser(currentUser);

        List<Universities> dbUnivs = universitiesDAO.getUserUniversities(dbUser);
        List<RUniversity> userUnivs = new ArrayList();
        for (Universities dbUniv : dbUnivs) {

            RUniversity univ = new RUniversity();
            univ.setUniversityId(dbUniv.getUniversityId());
            univ.setUniversityName(dbUniv.getUniversityName());
            univ.setUnivAcronym(dbUniv.getUnivAcronym());

            userUnivs.add(univ);
        }
        return userUnivs;
    }

    private void populateUserSelectedUniversities(List<RUniversity> allUniversities,
                                                  List<Universities> userUniversities) {

        for (RUniversity dbUniversity : allUniversities) {
            for (Universities userUniversity : userUniversities) {
                if (userUniversity.getUniversityId() == dbUniversity.getUniversityId()) {
                    dbUniversity.setSelected(true);

                }

            }

        }

    }

    public List<RUniversity> getAllUniversities() {
        return universitiesDAO
                .getAllUniversities()
                .stream()
                .map(univ -> mapper.map(univ, RUniversity.class))
                .collect(Collectors.toList());
    }

    public List<RUniversity> getUniversityNamesWithId(Users user) {

        List<RUniversity> rUniv = new ArrayList<RUniversity>();

        List<Object[]> universities = universitiesDAO.getUniversityNamesWithId(user);

        for (Object[] universitiy : universities) {
            rUniv.add(new RUniversity((Integer) universitiy[0], (String) universitiy[1]));
        }
        return rUniv;

    }

    public List<RUniversity> getUniversitiesByName(String searchString) {

        return universitiesDAO
                .getUniversitiesByName(searchString)
                .stream()
                .map(university ->
                        new RUniversity() {
                            {
                                setUniversityId(university.getUniversityId());
                                setUniversityName(university.getUniversityName());
                                setUnivAcronym(university.getUnivAcronym());
                            }
                        }
                ).collect(Collectors.toList());

    }

    public FlashCardsResponseDTO getFlashCards(FlashCardsRequestDTO flashCardsRequestDTO) {
        FlashCardsResponseDTO flashCardResponseDTO = new FlashCardsResponseDTO();
        int selectedUniversityID;

        if (flashCardsRequestDTO.getUniversityIDs() != null && !flashCardsRequestDTO.getUniversityIDs().isEmpty()) {
            selectedUniversityID = getSelectedUniversityID(flashCardsRequestDTO.getUniversityIDs(),
                    flashCardsRequestDTO.getCurrentUniversityID());
        } else {
            selectedUniversityID = getRecentUniversityID();
        }

        List<AirportDTO> airportCards = getAirportCards(selectedUniversityID);
        flashCardResponseDTO.setAirportCards(airportCards);
        flashCardResponseDTO.setAccomodationCards(getAccomodationCardsofSelectedUniversity(selectedUniversityID, (4 - airportCards.size())));
        flashCardResponseDTO.setCurrentUniversityID(selectedUniversityID);

        return flashCardResponseDTO;
    }

    private List<AirportDTO> getAirportCards(int selectedUniversityID) {

        return airportService.
                getAirportServices(selectedUniversityID)
                .stream()
                .map(x -> new AirportDTO() {{
                            setUniversityId(selectedUniversityID);
                            setUnivAcronym(x.getUniversity().getUnivAcronym());
                            setUniversityName(x.getUniversity().getUniversityName());
                            setUniversityPhotoUrl(x
                                    .getUniversity()
                                    .getUniversityPhotos()
                                    .stream()
                                    .filter(y -> y.getPhotoPriority() == 1)
                                    .findFirst()
                                    .get()
                                    .getPhotoUrl());
                            setUniversityPickupUrl(x.getGroupLink());
                        }}
                ).collect(Collectors.toList());

    }

    private List<RAccommodationAdd> getAccomodationCardsofSelectedUniversity(int selectedUniversityID,
                                                                             int numberOfCards) {

        List<AccommodationAdd> accomodationAdds = accommodationDAO
                .getAccommodationCardsByUniversityId(selectedUniversityID, numberOfCards);
        return accommodationService.getRAccommodationAdds(accomodationAdds, null, 1);

    }

    private int getRecentUniversityID() {
        return accommodationDAO.getRecentAccommodationAdd().getUniversity().getUniversityId();
    }

    private int getSelectedUniversityID(List<Integer> universitiesIDs, int currentUniversityID) {
        if (currentUniversityID > 0 && universitiesIDs.contains(currentUniversityID)) {
            int currentUniversityIndex = universitiesIDs.indexOf(currentUniversityID);
            if (currentUniversityIndex != universitiesIDs.size() - 1) {
                return universitiesIDs.get(currentUniversityIndex + 1);
            } else {
                return universitiesIDs.get(0);
            }
        } else {
            return universitiesIDs.get(0);
        }
    }

    public RUniversity getUniversityDetails(int universityId) {
        return mapper.map(universitiesDAO.getUniversityDetails(universityId), RUniversity.class);
    }
}
