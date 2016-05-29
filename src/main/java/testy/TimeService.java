package testy;

import org.springframework.stereotype.Component;

@Component
public class TimeService {

	public long getCurrentTime() {
		return System.currentTimeMillis();
	}
	
}
