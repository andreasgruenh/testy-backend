package testy.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import testy.domain.question.AbstractQuestion;
import testy.domain.question.mc.MCPossibility;
import testy.domain.question.mc.MCQuestion;
import testy.domain.test.Category;
import testy.helper.annotations.NeedsSessions;
import testy.helper.annotations.NeedsTestClasses;

import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class CategoryControllerTest extends ControllerTest {

	@NeedsTestClasses
	@NeedsSessions
	@Test
	public void GET_id_shouldReturnCorrectCategoryWithCorrectProperties() throws Exception {
		// act
		mockMvc.perform(get("/categories/" + testClasses.category1.getId()).session(userSession))
			.andExpect(status().isOk())

			// assert
			.andExpect(jsonPath("$.id", is((int) testClasses.category1.getId())))
			.andExpect(jsonPath("$.name", is(testClasses.category1.getName())))
			.andExpect(jsonPath("$.maxScore", is(testClasses.category1.getMaxScore())))
			.andExpect(jsonPath("$.questions").doesNotExist());
	}

	@NeedsTestClasses
	@NeedsSessions
	@Test
	public void GET_id_questions_withoutAdminPermissions_shouldReturn403() throws Exception {
		// act + assert
		mockMvc.perform(
			get("/categories/" + testClasses.category1.getId() + "/questions").session(userSession))
			.andExpect(status().isForbidden());
	}

	@NeedsTestClasses
	@NeedsSessions
	@Test
	public void GET_id_questions_withAdminPermissions_shouldReturnQuestions() throws Exception {
		// act
		mockMvc
			.perform(get("/categories/" + testClasses.category1.getId() + "/questions").session(adminSession))
			.andExpect(status().isOk())

			// assert
			.andExpect(jsonPath("$", iterableWithSize(testClasses.category1.getQuestions().size())));
	}
	
	@NeedsTestClasses
	@NeedsSessions
	@Test
	public void POST_id_questions_withoutAdminPermissions_shouldFAIL() throws Exception {
		
		// arrange
		AbstractQuestion question = new MCQuestion();
		question.setQuestionString("What the fuck is wrong with you?");
		
		// act
		mockMvc
			.perform(post("/categories/" + testClasses.category1.getId() + "/questions").session(userSession)
			.contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(question)))
			.andExpect(status().isForbidden());
	}
	
	@NeedsTestClasses
	@NeedsSessions
	@Test
	public void POST_id_questions_withAdminPermissions_shouldReturnCreatedCategory() throws Exception {
		
		// arrange
		MCQuestion question = new MCQuestion();
		question.setQuestionString("What the fuck is wrong with you?");
		MCPossibility possibility1 = new MCPossibility("Größer", false);
		MCPossibility possibility2 = new MCPossibility("kleiner", true);
		question.addAnswer(possibility1);
		question.addAnswer(possibility2);
		mapper.setSerializationInclusion(Include.NON_NULL);
		
		MockHttpServletResponse response = mockMvc
			.perform(post("/categories/" + testClasses.category1.getId() + "/questions").session(adminSession)
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(question)))
			.andExpect(status().isCreated()).andReturn().getResponse();

		// assert
		AbstractQuestion actualQuestion = mapper.readValue(response.getContentAsString(), AbstractQuestion.class);
		assertTrue("Question string should be returned",
			actualQuestion.getQuestionString().equals(question.getQuestionString()));
		assertTrue("Id should be set", actualQuestion.getId() != 0);
		assertTrue("Category should be set correctly",
			actualQuestion.getCategory().getId() == testClasses.category1.getId());
	}

	@NeedsTestClasses
	@NeedsSessions
	@Test
	public void PATCH_id_withoutAdminPermissions_shouldReturn403() throws Exception {
		// arrange
		Category changedCat = new Category("Changed Category");
		changedCat.setMaxScore(40);

		// act
		mockMvc.perform(
			patch("/categories/" + testClasses.category1.getId()).session(userSession)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(changedCat))).andExpect(
			status().isForbidden());
	}

	@NeedsTestClasses
	@NeedsSessions
	@Test
	public void PATH_id_withAdminPermissions_shouldReturnChangedCategory() throws Exception {
		// arrange
		Category changedCat = new Category("Changed Category");
		changedCat.setMaxScore(40);

		// act
		mockMvc.perform(
			patch("/categories/" + testClasses.category1.getId()).session(adminSession)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(changedCat))).andExpect(status().isOk())

			// assert
			.andExpect(jsonPath("$.name", is(equalTo(changedCat.getName()))))
			.andExpect(jsonPath("$.maxScore", is(equalTo(changedCat.getMaxScore()))));
	}
}
