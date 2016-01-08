package testy.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import testy.dataaccess.AccountRepository;
import testy.domain.Account;
import testy.domain.util.AccountBuilder;
import testy.helper.JsonIterableChecker;
import testy.helper.annotations.NeedsSessions;
import testy.helper.annotations.NeedsTestClasses;

public class AccountControllerTest extends ControllerTest {

	@Autowired
	private AccountRepository accountRepo;

	@NeedsTestClasses
	@NeedsSessions
	@Test
	public void GET_accountsMe_shouldReturnTheCorrectAccountObjectWithCorrectProperties()
		throws Exception {

		// act
		mockMvc.perform(get("/accounts/me").session(userSession))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json;charset=UTF-8"))

			// assert
			.andExpect(jsonPath("$.accountName", is(equalTo(testClasses.user.getAccountName()))))
			.andExpect(jsonPath("$.id", is(equalTo((int) testClasses.user.getId()))))
			.andExpect(jsonPath("$.admin", is(equalTo(testClasses.user.isAdmin()))))
			.andExpect(jsonPath("$.firstname", is(equalTo(testClasses.user.getFirstname()))))
			.andExpect(jsonPath("$.lastname", is(equalTo(testClasses.user.getLastname()))))
			.andExpect(jsonPath("$.email", is(equalTo(testClasses.user.getEmail()))))
			.andExpect(jsonPath("$.testResults").doesNotExist());
	}

	@NeedsSessions
	@Test
	public void GET_base_shouldReturnExpectedString() throws Exception {
		// arrange
		final String expectedString = "Backend is running!";

		// act
		mockMvc.perform(get("/").session(userSession)).andExpect(status().isOk())

			// assert
			.andExpect(content().string(expectedString));
	}

	@NeedsTestClasses
	@NeedsSessions
	@Test
	public void POST_accountsMe_shouldReturnUpdatedAccount() throws Exception {

		// arrange
		Account alteredAccount = testClasses.user;
		alteredAccount.setAdmin(true);
		String jsonAccount = mapper.writeValueAsString(alteredAccount);
		Account expectedAccount = alteredAccount;
		expectedAccount.setAdmin(false);

		// act
		mockMvc.perform(post("/accounts/me").session(userSession)
			.contentType(MediaType.APPLICATION_JSON).content(jsonAccount))
			.andExpect(status().isOk())

			// assert
			.andExpect(jsonPath("$.firstname", is(equalTo(expectedAccount.getFirstname()))))
			.andExpect(jsonPath("$.lastname", is(equalTo(expectedAccount.getLastname()))))
			.andExpect(jsonPath("$.email", is(equalTo(expectedAccount.getEmail()))))
			.andExpect(jsonPath("$.admin", is(equalTo(expectedAccount.isAdmin()))));
	}

	@NeedsTestClasses
	@NeedsSessions
	@Test
	public void POST_accountsMe_withWrongAccount_shouldReturn403()
		throws Exception {

		// arrange
		Account account = AccountBuilder.startWith("broth").build();
		String jsonAccount = mapper.writeValueAsString(account);

		// act + assert
		mockMvc.perform(post("/accounts/me").session(userSession)
			.contentType(MediaType.APPLICATION_JSON).content(jsonAccount))
			.andExpect(status().isForbidden());
	}

	@NeedsSessions
	@Test
	public void GET_accounts_withAdminPermissions_shouldReturnASetWithTheCorrectObjects()
		throws Exception {

		// act
		String response = mockMvc.perform(get("/accounts/").session(adminSession))
			.andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString();

		// assert
		assertTrue("Response should be collection of accounts but was: " + response,
			JsonIterableChecker.representsIterableOfClass(response, Account.class));
	}

	@NeedsSessions
	@Test
	public void GET_accounts_withoutAdminPermissions_shouldReturn403()
		throws Exception {

		// act + assert
		mockMvc.perform(get("/accounts/").session(userSession)).andExpect(status().isForbidden());
	}

	@NeedsSessions
	@Test
	public void PUT_idIsAdmin_withoutAdminPermissions_shouldReturn403()
		throws Exception {

		// act + assert
		mockMvc.perform(put("/accounts/" + testClasses.user.getId() + "/isAdmin").session(userSession)
			.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(false)))
			.andExpect(status().isForbidden());
	}

	@NeedsTestClasses
	@NeedsSessions
	@Test
	public void PUT_idIsAdmin_withAdminPermissions_shouldUpdateAccount()
		throws Exception {

		// act
		mockMvc.perform(put("/accounts/" + testClasses.user.getId() + "/isAdmin").session(adminSession)
			.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(true)))
			.andExpect(status().isOk())

			// assert
			.andExpect(jsonPath("$.admin", is(true)));
	}
}
