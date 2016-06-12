package testy.domain.question.mc;

import java.util.HashSet;
import java.util.Set;

import testy.domain.question.AbstractAnswerValidator;

public class MCAnswerValidator extends AbstractAnswerValidator<MCAnswer> {

	@Override
    public int validate(MCAnswer answer) {
	    int score = 0;
		
	    MCQuestion question = answer.getQuestion();
	    Set<MCPossibility> checkedAnswers = new HashSet<MCPossibility>();
	    for(MCPossibility sentPoss: answer.getCheckedPossibilities()) {
	    	checkedAnswers.add(findById(question.getPossibleAnswers(), sentPoss));
	    }
	    
	    Set<MCPossibility> uncheckedAnswers = new HashSet<MCPossibility>();
	    for(MCPossibility sentPoss: answer.getUncheckedPossibilities()) {
	    	uncheckedAnswers.add(findById(question.getPossibleAnswers(), sentPoss));
	    }
	    
	    score = (int)(Math.floor(question.getMaxScore() * (1 - errorQuote(checkedAnswers, uncheckedAnswers))));
	    
		return score;
    }
	
	private double errorQuote(Set<MCPossibility> checkedAnswers, Set<MCPossibility> uncheckedAnswers) {
		
		int errorCount = 0;
		int answerCount = 0;
		
		for(MCPossibility poss: checkedAnswers) {
			if(!poss.isCorrect()) {
				errorCount++;
			}
			answerCount++;
		}
		
		for(MCPossibility poss: uncheckedAnswers) {
			if(poss.isCorrect()) {
				errorCount++;
			}
			answerCount++;
		}
		
		if(errorCount == 0) {
			return 0;
		} else {
			double result = (double)errorCount / answerCount;
			return result;
		}
	}
	
	private MCPossibility findById(Iterable<MCPossibility> possibilities, MCPossibility possibility) {
		for(MCPossibility correctPossibility: possibilities) {
			if (possibility.getId() == correctPossibility.getId()) return correctPossibility;
		}
		return null;
	}
	
}
