package testy.controller;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import testy.Application;
import testy.dataaccess.AccountRepository;
import testy.domain.Account;
import testy.domain.util.AccountBuilder;
import testy.helper.SessionEstablisher;

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
	
	@Autowired
	private SessionEstablisher sessionEstablisher;

	private MockHttpSession userSession;
	private MockHttpSession adminSession;

	@Autowired
	private AccountRepository accountRepo;

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).dispatchOptions(true)
		        .addFilters(filterChainProxy).build();
		userSession = sessionEstablisher.getUserSessionWith(mockMvc);

		adminSession = sessionEstablisher.getAdminSessionWith(mockMvc);
	}

	@Test
	public void GET_accountsMe_shouldReturnTheCorrectAccountObject() throws Exception {
		mockMvc.perform(get("/accounts/me").session(userSession)).andExpect(status().isOk())
		        .andExpect(content().contentType("application/json;charset=UTF-8"))
		        .andExpect(jsonPath("$.accountName", is(env.getProperty("ldap.loginTester"))));
	}

	@Test
	public void GET_base_shouldReturnExpectedString() throws Exception {
		final String expectedString = "Backend is running!";
		mockMvc.perform(get("/").session(userSession)).andExpect(status().isOk())
		        .andExpect(content().string(expectedString));
	}

	@Test
	public void POST_accountsMe_shouldUpdateOwnAccount() throws Exception {

		ObjectMapper mapper = new ObjectMapper();

		final String firstname = "Andreas", lastname = "Roth", email = "Andreas.Roth@paul-consultants.de";

		String jsonAccount = mockMvc.perform(get("/accounts/me").session(userSession)).andReturn()
		        .getResponse().getContentAsString();
		Account account = mapper.readValue(jsonAccount, Account.class);

		final boolean isAdmin = account.isAdmin();

		account = AccountBuilder.startWithExisting(account).withEmail(email)
		        .withFirstname(firstname).withLastname(lastname).isAdmin(!isAdmin).build();

		jsonAccount = mapper.writeValueAsString(account);
		mockMvc.perform(
		        post("/accounts/me").session(userSession).contentType(MediaType.APPLICATION_JSON)
		                .content(jsonAccount)).andExpect(status().isOk());

		String newJsonAccount = mockMvc.perform(get("/accounts/me").session(userSession))
		        .andReturn().getResponse().getContentAsString();
		Account newAccount = mapper.readValue(newJsonAccount, Account.class);

		assertTrue("Firstname should be postet firstname",
		        newAccount.getFirstname().equals(firstname));
		assertTrue("Lastname should be posted lastname", newAccount.getLastname().equals(lastname));
		assertTrue("E-Mail should be the posted email", newAccount.getEmail().equals(email));
		assertTrue("Roles must not be changed with this endpoint!", newAccount.isAdmin() == isAdmin);
	}

	@Test
	public void POST_accountsMe_withWrongAccount_shouldReturn403() throws Exception {

		ObjectMapper mapper = new ObjectMapper();

		final String firstname = "Andreas", lastname = "Roth", email = "Andreas.Roth@paul-consultants.de";

		Account account = AccountBuilder.startWith("broth").withEmail(email)
		        .withFirstname(firstname).withLastname(lastname).isAdmin(true).build();

		String jsonAccount = mapper.writeValueAsString(account);
		mockMvc.perform(
		        post("/accounts/me").session(userSession).contentType(MediaType.APPLICATION_JSON)
		                .content(jsonAccount)).andExpect(status().isForbidden());
	}

	@Test
	public void GET_accounts__withAdminPermissions_shouldReturnASetWithTheCorrectObject() throws Exception {

		accountRepo.save(new Account("toni"));
		accountRepo.save(new Account("tom"));
		accountRepo.save(new Account("marcel"));

		ObjectMapper mapper = new ObjectMapper();
		MockHttpServletResponse response = mockMvc.perform(get("/accounts/").session(adminSession))
		        .andExpect(status().isOk()).andReturn().getResponse();
		Account[] accounts = mapper.readValue(response.getContentAsString(), Account[].class);
		assertTrue("Response should contain exactly 5 object", accounts.length == 5);
	}
	
	@Test
	public void GET_accounts__withoutAdminPermissions_shouldReturn403() throws Exception {

		accountRepo.save(new Account("toni"));
		accountRepo.save(new Account("tom"));
		accountRepo.save(new Account("marcel"));
		mockMvc.perform(get("/accounts/").session(userSession))
		        .andExpect(status().isForbidden());
	}

	@Test
	public void PUT_idIsAdmin_withoutAdminPermissions_shouldReturn403() throws Exception {

		ObjectMapper mapper = new ObjectMapper();

		String accountString = mockMvc.perform(get("/accounts/me").session(userSession))
		        .andExpect(status().isOk())
		        .andExpect(content().contentType("application/json;charset=UTF-8")).andReturn()
		        .getResponse().getContentAsString();

		Account account = mapper.readValue(accountString, Account.class);

		mockMvc.perform(
		        put("/accounts/" + account.getId() + "/isAdmin").session(userSession)
		                .contentType(MediaType.APPLICATION_JSON)
		                .content(mapper.writeValueAsString(false))).andExpect(
		        status().isForbidden());
	}

	@Test
	public void PUT_idIsAdmin_withAdminPermissions_shouldUpdateAccount() throws Exception {

		ObjectMapper mapper = new ObjectMapper();

		String accountString = mockMvc.perform(get("/accounts/me").session(userSession))
		        .andExpect(status().isOk())
		        .andExpect(content().contentType("application/json;charset=UTF-8")).andReturn()
		        .getResponse().getContentAsString();

		Account userAccount = mapper.readValue(accountString, Account.class);

		mockMvc.perform(
		        put("/accounts/" + userAccount.getId() + "/isAdmin").session(adminSession)
		                .contentType(MediaType.APPLICATION_JSON)
		                .content(mapper.writeValueAsString(true))).andExpect(status().isOk());

		mockMvc.perform(get("/accounts/me").session(userSession)).andExpect(status().isOk())
		        .andExpect(content().contentType("application/json;charset=UTF-8"))
		        .andExpect(jsonPath("$.admin", is(true)));
	}
}
