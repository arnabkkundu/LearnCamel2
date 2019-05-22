package com.learncamel.learncamelspringboot.route;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
// Clears the Context from Previous Test
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("mock")
public class CSVFileToDBRouteMockTest extends CamelTestSupport {

    @Autowired
    CamelContext context;

    @Autowired
    Environment environment;

    @Autowired
    ProducerTemplate producerTemplate;

    @Autowired
    protected CamelContext createCamelContext(){
        return context;
    }

    @Test
    public void testMoveFileMock()  throws Exception{

        String message = "type,sku#,itemdescription,price\n" +
                "ADD,100,Samsung TV,500\n" +
                "ADD,101,LG TV,500";
        MockEndpoint mockEndpoint = getMockEndpoint(environment.getProperty("toRouteFile"));
        mockEndpoint.expectedMessageCount(1);
        mockEndpoint.expectedBodiesReceived(message);

        producerTemplate.sendBodyAndHeader(environment.getProperty("startRoute"),
                message, "env",environment.getProperty("spring.profiles.active"));

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testMoveFileMockDB()  throws Exception{

        String message = "type,sku#,itemdescription,price\n" +
                "ADD,100,Samsung TV,500\n" +
                "ADD,101,LG TV,500";

        String outputMessage= "DATA UPDATED SUCCESSFULLY";

        MockEndpoint mockEndpoint = getMockEndpoint(environment.getProperty("toRouteFile"));
        mockEndpoint.expectedMessageCount(1);
        mockEndpoint.expectedBodiesReceived(message);


        MockEndpoint mockEndpointDBSuccess =
                getMockEndpoint(environment.getProperty("toRouteFileDBSuccess"));

        mockEndpointDBSuccess.expectedMessageCount(1);
        mockEndpointDBSuccess.expectedBodiesReceived(outputMessage);

        producerTemplate.sendBodyAndHeader(environment.getProperty("startRoute"),
                message, "env",environment.getProperty("spring.profiles.active"));

        assertMockEndpointsSatisfied();
    }
}
