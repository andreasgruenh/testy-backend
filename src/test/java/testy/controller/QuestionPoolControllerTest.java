
package testy.controller;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import testy.domain.test.Category;
import testy.domain.test.QuestionPool;
import testy.domain.test.TestResult;
import testy.helper.annotations.NeedsSessions;
import testy.helper.annotations.NeedsTestClasses;

import com.fasterxml.jackson.core.JsonProcessingException;

public class QuestionPoolControllerTest extends ControllerTest {

	@NeedsSessions
	@NeedsTestClasses
	@Test
	public void GET_poolsId_withoutPermissionsShouldReturn403() throws Exception {

		// act + assert
		mockMvc.perform(get("/pools/" + testClasses.questionPool1.getId()).session(userSession)).andExpect(
			status().isForbidden());
	}
	
	@NeedsSessions
	@NeedsTestClasses
	@Test
	public void PATCH_poolsId_withoutPermissionsShouldReturn403() throws JsonProcessingException, Exception {
		
		// act
		mockMvc.perform(
			patch("/pools/" + testClasses.questionPool1.getId()).session(userSession)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(testClasses.questionPool1)))

			// assert
			.andExpect(status().isForbidden());
		
	}
	
	@NeedsSessions
	@NeedsTestClasses
	@Test
	public void PATCH_poolsId_withPermissionsShouldReturnUpdatedPool() throws Exception {
		QuestionPool newPool = testClasses.questionPool1;
		newPool.setPercentageToPass(100);
		newPool.setName("geändert");

		// act
		MockHttpServletResponse response = mockMvc
			.perform(
				patch("/pools/" + testClasses.questionPool1.getId()).session(adminSession)
					.contentType(MediaType.APPLICATION_JSON)
					.content(mapper.writeValueAsString(newPool)))
			.andExpect(status().isOk())
			.andReturn().getResponse();
		QuestionPool actualPool = mapper.readValue(response.getContentAsString(), QuestionPool.class);

		// assert
		assertTrue("Name should have been updated. Expected: " + newPool.getName() +
			"But was " + actualPool.getName(),
			actualPool.getName().equals(newPool.getName()));
		assertTrue("Description should have been updated. Expected: " + newPool.getDescription() +
			"But was " + actualPool.getDescription(),
			actualPool.getDescription().equals(newPool.getDescription()));
		assertTrue("Percentage to pass should have been updated. Expected: " + newPool.getPercentageToPass() +
			"But was " + actualPool.getPercentageToPass(),
			actualPool.getPercentageToPass() == newPool.getPercentageToPass());
	}
	
	@NeedsSessions
	@NeedsTestClasses
	@Test
	public void DELETE_poolsId_withoutPermissionsShouldFail() throws Exception {
		
		// act
		mockMvc.perform(
			delete("/pools/" + testClasses.questionPool1.getId()).session(userSession))
		.andExpect(status().isForbidden());
		
		// assert
		mockMvc
		.perform(get("/subjects/" + testClasses.subject1.getId() + "/pools").session(userSession))
		.andExpect(status().isOk()).andExpect(jsonPath("$.*", hasSize(1)));
	}
	
	@NeedsSessions
	@NeedsTestClasses
	@Test
	public void DELETE_poolsId_withPermissionsShouldDeletePool() throws Exception {
		// act
		mockMvc.perform(
			delete("/pools/" + testClasses.questionPool1.getId()).session(adminSession))
		.andExpect(status().isOk());
		
		// assert
		mockMvc
		.perform(get("/subjects/" + testClasses.subject1.getId() + "/pools").session(userSession))
		.andExpect(status().isOk()).andExpect(jsonPath("$.*", hasSize(0)));
	}

	@NeedsSessions
	@NeedsTestClasses
	@Test
	public void GET_poolsId_withPermissions_shouldReturnCorrectPool() throws Exception {

		// act
		mockMvc.perform(
			get("/pools/" + testClasses.questionPool1.getId()).session(adminSession))
			.andExpect(status().isOk())

		// assert
			.andExpect(jsonPath("$.id", is(equalTo((int) testClasses.questionPool1.getId()))))
			.andExpect(jsonPath("$.name", is(equalTo(testClasses.questionPool1.getName()))))
			.andExpect(jsonPath("$.description", is(equalTo(testClasses.questionPool1.getDescription()))))
			.andExpect(jsonPath("$.categories").doesNotExist())
			.andExpect(jsonPath("$.results").exists())
			.andExpect(
				jsonPath("$.percentageToPass", is(equalTo(testClasses.questionPool1.getPercentageToPass()))))
			.andExpect(
				jsonPath("$.subject.id", is(equalTo((int) testClasses.questionPool1.getSubject().getId()))));
	}

	@NeedsSessions
	@NeedsTestClasses
	@Test
	public void GET_poolsIdCategories_withoutPermissions_shouldReturn403() throws Exception {

		// act
		mockMvc.perform(
			get("/pools/" + testClasses.questionPool1.getId() + "/categories").session(userSession))
			.andExpect(status().isForbidden());
	}
	
	@NeedsSessions
	@NeedsTestClasses
	@Test
	public void GET_poolsIdCategories_withPermissions_shouldReturnCollectionOfCategories() throws Exception {

		// act
		mockMvc.perform(
			get("/pools/" + testClasses.questionPool1.getId() + "/categories").session(adminSession))
			.andExpect(status().isOk())
			
			// assert
			.andExpect(jsonPath("$[0].id", is(any(Integer.class))))
			.andExpect(jsonPath("$[0].name", is(any(String.class))))
			.andExpect(jsonPath("$[0].maxScore", is(any(Integer.class))))
			.andExpect(jsonPath("$[0].questions").doesNotExist());
	}
	
	@NeedsSessions
	@NeedsTestClasses
	@Test
	public void POST_poolsIdCategories_withoutAdminPermissions_shouldReturn403() throws Exception {

		// arrange
		Category newCat = new Category("New category");
		newCat.setMaxScore(50);

		// act+assert
		mockMvc
			.perform(post("/pools/" + testClasses.questionPool1.getId() + "/categories").session(userSession)
			.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(newCat)))
			.andExpect(status().isForbidden());
	}

	@NeedsSessions
	@NeedsTestClasses
	@Test
	public void POST_poolsIdCategories_withAdminPermissions_shouldReturnCreatedCategory()
		throws Exception {

		// arrange
		Category newCat = new Category("New category");
		newCat.setMaxScore(50);

		// act
		MockHttpServletResponse response = mockMvc
			.perform(post("/pools/" + testClasses.questionPool1.getId() + "/categories").session(adminSession)
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(newCat)))
			.andExpect(status().isCreated()).andReturn().getResponse();

		// assert
		Category actualCategory = mapper.readValue(response.getContentAsString(), Category.class);
		assertTrue("Name of category should be returned",
			actualCategory.getName().equals(newCat.getName()));
		assertTrue("Max Score of category should be returned",
			actualCategory.getMaxScore() == newCat.getMaxScore());
	}
	
	@NeedsSessions
	@NeedsTestClasses
	@Test
	public void GET_poolsIdTest_shouldReturnCategoriesWithQuestions() throws Exception {

		// act
		mockMvc.perform(get("/pools/" + testClasses.questionPool1.getId() + "/test").session(userSession))
		
		// assert
		.andExpect(jsonPath("$[0].questions[0].possibleAnswers[0].correct").doesNotExist());
	}
	
	@NeedsSessions
	@NeedsTestClasses
	@Test
	public void POST_poolsIdTest_shouldReturnTestScore() throws Exception {

		// arrange
		String answerString = "[{\"type\":\"MCAnswer\",\"id\":0,\"question\":{\"type\":\"MCQuestion\", \"id\":2},\"checkedPossibilities\":[{\"id\":4,\"text\":\"A1\"}],\"uncheckedPossibilities\":[{\"id\":3,\"text\":\"A2\"}]}]";
		int expectedScore = 5;
		
		// act
		MockHttpServletResponse response = mockMvc
				.perform(post("/pools/" + testClasses.questionPool1.getId() + "/test").session(userSession)
				.contentType(MediaType.APPLICATION_JSON)
				.content(answerString))
				.andReturn().getResponse();
		TestResult result = mapper.readValue(response.getContentAsString(), TestResult.class);
		
		// assert
		assertTrue("Testscroe should be " + expectedScore + " but was " + result.getScore(),
			result.getScore() == expectedScore);
		assertTrue("User should be set correctly, was: " + result.getUser(),
			result.getUser().getId() == testClasses.user.getId());
		assertTrue("User should have reference to Result, size of results was: " + result.getUser().getTestResults().size(),
			testClasses.user.getTestResults().size() == 1);
		assertTrue("Pool should be set " + result.getQuestionPool(),
			result.getQuestionPool().getId() == testClasses.questionPool1.getId());
		assertTrue("Pool should have reference to Result, size of results was: " + result.getQuestionPool().getResults().size(),
			testClasses.questionPool1.getResults().size() == 1);
		
	}
	
}
