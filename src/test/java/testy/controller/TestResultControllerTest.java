
package testy.controller;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import testy.domain.test.TestResult;
import testy.helper.annotations.NeedsSessions;
import testy.helper.annotations.NeedsTestClasses;

public class TestResultControllerTest extends ControllerTest {

	@NeedsSessions
	@NeedsTestClasses
	@Test
	public void DELETE_testResultsId_withWrongPermissions_ShouldFail() throws Exception {
		// act
		mockMvc
				.perform(delete("/test-results/1").session(userSession))
				.andExpect(status().isForbidden());
	}
	
	@NeedsSessions
	@NeedsTestClasses
	@Test
	public void DELETE_testResultsId_withPermissions_shouldDeleteResult() throws Exception {

		// arrange
		String answerString = "[{\"type\":\"MCAnswer\",\"id\":0,\"question\":{\"type\":\"MCQuestion\", \"id\":2},\"checkedPossibilities\":[{\"id\":4,\"text\":\"A1\"}],\"uncheckedPossibilities\":[{\"id\":3,\"text\":\"A2\"}]}]";
		MockHttpServletResponse response = mockMvc
				.perform(post("/pools/" + testClasses.questionPool1.getId() + "/test").session(userSession)
				.contentType(MediaType.APPLICATION_JSON)
				.content(answerString))
				.andReturn().getResponse();
		TestResult result = mapper.readValue(response.getContentAsString(), TestResult.class);
		
		// act
		mockMvc
			.perform(delete("/test-results/" + result.getId()).session(adminSession));
			
		// assert
		assertTrue("Account should no longer contain result", testClasses.user.getTestResults().size() == 0);
		assertTrue("QuestionPool should no longer contain result", testClasses.questionPool1.getResults().size() == 0);
	}
	
}
