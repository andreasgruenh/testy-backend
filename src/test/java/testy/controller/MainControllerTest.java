package testy.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import testy.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class MainControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private FilterChainProxy filterChainProxy;
    
    @Autowired
    private WebApplicationContext webAppContext;
    
    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).dispatchOptions(true).addFilters(filterChainProxy).build();
    }
	
	@Test
	public void test() {
		Assert.assertTrue(true);
		
	}
	
	@Test
	public void testSimpleRequest() throws Exception {
		MvcResult result = mockMvc.perform(get("/profile")).andExpect(status().is(401)).andReturn();
	}
	
}
