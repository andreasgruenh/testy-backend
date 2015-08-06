package testy.controller;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import testy.Application;
import testy.domain.Account;
import testy.domain.util.AccountBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

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
		mockMvc.perform(get("/accounts/me").session(session)).andExpect(status().isOk()).andExpect(content().contentType("application/json;charset=UTF-8"))
			.andExpect(jsonPath("$.accountName", is(env.getProperty("ldap.loginTester"))));
	}
	
	@Test
	public void GET_base_shouldReturnExpectedString() throws Exception {
		final String expectedString = "Backend is running!";
		mockMvc.perform(get("/").session(session)).andExpect(status().isOk()).andExpect(content().string(expectedString));
	}
	
	@Test
	public void POST_accountsMe_shouldUpdateOwnAccount() throws Exception {
		
		ObjectMapper mapper = new ObjectMapper();
		
		final String firstname = "Andreas",
				lastname = "Roth",
				email = "Andreas.Roth@paul-consultants.de";
		
		String jsonAccount = mockMvc.perform(get("/accounts/me").session(session)).andReturn().getResponse().getContentAsString();
		Account account = mapper.readValue(jsonAccount, Account.class);
		
		account = AccountBuilder.startWithExisting(account)
			.withEmail(email)
			.withFirstname(firstname)
			.withLastname(lastname)
			.isAdmin(true)
			.build();
		
		jsonAccount = mapper.writeValueAsString(account);
		mockMvc.perform(post("/accounts/me")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonAccount)).andExpect(status().isOk());
		
		String newJsonAccount = mockMvc.perform(get("/accounts/me").session(session)).andReturn().getResponse().getContentAsString();
		Account newAccount = mapper.readValue(newJsonAccount, Account.class);
		
		assertTrue(newAccount.getFirstname().equals(firstname));
		assertTrue(newAccount.getLastname().equals(lastname));
		assertTrue(newAccount.getEmail().equals(email));
		assertTrue(newAccount.isAdmin());
	}
	
	@Test
	public void POST_accountsMe_withWrongAccount_shouldReturn403() throws Exception {
		
		ObjectMapper mapper = new ObjectMapper();
		
		final String firstname = "Andreas",
				lastname = "Roth",
				email = "Andreas.Roth@paul-consultants.de";
		
		Account account = AccountBuilder.startWith("broth")
			.withEmail(email)
			.withFirstname(firstname)
			.withLastname(lastname)
			.isAdmin(true)
			.build();
		
		String jsonAccount = mapper.writeValueAsString(account);
		mockMvc.perform(post("/accounts/me")
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonAccount)).andExpect(status().isForbidden());
	}

}
