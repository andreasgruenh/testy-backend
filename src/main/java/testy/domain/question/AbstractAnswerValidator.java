package testy.domain.question;

public abstract class AbstractAnswerValidator<A extends AbstractAnswer<?>> {

	public abstract int validate(A answer);
	
}
