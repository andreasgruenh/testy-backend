package testy.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import testy.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MainControllerTest {

    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webAppContext;
    
    @Autowired
    private Environment env;
    
    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }
	
	@Test
	public void GET_base_shouldReturnExpectedString() throws Exception {
		final String expectedString = "Backend is running!";
		mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(content().string(expectedString));
	}
	
	@Test
	public void GET_profile_shouldReturnExpectedString() throws Exception {
		final String expectedString = env.getProperty("spring.profiles.active");
		mockMvc.perform(get("/profile")).andExpect(status().isOk()).andExpect(content().string(expectedString));
	}
	
}
