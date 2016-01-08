package testy.helper.annotations;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class AnnotationChecker extends TestWatcher {

	public boolean needsTestClasses = false;
	public boolean needsSessions = false;
	
	@Override
    protected void starting( Description description) {
		
        NeedsTestClasses testClassesAnnotation = description.getAnnotation( NeedsTestClasses.class);
        if(testClassesAnnotation != null) {
        	needsTestClasses = true;
        }
        
        NeedsSessions sessionAnnotation = description.getAnnotation( NeedsSessions.class);
        if(sessionAnnotation != null) {
        	needsSessions = true;
        }
    }
	
}
