package wa.arm.springselector.ws;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import org.glassfish.grizzly.http.server.HttpServer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SpringSelectorWebServiceTest {

  private HttpServer mServer;
  private WebTarget mTarget;

  @Before
  public void setUp() throws Exception {
    // start the server
    mServer = WebServer.startServer();
    // create the client
    Client c = ClientBuilder.newClient();

    // uncomment the following line if you want to enable
    // support for JSON in the client (you also have to uncomment
    // dependency on jersey-media-json module in pom.xml and Main.startServer())
    // Enable JSON Support
    // TODO: Work out what's wrong with this
//        c.getConfiguration().enable(new org.glassfish.jersey.media.json.JsonJaxbFeature());

    mTarget = c.target(WebServer.BASE_URI);
  }

  @After
  public void tearDown() throws Exception {
    mServer.shutdown();
  }

  /**
   * Test to see that the message "Got it!" is sent in the response.
   */
  @Test
  public void testRunScenario() {
    String responseMsg = mTarget.path("springselector").request().get(String.class);
    assertEquals("Got it!", responseMsg);
  }
}