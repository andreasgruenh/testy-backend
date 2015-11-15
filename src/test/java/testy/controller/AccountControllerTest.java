package testy.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import testy.Application;
import testy.dataaccess.AccountRepository;
import testy.domain.Account;
import testy.domain.util.AccountBuilder;
import testy.helper.SessionEstablisher;
import testy.helper.TestClasses;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.*;

import static org.junit.Assert.assertTrue;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
	private TestClasses testClasses;
	
	@Autowired
	private SessionEstablisher sessionEstablisher;

	private MockHttpSession userSession;
	private MockHttpSession adminSession;
	
	private ObjectMapper mapper;

	@Autowired
	private AccountRepository accountRepo;

	@Before
	public void setUp() throws Exception {
		testClasses.initWithDb();
		
		mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).dispatchOptions(true)
		        .addFilters(filterChainProxy).build();
		userSession = sessionEstablisher.getUserSessionWith(mockMvc);
		adminSession = sessionEstablisher.getAdminSessionWith(mockMvc);
		
		mapper = new ObjectMapper();
	}

	@Test
	public void GET_accountsMe_shouldReturnTheCorrectAccountObjectWithCorrectProperties() throws Exception {
		
		// act
		ResultActions result = mockMvc.perform(get("/accounts/me").session(userSession)).andExpect(status().isOk())
		        .andExpect(content().contentType("application/json;charset=UTF-8"));
		
		// assert
		result
		.andExpect(jsonPath("$.accountName", is(equalTo(testClasses.user.getAccountName()))))
		.andExpect(jsonPath("$.id", is(equalTo((int) testClasses.user.getId()))))
		.andExpect(jsonPath("$.admin", is(equalTo(testClasses.user.isAdmin()))))
		.andExpect(jsonPath("$.firstname", is(equalTo(testClasses.user.getFirstname()))))
		.andExpect(jsonPath("$.lastname", is(equalTo(testClasses.user.getLastname()))))
		.andExpect(jsonPath("$.email", is(equalTo(testClasses.user.getEmail()))))
		.andExpect(jsonPath("$.testResults", nullValue()));
	}

	@Test
	public void GET_base_shouldReturnExpectedString() throws Exception {
		// arrange
		final String expectedString = "Backend is running!";
		
		// act
		ResultActions result = mockMvc.perform(get("/").session(userSession)).andExpect(status().isOk());
		
		// assert
		result.andExpect(content().string(expectedString));
	}

	@Test
	public void POST_accountsMe_shouldUpdateOwnAccount() throws Exception {

		// arrange
		Account alteredAccount = testClasses.user;
		alteredAccount.setAdmin(true);
		String jsonAccount = mapper.writeValueAsString(alteredAccount);
		Account expectedAccount = alteredAccount;
		expectedAccount.setAdmin(false);
		
		// act	
		mockMvc.perform(
		        post("/accounts/me").session(userSession).contentType(MediaType.APPLICATION_JSON)
		                .content(jsonAccount)).andExpect(status().isOk());

		// assert
		String actualJsonAccount = mockMvc.perform(get("/accounts/me").session(userSession))
		        .andReturn().getResponse().getContentAsString();
		Account actualAccount = mapper.readValue(actualJsonAccount, Account.class);

		assertTrue("Firstname should be posted firstname",
		        actualAccount.getFirstname().equals(expectedAccount.getFirstname()));
		assertTrue("Lastname should be posted lastname", actualAccount.getLastname().equals(expectedAccount.getLastname()));
		assertTrue("E-Mail should be the posted email", actualAccount.getEmail().equals(expectedAccount.getEmail()));
		assertTrue("Roles must not be changed with this endpoint!", actualAccount.isAdmin() == expectedAccount.isAdmin());
	}

	@Test
	public void POST_accountsMe_withWrongAccount_shouldReturn403() throws Exception {

		// arrange
		Account account = AccountBuilder.startWith("broth").build();
		String jsonAccount = mapper.writeValueAsString(account);
		
		// act + assert
		mockMvc.perform(
		        post("/accounts/me").session(userSession).contentType(MediaType.APPLICATION_JSON)
		                .content(jsonAccount)).andExpect(status().isForbidden());
	}

	@Test
	public void GET_accounts_withAdminPermissions_shouldReturnASetWithTheCorrectObject() throws Exception {
		
		// act
		MockHttpServletResponse response = mockMvc.perform(get("/accounts/").session(adminSession))
		        .andExpect(status().isOk()).andReturn().getResponse();
		
		// assert
		Account[] accounts = mapper.readValue(response.getContentAsString(), Account[].class);
		assertTrue("Response should contain exactly 2 object", accounts.length == 2);
	}
	
	@Test
	public void GET_accounts_withoutAdminPermissions_shouldReturn403() throws Exception {

		// act + assert
		mockMvc.perform(get("/accounts/").session(userSession))
		        .andExpect(status().isForbidden());
	}

	@Test
	public void PUT_idIsAdmin_withoutAdminPermissions_shouldReturn403() throws Exception {

		// act + assert
		mockMvc.perform(
		        put("/accounts/" + testClasses.user.getId() + "/isAdmin").session(userSession)
		                .contentType(MediaType.APPLICATION_JSON)
		                .content(mapper.writeValueAsString(false))).andExpect(
		        status().isForbidden());
	}

	@Test
	public void PUT_idIsAdmin_withAdminPermissions_shouldUpdateAccount() throws Exception {

		// act
		mockMvc.perform(
		        put("/accounts/" + testClasses.user.getId() + "/isAdmin").session(adminSession)
		                .contentType(MediaType.APPLICATION_JSON)
		                .content(mapper.writeValueAsString(true))).andExpect(status().isOk());

		// assert
		Account actualAccount = accountRepo.findByAccountName(testClasses.user.getAccountName());
		assertTrue("User should now be admin but wasn't", actualAccount.isAdmin());
	}
}
