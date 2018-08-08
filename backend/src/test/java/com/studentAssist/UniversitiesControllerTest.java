package com.studentAssist;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.studentAssist.controllers.UniversitiesController;
import com.studentAssist.interceptor.ExecuteInterceptor;
import com.studentAssist.services.LoggingService;
import com.studentAssist.services.UniversitiesService;
import com.studentAssist.util.FBGraph;

@RunWith(SpringRunner.class)
@WebMvcTest(UniversitiesController.class)
public class UniversitiesControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	UniversitiesService service;

	@MockBean
	LoggingService loggingService;

	@MockBean
	FBGraph fbGraph;

	@MockBean
	ExecuteInterceptor executeInterceptor;

	@Test
	public void testAllUniversityNames() throws Exception {

		mvc.perform(get("/getAllUniversitiesList")).andExpect(status().isOk());
		
//        .andDo(print())
//				.andExpect(content().string("SUNY"));

	}

}
