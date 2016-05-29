package testy.jobs;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import testy.TestyMailSender;
import testy.TimeService;
import testy.dataaccess.QuestionPoolRepository;
import testy.domain.test.QuestionPool;
import testy.domain.test.TestResult;

@Component
public class CheckIfTestIsDue {

	@Autowired
	QuestionPoolRepository questionPoolRepo;
	
	@Autowired
	TimeService timeService;
	
	@Autowired
	Environment env;
	
	@Autowired
	TestyMailSender mailSender;
	
	public void exec() {
		for(QuestionPool pool: questionPoolRepo.findAll()) {
			for(TestResult result: pool.getResults()) {
				if (isTestDue(result)) {
					informUser(result);
				}
			}
		}
	}
	
	public boolean isTestDue(TestResult lastResult) {
		long now = timeService.getCurrentTime();
		int weekCount = lastResult.getQuestionPool().getWeeksAfterWhichTestHasToBeRepeated();
		if (weekCount < 1) return false;
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(lastResult.getTimestamp().getTime());
		calendar.add(Calendar.DAY_OF_YEAR, weekCount * 7);
		Date dueDate = calendar.getTime();
		return dueDate.before(new Date(now));
	}
	
	public boolean isTestMoreThanTwoWeeksDue(TestResult lastResult) {
		long now = timeService.getCurrentTime();
		int weekCount = lastResult.getQuestionPool().getWeeksAfterWhichTestHasToBeRepeated();
		if (weekCount < 1) return false;
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(lastResult.getTimestamp().getTime());
		calendar.add(Calendar.DAY_OF_YEAR, (weekCount + 2) * 7);
		Date dueDate = calendar.getTime();
		return dueDate.before(new Date(now));
	}
	
	private void informUser(TestResult result) {
		if (isTestMoreThanTwoWeeksDue(result)) {
			mailSender.sendMail(
				env.getProperty("mail.admin"),
				"Test 2 Wochen überfällig",
				result.getUser().getAccountName() + " ist seit 2 Wochen überfällig mit dem Test " + 
					result.getQuestionPool().getName() + ". Bitte leite entsprechende Maßnahmen ein oder " +
						"entferne den Nutzer von dem Test unter folgendem Link: " + 
						env.getProperty("application.link") + "pools/" + result.getQuestionPool().getId()
			);
		} else if (result.getUser().getEmail() == null || result.getUser().getEmail().isEmpty()) {
			mailSender.sendMail(
				env.getProperty("mail.admin"),
				"Test überfällig",
				result.getUser().getAccountName() + " muss den Test " + 
					result.getQuestionPool().getName() +
					" machen. Leider hat der Nutzer noch keine E-Mail Adresse hinterlegt. Bitte informiere ihn."
			);
		} else {
			mailSender.sendMail(
				result.getUser().getEmail(),
				"Test überfällig",
				"Hallo " + result.getUser().getAccountName() + ", du musst den Test" + 
					result.getQuestionPool().getName() +
					" mal wieder machen. Mache ihn zügig sonst wird ein Administrator informiert. In 2 Tagen "
					+ "wirst du erneut daran erinnert. Den Test kannst du hier machen: "
					+ env.getProperty("application.link") + "subjects/" + result.getQuestionPool().getSubject().getId()
			);
		}
	}
}
