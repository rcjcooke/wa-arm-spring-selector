package wa.arm.springselector.ws;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;

import wa.arm.springselector.Scenario;
import wa.arm.springselector.Spring;

public class SpringSelectorWebServiceTest extends JerseyTest {

  // The scenario used for test purposes
  private static final Scenario TEST_SCENARIO = new Scenario(15000, 1, 1, 1, new float[] {100, 200}, new float[] {100, 200}, 1200);

  private static final List<Spring> EXPECTED_SPRINGS = Arrays.asList(
      new Spring("Z-377I","Gutekunst",6.382,334.0645566,0,0,0,0),
      new Spring("Z-377X","Gutekunst",6.348,335.8538122,0,0,0,0),
      new Spring("Z-378I","Gutekunst",5.547,384.3519019,0,0,0,0),
      new Spring("Z-378X","Gutekunst",5.521,386.1619272,0,0,0,0),
      new Spring("Z-379I","Gutekunst",4.826,441.7737257,0,0,0,0),
      new Spring("Z-379X","Gutekunst",4.806,443.6121515,0,0,0,0),
      new Spring("Z-387I","Gutekunst",8.104,310.3405726,0,0,0,0),
      new Spring("Z-387X","Gutekunst",8.075,311.4551084,0,0,0,0)
      );  
  
  @Override
  protected Application configure() {
    enable(TestProperties.LOG_TRAFFIC);
    enable(TestProperties.DUMP_ENTITY);

    return WebServerMoxy.createApp();
  }

  @Override
  protected void configureClient(ClientConfig config) {
    config.register(WebServerMoxy.createMoxyJsonResolver());
  }

  @Test
    public void testRunScenario() {
        final WebTarget target = target("springselector");
        final List<Spring> springs = target
            .request(MediaType.APPLICATION_JSON_TYPE)
            .post(Entity.entity(TEST_SCENARIO, MediaType.APPLICATION_JSON_TYPE), new GenericType<List<Spring>>(){});

        assertEquals(EXPECTED_SPRINGS, springs);
    }
}