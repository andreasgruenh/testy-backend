
package testy.controller;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import testy.domain.question.mc.MCAnswer;
import testy.domain.question.mc.MCPossibility;
import testy.domain.question.mc.MCQuestion;
import testy.domain.test.Category;
import testy.helper.annotations.NeedsSessions;
import testy.helper.annotations.NeedsTestClasses;

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
	public void GET_poolsId_withPermissions_shouldReturnCorrectPool() throws Exception {

		// act
		mockMvc.perform(
			get("/pools/" + testClasses.questionPool1.getId()).session(adminSession))
			.andExpect(status().isOk())

		// assert
			.andExpect(jsonPath("$.id", is(equalTo((int) testClasses.questionPool1.getId()))))
			.andExpect(jsonPath("$.name", is(equalTo(testClasses.questionPool1.getName()))))
			.andExpect(jsonPath("$.categories").doesNotExist())
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
		System.out.println(mockMvc.perform(get("/pools/" + testClasses.questionPool1.getId() + "/test").session(userSession))
		
		// assert
		.andExpect(jsonPath("$[0].questions[0].correct").doesNotExist()).andReturn().getResponse().getContentAsString());
	}
	
	@NeedsSessions
	@NeedsTestClasses
	@Test
	public void POST_poolsIdTest_shouldReturnTestScore() throws Exception {

		String answerString = "[{\"type\":\"MCAnswer\",\"id\":0,\"question\":{\"id\":2},\"checkedPossibilities\":[{\"id\":4,\"text\":\"A1\"}],\"uncheckedPossibilities\":[{\"id\":3,\"text\":\"A2\"}]}]";
		System.out.println(answerString);
		
		
		// act
		System.out.println(mockMvc.perform(post("/pools/" + testClasses.questionPool1.getId() + "/test").session(userSession)
			.contentType(MediaType.APPLICATION_JSON)
			.content(answerString))
		
		// assert
		.andReturn().getResponse().getContentAsString());
	}
	
}
