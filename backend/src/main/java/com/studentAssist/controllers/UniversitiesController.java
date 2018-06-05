package com.studentAssist.controllers;

import com.studentAssist.exception.InvalidTokenException;
import com.studentAssist.response.FlashCardsRequestDTO;
import com.studentAssist.response.FlashCardsResponseDTO;
import com.studentAssist.response.RUniversity;
import com.studentAssist.services.UniversitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class UniversitiesController extends AbstractController {

    @Autowired
    private UniversitiesService universitiesService;

    /**
     * Returns only names of Universities
     *
     * @return List<String>
     */
    @RequestMapping(method = RequestMethod.GET, value = "/universities/all")
    public List<RUniversity> getAllUniversities() {
        return universitiesService.getAllUniversities();
    }

    /**
     * called from UniversitiesActivity when the list of Univs is shown to the
     * user for the first time/new user
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getAllUniversitiesList")
    public List<RUniversity> getAllUniversitiesList(HttpServletRequest request) throws Exception {

        return universitiesService.getUniversityNames(getUserFromRequest(request));
    }

    /**
     * Returns boolean value of true or false to check if the user has
     * universities or not. This is called before displaying the home screen to
     * check if the user has univs else redirect him/her to select univs page
     *
     * @param request
     * @return
     * @throws InvalidTokenException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getUniversityDetailsForUser")
    public String getUniversityDetailsForUser(HttpServletRequest request) throws InvalidTokenException {

        return universitiesService.doesUserHaveUnivs(getUserFromRequest(request));
    }

    /**
     * Called when the user opens the settings page to populate the univs the
     * user has already selected.
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, value = "profile/getUserUniversities")
    public List<RUniversity> getUserUniversities(HttpServletRequest request) throws Exception {

        return universitiesService.getUserSelectedUniversities(getUserFromRequest(request));
    }

    /**
     * @param request
     * @return
     * @throws InvalidTokenException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/getUniversityNamesWithId")
    public List<RUniversity> getUniversityNamesWithId(HttpServletRequest request) throws InvalidTokenException {

        return universitiesService.getUniversityNamesWithId(getUserFromRequest(request));
    }

    /**
     * @param request
     * @return
     * @throws InvalidTokenException
     */
    @RequestMapping(method = RequestMethod.GET, value = "/universityName/{universityName}")
    public List<RUniversity> searchUniversitiesByName(HttpServletRequest request,
                                                      @PathVariable("universityName") String universityName) throws InvalidTokenException {

        return universitiesService.getUniversitiesByName(universityName);
    }

    /**
     * @param request
     * @return
     * @throws InvalidTokenException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/getFlashCards")
    public FlashCardsResponseDTO getFlashCards(HttpServletRequest request, @RequestBody FlashCardsRequestDTO flashCards)
            throws InvalidTokenException {

        return universitiesService.getFlashCards(flashCards);
    }

    /**
     * @param request
     * @return
     * @throws InvalidTokenException
     */
    @RequestMapping(method = RequestMethod.GET, value = "profile/getUniversityDetails/{universityId}")
    public RUniversity getUniversityDetails(HttpServletRequest request, @PathVariable int universityId) throws InvalidTokenException {

        return universitiesService.getUniversityDetails(universityId);
    }

}
