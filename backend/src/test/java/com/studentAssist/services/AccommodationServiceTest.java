package com.studentAssist.services;

import com.studentAssist.controllers.AccommodationController;
import com.studentAssist.dao.AccommodationDAO;
import com.studentAssist.entities.AccommodationAdd;
import com.studentAssist.entities.Apartments;
import com.studentAssist.entities.Universities;
import com.studentAssist.entities.Users;
import com.studentAssist.interceptor.ExecuteInterceptor;
import com.studentAssist.response.AccommodationSearchDTO;
import com.studentAssist.response.RAccommodationAdd;
import com.studentAssist.response.RApartmentNames;
import com.studentAssist.response.UniversityAccommodationDTO;
import com.studentAssist.util.FBGraph;
import com.studentAssist.util.InsertApartmentDetails;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import sun.jvm.hotspot.utilities.AssertionFailure;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class AccommodationServiceTest {

    private MockMvc mockMvc;

    @InjectMocks
    private AccommodationController accommodationController;

    @InjectMocks
    AccommodationService accommodationService;

    @MockBean
    NotificationsService notificationsService;

    @MockBean
    UserService userService;

    @MockBean
    InsertApartmentDetails insertApartmentDetails;

    @Mock
    AccommodationDAO dao;

    @MockBean
    LoggingService loggingService;

    @MockBean
    FBGraph fbGraph;

    @MockBean
    ExecuteInterceptor executeInterceptor;

    private AccommodationAdd cooperChase;
    private AccommodationAdd mapleSqAdd;


    private RAccommodationAdd responseAdd;
    private AccommodationAdd someOtherUniv;
    private List<AccommodationAdd> addsList;
    Apartments mapleApt;
    Apartments cooperApt;


    @Before
    public void setup() {

        mockMvc = MockMvcBuilders.standaloneSetup(accommodationController)
                .build();

        cooperChase = new AccommodationAdd() {{
            setApartment(new Apartments() {{
                setId(2);
                setApartmentName("Cooper Chase");
                setCity("city");
                setState("TX");
                setZip(32746);
            }});
            setUser(new Users() {{
                setUserId(10207639158073180L);
                setFirstName("Apurv");
                setLastName("Kamalapuri");
            }});
            setUniversity(new Universities() {{
                setUniversityId(1160);
                setUniversityName("UT Arlington");

            }});
            setNoOfRooms("1bhk/1bath");
            setCost(123);
            setGender("Male");
            setNotes("notesssss");
            setAddId(1160);
            setDatePosted(new Date());
        }};

        mapleSqAdd = new AccommodationAdd() {{
            setApartment(new Apartments() {{
                setId(3);
                setApartmentName("Male Square");
                setCity("city");
                setState("TX");
                setZip(32746);
            }});
            setUser(new Users() {{
                setUserId(10207639158073180L);
                setFirstName("Apurv");
                setLastName("Kamalapuri");
            }});
            setUniversity(new Universities() {{
                setUniversityId(1160);
                setUniversityName("UT Arlington");

            }});
            setNoOfRooms("1bhk/1bath");
            setCost(123);
            setGender("Male");
            setNotes("notesssss");
            setAddId(1160);
            setDatePosted(new Date());
        }};

        someOtherUniv = new AccommodationAdd() {{
            setApartment(new Apartments() {{
                setId(5);
                setApartmentName("Some other Univ");
                setCity("city");
                setState("TX");
                setZip(32746);
            }});
            setUser(new Users() {{
                setUserId(10207639158073180L);
                setFirstName("Apurv");
                setLastName("Kamalapuri");
            }});
            setUniversity(new Universities() {{
                setUniversityId(1161);
                setUniversityName("Univ Of Alabama");

            }});
            setNoOfRooms("1bhk/1bath");
            setCost(123);
            setGender("Male");
            setNotes("notesssss");
            setAddId(1160);
            setDatePosted(new Date());
        }};


        responseAdd = new RAccommodationAdd() {{
            setApartmentName("Cooper Chase");
            setAddId(4);
            setUserId(10207639158073180L);
            setFirstName("Apurv");
            setLastName("Kamalapuri");
            setUniversityId(1160);
            setUniversityName("UT Arlington");
            setCity("city");
            setState("TX");
            setZip(32746);
            setNoOfRooms("1bhk/1bath");
            setCost(123);
            setGender("Male");
            setNotes("notesssss");
            setAddId(1160);
            setCreateDate(new SimpleDateFormat("dd MMM").format(cooperChase.getDatePosted()));

        }};

        addsList = Arrays.asList(mapleSqAdd, cooperChase, someOtherUniv);


        mapleApt = new Apartments() {{
            setApartmentName("Maple Square");
        }};
        cooperApt = new Apartments() {{
            setApartmentName("Cooper Chase");
        }};
    }

    @Test
    public void verifyAddFromAddId() throws Exception {
        given(this.dao.getAccommodationFromId(1160)).willReturn(cooperChase);
        RAccommodationAdd serviceAdd = accommodationService.getAccommodationFromId(1160);
        verifyAccommodationAddDetails(serviceAdd);

    }

    @Test
    public void verifyNullUserVisitedAdds() throws Exception {
        RAccommodationAdd serviceAdd = accommodationService.getRAccommodationAdds(Arrays.asList(cooperChase), null, 2).get(0);
        verifyAccommodationAddDetails(serviceAdd);
    }

    @Test
    public void verifyNullUserVisitedAddsPhotoPriorityMinus1() throws Exception {
        RAccommodationAdd serviceAdd = accommodationService.getRAccommodationAdds(Arrays.asList(cooperChase), null, -1).get(0);
        verifyAccommodationAddDetails(serviceAdd);
    }

    @Test
    public void verifyRAccAddsWithUserVisitedAdds() throws Exception {
        RAccommodationAdd serviceAdd = accommodationService.getRAccommodationAdds(Arrays.asList(cooperChase), Arrays.asList(1160), -1).get(0);
        verifyAccommodationAddDetails(serviceAdd);
        Assert.assertEquals(serviceAdd.getUserVisitedSw(), true);

    }

    @Test
    public void testAccommodationNotifications() throws Exception {
        given(this.dao.getAccommodationNotifications(null, 0)).willReturn(Arrays.asList(cooperChase));
        RAccommodationAdd serviceAdd = accommodationService.getAccommodationNotifications(null, 0).get(0);
        verifyAccommodationAddDetails(serviceAdd);
    }


    @Test
    public void testSimpleSearchAdds() throws Exception {
        Users testUser = new Users() {{
            setUserId(123);
        }};

        given(this.dao.getSimpleSearchAdds("On", "male", Arrays.asList(1, 2, 3)))
                .willReturn(addsList);
        given(this.dao.getUserVisitedAdds(testUser)).willReturn(Arrays.asList(1160));

        List<UniversityAccommodationDTO> serviceAdds = accommodationService.getSimpleSearchAdds(
                new AccommodationSearchDTO() {{
                    setLeftSpinner("On");
                    setRightSpinner("male");
                    setUniversityIds(Arrays.asList(1, 2, 3));
                }}, testUser);

        if (serviceAdds.isEmpty()) {
//            throw new AssertionFailure();
        }

        serviceAdds
                .forEach(univ -> {
                    Assert.assertEquals(addsList
                            .stream()
                            .filter(item -> item.getUniversity().getUniversityId() == univ.getUniversityId())
                            .map(item -> item.getApartment())
                            .collect(toList()).size(), univ.getAccommodationAdds().size());

                });
    }


    private void verifyAccommodationAddDetails(RAccommodationAdd serviceAdd) {
        Assert.assertEquals(serviceAdd.getUniversityName(), responseAdd.getUniversityName());
        Assert.assertEquals(serviceAdd.getAddId(), responseAdd.getAddId());
        Assert.assertEquals(serviceAdd.getUserId(), responseAdd.getUserId());
        Assert.assertEquals(serviceAdd.getFirstName(), responseAdd.getFirstName());
        Assert.assertEquals(serviceAdd.getLastName(), responseAdd.getLastName());
        Assert.assertEquals(serviceAdd.getUniversityId(), responseAdd.getUniversityId());
        Assert.assertEquals(serviceAdd.getUniversityName(), responseAdd.getUniversityName());
        Assert.assertEquals(serviceAdd.getCity(), responseAdd.getCity());
        Assert.assertEquals(serviceAdd.getState(), responseAdd.getState());
        Assert.assertEquals(serviceAdd.getZip(), responseAdd.getZip());
        Assert.assertEquals(serviceAdd.getNoOfRooms(), responseAdd.getNoOfRooms());
        Assert.assertEquals(serviceAdd.getCost(), responseAdd.getCost());
        Assert.assertEquals(serviceAdd.getGender(), responseAdd.getGender());
        Assert.assertEquals(serviceAdd.getNotes(), responseAdd.getNotes());
        Assert.assertEquals(serviceAdd.getAddId(), responseAdd.getAddId());
        Assert.assertEquals(serviceAdd.getCreateDate(), responseAdd.getCreateDate());


    }

    @Test
    public void createAccommodationAddFromFacebook() {
    }

    @Test
    public void createAccommodationAdd() {
    }

    @Test
    public void deleteAccommodationAdd() {
    }

    @Test
    public void getUserPosts() throws Exception {
        given(this.dao.getUserPosts(101, 0)).willReturn(Arrays.asList(cooperChase, mapleSqAdd));
        accommodationService
                .getUserPosts(101, 0)
                .stream()
                .forEach(add -> verifyAccommodationAddDetails(add));
    }

    @Test
    public void getAllApartmentNames() throws Exception {

        Users testUser = new Users() {{
            setUserId(123);
        }};

        List<Apartments> testApts = Arrays.asList(mapleApt, cooperApt);

        given(this.dao.getAllApartmentNames(testUser)).willReturn(testApts);

        List<RApartmentNames> apts = accommodationService
                .getAllApartmentNames(testUser);

//        testApts
//                .stream()
//                .forEach(apartment -> Assert.assertTrue(apts.contains(apartment)));

    }

    @Test
    public void getAdvancedAdvertisements() {
    }

    @Test
    public void getSimpleSearchAdds() {
    }

    @Test
    public void getSimpleSearchAddsPagination() {
    }

    @Test
    public void getApartmentNames() {
    }

    @Test
    public void getApartmentNamesWithType() {
    }

    @Test
    public void setUserVisitedAdds() {
    }

    @Test
    public void getRecentlyViewed() {
    }

    @Test
    public void getRAccommodationAdds() {
    }

    @Test
    public void addNewApartment() {
    }

    @Test
    public void getAccommodationFromId() {
    }

    @Test
    public void editAccommodationAdd() {
    }

    @Test
    public void validateActiveAccommodationByUser() {
    }

    @Test
    public void givenList_whenParitioningIntoSublistsUsingPartitionBy_thenCorrect() {
        List<Integer> intList = Lists.newArrayList(1, 2, 3, 4, 5, 6,7,8);

       List aa =  intList.stream()
                .collect(collectingAndThen(partitioningBy(s -> s > 6),
                        items -> Stream.of(items.get(true), items.get(false))
                                .flatMap(Collection::stream)
                                .collect(toList())));


        System.out.println("came here" + aa);


    }
}