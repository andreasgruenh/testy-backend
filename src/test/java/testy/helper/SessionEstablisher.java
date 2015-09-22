package testy.helper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;

@Service
public class SessionEstablisher {

	@Autowired
	private Environment env;

	public MockHttpSession getUserSessionWith(MockMvc mock) throws Exception {
		return (MockHttpSession) mock
		        .perform(
		                post("/login").param("username", env.getProperty("ldap.loginTester"))
		                        .param("password", env.getProperty("ldap.loginTesterPw")))
		        .andExpect(status().isOk()).andReturn().getRequest().getSession();
	}

	public MockHttpSession getAdminSessionWith(MockMvc mock) throws Exception {
		return (MockHttpSession) mock
		        .perform(
		                post("/login").param("username", env.getProperty("ldap.loginAdmin")).param(
		                        "password", env.getProperty("ldap.loginAdminPw")))
		        .andExpect(status().isOk()).andReturn().getRequest().getSession();
	}

}
