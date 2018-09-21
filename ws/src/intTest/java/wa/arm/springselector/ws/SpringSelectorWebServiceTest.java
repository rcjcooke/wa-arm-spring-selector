package wa.arm.springselector.ws;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

import wa.arm.springselector.Scenario;
import wa.arm.springselector.Spring;

public class SpringSelectorWebServiceTest extends JerseyTest {

  // The scenario used for test purposes
  private static final Scenario TEST_SCENARIO = new Scenario(15000, 1, 1, 1, 100, 200, 100, 200, 1200, false);

  private static final List<Spring> EXPECTED_SPRINGS = Arrays.asList(
      new Spring("Z-377I","Gutekunst",2132,6.382,334.0645566,0,0,0,0,0),
      new Spring("Z-377X","Gutekunst",2132,6.348,335.8538122,0,0,0,0,0),
      new Spring("Z-378I","Gutekunst",2132,5.547,384.3519019,0,0,0,0,0),
      new Spring("Z-378X","Gutekunst",2132,5.521,386.1619272,0,0,0,0,0),
      new Spring("Z-379I","Gutekunst",2132,4.826,441.7737257,0,0,0,0,0),
      new Spring("Z-379X","Gutekunst",2132,4.806,443.6121515,0,0,0,0,0),
      new Spring("Z-387I","Gutekunst",2515,8.104,310.3405726,0,0,0,0,0),
      new Spring("Z-387X","Gutekunst",2515,8.075,311.4551084,0,0,0,0,0)
      );  
  
  @Override
  protected Application configure() {
    
    try {
      ResourceConfig config = WebServerMoxy.createApp();
  
      // Set up traffic logging - http://www.indestructiblevinyl.com/2016/07/23/logging-with-jersey-and-maven.html
      enable(TestProperties.LOG_TRAFFIC);
      enable(TestProperties.DUMP_ENTITY);
      config.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, "WARNING");
      
      return config;
    } catch (InstantiationException e) {
      Logger.getLogger(SpringSelectorWebServiceTest.class.getName()).log(Level.SEVERE, "Problem creating Spring Selector", e);
      return null;
    }
  }

  @Override
  protected void configureClient(ClientConfig config) {
    config.register(WebServerMoxy.createMoxyJsonResolver());
  }

  @Test
    public void testRunScenario() {
        final WebTarget target = target("springselector/runscenario");        
        final List<Spring> springs = target
            .request(MediaType.APPLICATION_JSON_TYPE)
            .post(
                Entity.entity(TEST_SCENARIO, MediaType.APPLICATION_JSON_TYPE), 
                new GenericType<List<Spring>>(){});

        assertEquals(EXPECTED_SPRINGS, springs);
    }
}