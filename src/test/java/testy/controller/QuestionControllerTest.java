package testy.controller;

import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;

import testy.domain.question.mc.MCPossibility;
import testy.domain.question.mc.MCQuestion;
import testy.domain.test.Category;
import testy.helper.annotations.NeedsSessions;
import testy.helper.annotations.NeedsTestClasses;

public class QuestionControllerTest extends ControllerTest {

	@NeedsTestClasses
	@NeedsSessions
	@Test
	public void GET_id_withoutAdminPermissions_shouldFail() throws Exception {
		// act
		mockMvc.perform(get("/questions/" + testClasses.question1.getId()).session(userSession))
			.andExpect(status().isForbidden());
	}
	
	@NeedsTestClasses
	@NeedsSessions
	@Test
	public void GET_id_withAdminPermissions_shouldReturnCorrectFieldsForMCQuestion() throws Exception {
		// act
		mockMvc.perform(get("/questions/" + testClasses.question1.getId()).session(adminSession))
			.andExpect(status().isOk())
			
		// assert
			
		.andExpect(jsonPath("$.id", is(equalTo((int) testClasses.question1.getId()))))
		.andExpect(jsonPath("$.questionString", is(any(String.class))))
		.andExpect(jsonPath("$.possibleAnswers[0].text", is(any(String.class))))
		.andExpect(jsonPath("$.possibleAnswers[0].correct", is(any(Boolean.class))))
		.andExpect(jsonPath("$.category.id", is(equalTo((int) testClasses.question1.getCategory().getId()))));
	}
	
	@NeedsTestClasses
	@NeedsSessions
	@Test
	public void DELETE_id_withoutAdminPermissions_shouldFail() throws Exception {
		// act
		mockMvc.perform(delete("/questions/" + testClasses.question1.getId()).session(userSession))
			.andExpect(status().isForbidden());
	}
	
	@NeedsTestClasses
	@NeedsSessions
	@Test
	public void DELETE_id_withAdminPermissions_shouldDeleteQuestion() throws Exception {
		// act
		mockMvc.perform(delete("/questions/" + testClasses.question1.getId()).session(adminSession))
			.andExpect(status().isOk());
		
		// assert
		mockMvc.perform(get("/categories/" + testClasses.category1.getId() + "/questions").session(adminSession))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.*", hasSize(1)));
	}
	
	@NeedsTestClasses
	@NeedsSessions
	@Test
	public void PATCH_id_withoutAdminPermissions_shouldFail() throws Exception {
		// arrange
		MCQuestion newQuestion = new MCQuestion();
		newQuestion.setCategory(new Category());
		newQuestion.setQuestionString("NEW STRING");
		newQuestion.addAnswer(new MCPossibility("Antwort", false));
		
		// act
		mockMvc.perform(patch("/questions/" + testClasses.question1.getId()).session(userSession)
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(testClasses.question1)))
			.andExpect(status().isForbidden());
	}
	
	@NeedsTestClasses
	@NeedsSessions
	@Test
	public void PATCH_id_withAdminPermissions_shouldReturnUpdatedQuestion() throws Exception {
		// arrange
		MCQuestion newQuestion = new MCQuestion();
		newQuestion.setCategory(new Category());
		newQuestion.setQuestionString("NEW STRING");
		MCPossibility newPossibility = new MCPossibility("Antwort", true);
		newQuestion.addAnswer(newPossibility);
		
		// act
		MockHttpServletResponse response = mockMvc.perform(patch("/questions/" + testClasses.question1.getId())
			.session(adminSession)
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(newQuestion)))
			.andExpect(status().isOk()).andReturn().getResponse();
		MCQuestion updatedQuestion = mapper.readValue(response.getContentAsString(), MCQuestion.class);
		
		// assert
		assertTrue("Category should not have been changed",
			updatedQuestion.getCategory().getId() == testClasses.category1.getId());
		assertTrue("Questionstring should have been updated, was: " + updatedQuestion.getQuestionString() + 
			" but expected 'NEW STRING'",
			updatedQuestion.getQuestionString().equals("NEW STRING"));
		assertTrue("New Question should have been added",
			updatedQuestion.getPossibleAnswers().size() == 1);
	}
}
