package testy.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;

import testy.domain.test.Category;
import testy.helper.annotations.NeedsSessions;
import testy.helper.annotations.NeedsTestClasses;

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
	public void PUT_id_withoutAdminPermissions_shouldReturn403() throws Exception {
		// arrange
		Category changedCat = new Category("Changed Category");
		changedCat.setMaxScore(40);

		// act
		mockMvc.perform(
			put("/categories/" + testClasses.category1.getId()).session(userSession)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(changedCat))).andExpect(
			status().isForbidden());
	}

	@NeedsTestClasses
	@NeedsSessions
	@Test
	public void PUT_id_withAdminPermissions_shouldReturnChangedCategory() throws Exception {
		// arrange
		Category changedCat = new Category("Changed Category");
		changedCat.setMaxScore(40);

		// act
		mockMvc.perform(
			put("/categories/" + testClasses.category1.getId()).session(adminSession)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(changedCat))).andExpect(status().isOk())

			// assert
			.andExpect(jsonPath("$.name", is(equalTo(changedCat.getName()))))
			.andExpect(jsonPath("$.maxScore", is(equalTo(changedCat.getMaxScore()))));
	}
}
