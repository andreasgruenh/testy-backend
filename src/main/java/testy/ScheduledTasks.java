package testy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import testy.jobs.CheckIfTestIsDue;

@Component
public class ScheduledTasks {

	@Autowired
	CheckIfTestIsDue testChecker;
	
	// Alle 2 Tage
	@Scheduled(fixedDelay = 172800000)
	public void reportCurrentTime() {
		testChecker.exec();
	}
	
}
