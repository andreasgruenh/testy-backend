package testy.domain;

import java.io.IOException;

import org.junit.Test;

import testy.domain.question.AbstractAnswer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AbstractAnswerDeserializationtest {

	@Test
	public void test() throws JsonParseException, JsonMappingException, IOException {
		// arrange
		String jsonString = "[{\"type\":\"MCAnswer\",\"id\":0,\"question\":{\"type\":\"MCQuestion\", \"id\":2},\"checkedPossibilities\":[{\"id\":4,\"text\":\"A1\"}],\"uncheckedPossibilities\":[{\"id\":3,\"text\":\"A2\"}]}]";
		ObjectMapper mapper = new ObjectMapper();
		
		// act
		mapper.readValue(jsonString, AbstractAnswer[].class);
	}

}
