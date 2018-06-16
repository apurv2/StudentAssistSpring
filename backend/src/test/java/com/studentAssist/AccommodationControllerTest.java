package com.studentAssist;

import com.studentAssist.controllers.AccommodationController;
import com.studentAssist.dao.AccommodationDAO;
import com.studentAssist.entities.AccommodationAdd;
import com.studentAssist.entities.Apartments;
import com.studentAssist.entities.Universities;
import com.studentAssist.entities.Users;
import com.studentAssist.interceptor.ExecuteInterceptor;
import com.studentAssist.services.AccommodationService;
import com.studentAssist.services.LoggingService;
import com.studentAssist.services.NotificationsService;
import com.studentAssist.services.UserService;
import com.studentAssist.util.FBGraph;
import com.studentAssist.util.InsertApartmentDetails;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static org.mockito.BDDMockito.given;


@RunWith(SpringRunner.class)
public class AccommodationControllerTest {

//    @Autowired
//    private MockMvc mvc;

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

    private AccommodationAdd add;


    @Before
    public void setup() {

        mockMvc = MockMvcBuilders.standaloneSetup(accommodationController)
                .build();


        add = new AccommodationAdd() {{
            setApartment(new Apartments() {{
                setApartmentName("Cooper Chase");
                setId(4);
            }});
            setUser(new Users() {{
                setUserId(10207639158073180L);
                setFirstName("Apurv");
                setLastName("Kamalapuri");
            }});
            setUniversity(new Universities() {{
                setUniversityId(1160);
                setUniversityName("UT Arlington");
                setCity("city");
                setState("TX");
                setZip(32746);

            }});
            setNoOfRooms("1bhk/1bath");
            setCost(123);
            setGender("Male");
            setNotes("notes");
            setAddId(1160);
            setDatePosted(new Date());
//            setPostedTill(1528423455000L);
        }};
    }

    @Test
    public void getAddFromAddId() throws Exception {
        given(this.dao.getAccommodationFromId(1160)).willReturn(add);
//        assertThat(accommodationService.getAccommodationFromId(1160)).isEqualTo(add);
    }
}
