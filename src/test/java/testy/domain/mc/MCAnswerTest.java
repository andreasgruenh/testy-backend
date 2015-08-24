package testy.domain.mc;

import static org.junit.Assert.assertTrue;
import static testy.helper.UnmodifiableChecker.isUnmodifiable;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import testy.domain.question.mc.MCAnswer;
import testy.domain.question.mc.MCPossibility;
import testy.domain.question.mc.MCQuestion;

public class MCAnswerTest {

	MCQuestion quest = new MCQuestion();
	Set<MCPossibility> set = new HashSet<MCPossibility>();
	
	MCAnswer answer = new MCAnswer(quest, set, set);
	
	@Test
	public void getCheckedPossibilities_shouldReturnUnmodifiableSet() {
		assertTrue("Set should be unmodifiable", isUnmodifiable(answer.getCheckedPossibilities()));
	}
	
	@Test
	public void getUnCheckedPossibilities_shouldReturnUnmodifiableSet() {
		assertTrue("Set should be unmodifiable", isUnmodifiable(answer.getUncheckedPossibilities()));
	}

}
