package testy.domain.question;

public abstract class AbstractQuestionValidator<Q extends AbstractQuestion<Q, A>, A> {

	public abstract int validate(Q question, A answer);
	
}
