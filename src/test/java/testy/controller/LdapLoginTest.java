package testy.controller;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import testy.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class LdapLoginTest {

	private MockMvc mockMvc;

    @Autowired
    private FilterChainProxy filterChainProxy;
    
    @Autowired
    private Environment env;
    
    @Autowired
    private WebApplicationContext webAppContext;
    
    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).dispatchOptions(true).addFilters(filterChainProxy).build();
    }
    
    @Test
    public void Request_Controller_withoutLogin_returns401() throws Exception {
    	mockMvc.perform(get("/profile")).andExpect(status().is(401));
    	mockMvc.perform(post("/profile")).andExpect(status().is(401));
    	mockMvc.perform(delete("/")).andExpect(status().is(401));
    	mockMvc.perform(put("/NotExistingEndPoint")).andExpect(status().is(401));
    }
    
    @Test
    public void POST_login_withWrongCredentials_returns302() throws Exception {
    	String redirect = mockMvc.perform(post("/login")
    			.param("username", "aroth")
    			.param("password", "123"))
    			.andExpect(status().is(302)).andReturn().getResponse().getRedirectedUrl();
    	assertTrue(redirect.equals("/login?error"));
    }
    
    @Test
    public void POST_login_withCorrectCredentials_returns200() throws Exception {
    	mockMvc.perform(post("/login")
    			.param("username", "aroth")
    			.param("password", "1malStud!um"))
    			.andExpect(status().is(200));
    }
    
    @Test
    public void GET_controller_withLogin_shouldReturnOkCode() throws Exception {
    	MvcResult result = mockMvc.perform(post("/login")
    			.param("username", env.getProperty("ldap.loginTester"))
    			.param("password", env.getProperty("ldap.loginTesterPw")))
    			.andExpect(status().isOk()).andReturn();
    	MockHttpSession session = (MockHttpSession)result.getRequest().getSession();
    	mockMvc.perform(get("/profile").session(session)).andExpect(status().isOk());
    	mockMvc.perform(get("/").session(session)).andExpect(status().isOk());
    }
    
    @Test
    public void GET_controller_afterLogout_shouldReturn401() throws Exception {
    	MvcResult result = mockMvc.perform(post("/login")
    			.param("username", env.getProperty("ldap.loginTester"))
    			.param("password", env.getProperty("ldap.loginTesterPw")))
    			.andExpect(status().isOk()).andReturn();
    	MockHttpSession session = (MockHttpSession)result.getRequest().getSession();
    	mockMvc.perform(get("/profile").session(session)).andExpect(content().string("test"));
    	mockMvc.perform(get("/logout").session(session)).andExpect(status().isOk());
    	mockMvc.perform(get("/profile").session(session)).andExpect(status().isUnauthorized());
    }	
}
