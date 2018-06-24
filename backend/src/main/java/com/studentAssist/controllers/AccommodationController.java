package com.studentAssist.controllers;

import com.studentAssist.entities.AccommodationAdd;
import com.studentAssist.entities.Users;
import com.studentAssist.exception.InvalidTokenException;
import com.studentAssist.response.*;
import com.studentAssist.services.AccommodationService;
import com.studentAssist.services.NotificationsService;
import com.studentAssist.services.UserService;
import com.studentAssist.util.FBGraph;
import com.studentAssist.util.InsertApartmentDetails;
import com.studentAssist.util.SAConstants;
import com.studentAssist.util.UserVisitedAddsRest;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class AccommodationController extends AbstractController {

    @Autowired
    private AccommodationService accommodationService;

    @Autowired
    private NotificationsService notificationService;

    @Autowired
    InsertApartmentDetails aptDetails;

    @Autowired
    private Mapper mapper;

    @Autowired
    FBGraph fbGraph;

    @Autowired
    UserService userService;

    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT, value = "profile/createAccommodationAddFromFacebook")
    public String createAccommodationAddFromFacebook(@RequestBody RAccommodationAdd rAccommodationAdd,
                                                     HttpServletRequest request) throws Exception {

        userService.validateAdminUser(getUserFromRequest(request).getUserId());
        AccommodationAdd add = mapper.map(rAccommodationAdd, AccommodationAdd.class);
        Users user = mapper.map(rAccommodationAdd, Users.class);

        return accommodationService.createAccommodationAddFromFacebook(add, user, rAccommodationAdd.getApartmentId());

    }

    /**
     * Used in Accommodation Activity Notifications tab where user can see list
     * of his notifications(list of accommodation Notifications only)
     *
     * @param position
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET, value = "profile/getAccommodationNotifications")
    public List<RAccommodationAdd> getAccommodationNotifications(@RequestParam("position") int position,
                                                                 HttpServletRequest request) throws Exception {

        return accommodationService.getAccommodationNotifications(getUserFromRequest(request), position);

    }

    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT, value = "profile/createAccommodationAdd")
    public String createAccommodationAdd(@RequestBody RAccommodationAdd rAccommodationAdd, HttpServletRequest request)
            throws Exception {

        Users user = getUserFromRequest(request);
        long userId = user.getUserId();
        AccommodationAdd add = mapper.map(rAccommodationAdd, AccommodationAdd.class);
        return accommodationService.createAccommodationAdd(add, userId, rAccommodationAdd.getApartmentId());

    }

    @RequestMapping(method = RequestMethod.DELETE, value = "profile/deleteAccommodationAdd")
    public String deleteAccommodationAdd(@RequestParam("addId") String addId, HttpServletRequest request)
            throws NumberFormatException, Exception {

        return accommodationService.deleteAccommodationAdd(Integer.parseInt(addId), getUserFromRequest(request));
    }

    @RequestMapping(method = RequestMethod.GET, value = "profile/getUserPosts")
    public List<RAccommodationAdd> getUserPosts(@RequestParam("position") int position, HttpServletRequest request)
            throws Exception {
        return accommodationService.getUserPosts(getUserFromRequest(request).getUserId(), position);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getAllApartmentNames")
    public List<RApartmentNames> getAllApartmentNames(HttpServletRequest request) throws Exception {

        return accommodationService.getAllApartmentNames(getUserFromRequest(request));
    }

    @RequestMapping(method = RequestMethod.POST, value = "/getAdvancedSearchAdds")
    public List<RAccommodationAdd> getAdvancedSearchAddsWebApp(@RequestBody AccommodationSearchDTO searchParams,
                                                               HttpServletRequest request) throws Exception {

        return accommodationService.getAdvancedAdvertisements(searchParams.getApartmentName(), searchParams.getGender(),
                searchParams.getSelectedUniversityId(), searchParams.getPaginationPosition());

    }

    /**
     * Pagination for returning additional accommodation Adds
     *
     * @param request
     * @return
     * @throws Exception
     * @throws InvalidTokenException
     */
    @RequestMapping(method = RequestMethod.POST, value = "/getSimpleSearchAddsPagination")
    public List<RAccommodationAdd> getSimpleSearchAddsPagination(@RequestBody AccommodationSearchDTO filterData,
                                                                 HttpServletRequest request) throws InvalidTokenException, Exception {

        return accommodationService.getSimpleSearchAddsPagination(filterData.getLeftSpinner(),
                filterData.getRightSpinner(), filterData.getPaginationPosition(), filterData.getSelectedUniversityId(),
                getUserFromToken(request));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getAllUnivDetails")
    public List<RApartmentNames> getApartmentNames(@RequestParam("apartmentType") String apartmentType,
                                                   HttpServletRequest request) throws Exception {

        return accommodationService.getApartmentNames(apartmentType);

    }

    @RequestMapping(method = RequestMethod.POST, value = "profile/apartments")
    public int addNewApartment(@RequestBody ApartmentDTO apartmentDto, HttpServletRequest request) throws Exception {

        Users user = getUserFromRequest(request);
        return accommodationService.addNewApartment(apartmentDto, user);

    }

    @RequestMapping(method = RequestMethod.POST, value = "/getAllApartmentsWithType")
    public List<ApartmentDTO> getAllApartmentsWithType(HttpServletRequest request,
                                                                  @RequestBody AccommodationSearchDTO aptNames) throws Exception {

        List<Integer> universityIds = aptNames.getUniversityIds();
        return accommodationService.getApartmentNamesWithType(universityIds);

    }

    @RequestMapping(method = RequestMethod.PUT, value = "profile/setUserVisitedAdds")
    public String setUserVisitedAdds(@RequestBody UserVisitedAddsRest userVisited, HttpServletRequest request)
            throws NumberFormatException, Exception {

        String addId = userVisited.getAddId();

        if (addId != null && addId != "") {
            return accommodationService.setUserVisitedAdds(getUserFromRequest(request), Integer.parseInt(addId));
        }

        return SAConstants.RESPONSE_FAILURE;

    }

    @RequestMapping(method = RequestMethod.GET, value = "/addAptsToDb")
    public String addAptsToDb(String test) {

        return aptDetails.addAptsToNewDb();

    }

    @RequestMapping(method = RequestMethod.GET, value = "profile/getRecentlyViewed")
    public List<RAccommodationAdd> getRecentlyViewed(@RequestParam("position") int position,
                                                     HttpServletRequest request) {

        return accommodationService.getRecentlyViewed(getUserFromRequest(request), position);

    }

    @RequestMapping(method = RequestMethod.POST, value = "/getSimpleSearchAdds", headers = {
            "content-type=application/json"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<UniversityAccommodationDTO> getSimpleSearchAdds(@RequestBody AccommodationSearchDTO accommodationSearch,
                                                                HttpServletRequest request) throws Exception {

        return accommodationService.getSimpleSearchAdds(accommodationSearch, getUserFromToken(request));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/accommodationAdds/{addId}")
    public RAccommodationAdd getAccommodationFromAddId(@PathVariable int addId, HttpServletRequest request)
            throws Exception {
        return accommodationService.getAccommodationFromId(addId);
    }

    private Users getUserFromToken(HttpServletRequest request) throws InvalidTokenException {

        String access_token = request.getHeader(SAConstants.ACCESS_TOKEN);
        Users user = null;
        if (access_token != null && !access_token.isEmpty() && !"0".equals(access_token)) {
            user = fbGraph.getUserDetails(access_token);
        }

        return user;
    }

    @CrossOrigin
    @RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT, value = "profile/editAccommodationAdd")
    public String editAccommodationAdd(@RequestBody RAccommodationAdd rAccommodationAdd, HttpServletRequest request)
            throws Exception {

        Users user = getUserFromRequest(request);
        long userId = user.getUserId();
        return accommodationService.editAccommodationAdd(rAccommodationAdd, userId);

    }

    @CrossOrigin
    @GetMapping(value = "profile/validations/duplicates/{userId}")
    public boolean validateActiveAccommodationByUser(@PathVariable long userId)
            throws Exception {
        return accommodationService.validateActiveAccommodationByUser(userId);
    }

}
