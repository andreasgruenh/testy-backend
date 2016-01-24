package testy.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import testy.helper.annotations.NeedsSessions;

public class MainControllerTest extends ControllerTest {

	@Autowired
	private Environment env;

	@NeedsSessions
	@Test
	public void GET_base_shouldReturnExpectedString() throws Exception {
		final String expectedString = "Backend is running!";
		mockMvc.perform(get("/").session(userSession)).andExpect(status().isOk())
			.andExpect(content().string(expectedString));
	}

	@NeedsSessions
	@Test
	public void GET_profile_shouldReturnExpectedString() throws Exception {
		final String expectedString = env.getProperty("spring.profiles.active");
		mockMvc.perform(get("/profile").session(userSession)).andExpect(status().isOk())
			.andExpect(content().string(expectedString));
	}

}
