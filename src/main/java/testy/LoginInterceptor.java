package testy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import testy.controller.ApiController;
import testy.controller.NeedsLoggedInAccount;
import testy.controller.exception.NotEnoughPermissionsException;
import testy.security.service.CurrentAccountService;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter  {
	
	@Autowired
	CurrentAccountService accountService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		boolean isNoHandlerMethod = !(handler instanceof HandlerMethod);
		if (isNoHandlerMethod) {
			return true;
		}
		HandlerMethod method = (HandlerMethod) handler;
		
		NeedsLoggedInAccount annotation = method.getMethod().getAnnotation(NeedsLoggedInAccount.class);
		boolean annotationNotPresent = annotation == null;
		if(annotationNotPresent) {
			return true;
		}
		
		boolean isAdminNeeded = annotation.admin().equals("true");
		boolean userIsNotAdmin = !(accountService.getLoggedInAccount().isAdmin());
		if(isAdminNeeded && userIsNotAdmin) {
			throw new NotEnoughPermissionsException("Only admins are allowed to do this!");
		}
		
		ApiController controller = (ApiController) method.getBean();
		controller.setLoggedInAccount(accountService.getLoggedInAccount());
		
		return true;
	}

}
