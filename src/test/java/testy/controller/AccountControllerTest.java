package testy.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import testy.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@Transactional
@IntegrationTest
public class AccountControllerTest {

	private MockMvc mockMvc;

	@Autowired
    private FilterChainProxy filterChainProxy;
	
	@Autowired
	private WebApplicationContext webAppContext;

	@Autowired
	private Environment env;
	
	private MockHttpSession session;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).dispatchOptions(true).addFilters(filterChainProxy).build();
        session = (MockHttpSession) mockMvc.perform(post("/login")
    			.param("username", env.getProperty("ldap.loginTester"))
    			.param("password", env.getProperty("ldap.loginTesterPw")))
    			.andExpect(status().isOk()).andReturn().getRequest().getSession();
    }
	
	@Test
	public void GET_accountsMe_shouldReturnTheCorrectAccountObject() throws Exception {
		mockMvc.perform(get("/accounts/me").session(session)).andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.accountName", is("aroth")));
	}
	
	@Test
	public void GET_base_shouldReturnExpectedString() throws Exception {
		final String expectedString = "Backend is running!";
		mockMvc.perform(get("/").session(session)).andExpect(status().isOk()).andExpect(content().string(expectedString));
	}

}
