package testy.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

	@Autowired
	private RestAuthenticationSuccessHandler restAuthenticationSuccessHandler;

	@Autowired
	private RestLogoutSuccessHandler restLogoutSuccessHandler;

	@Autowired
	private Environment env;

	@Bean
	public RestUsernamePasswordAuthenticationFilter restFilter() throws Exception {
		RestUsernamePasswordAuthenticationFilter myFilter = new RestUsernamePasswordAuthenticationFilter();
		myFilter.setAuthenticationManager(authenticationManager());

		return myFilter;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterBefore(restFilter(), UsernamePasswordAuthenticationFilter.class);
		http.authorizeRequests().antMatchers("/**").fullyAuthenticated();
		http.exceptionHandling().authenticationEntryPoint(restAuthenticationEntryPoint);
		http.formLogin().loginProcessingUrl("/login")
		        .successHandler(restAuthenticationSuccessHandler);
		http.csrf().disable();
		http.logout().logoutUrl("/logout").deleteCookies("JSESSIONID")
		        .logoutSuccessHandler(restLogoutSuccessHandler);
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.ldapAuthentication().userDnPatterns(env.getProperty("ldap.userDnPatterns"))
		        .contextSource().port(Integer.parseInt(env.getProperty("ldap.port")))
		        .root(env.getProperty("ldap.root")).url(env.getProperty("ldap.url"));
	}
}