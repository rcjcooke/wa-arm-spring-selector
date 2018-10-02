package wa.arm.springselector.ws;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.ext.ContextResolver;

import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.moxy.json.MoxyJsonConfig;
import org.glassfish.jersey.server.ResourceConfig;

import wa.arm.springselector.SpringSelector;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 * @author Ray Cooke
 *
 */
public class WebServerMoxy extends ResourceConfig {

  private static final URI BASE_URI = URI.create("http://localhost:8080/");
  private static final String DEFAULT_DATABASE_PATH = "data/Databases/processed.csv";

  private final SpringSelector mSpringSelector;

  /**
   * Creates a new instance of the application as a JSON enabled Web App instance.
   * @param databasePath The path to the database CSV file, e.g. data/Databases/processed.csv
   * @throws InstantiationException If there's a problem loading the database
   */
  public WebServerMoxy(String databasePath) throws InstantiationException {
    // Create our singleton Spring Selector instance
    // TODO: Put the path in configuration
    mSpringSelector = new SpringSelector(databasePath);
    // Pull in our web resources
    packages("wa.arm.springselector.ws");
    // Make sure we can talk JSON
    register(createMoxyJsonResolver());
    // Handle Cross-Origin Resource Sharing (CORS)
    // http://www.codingpedia.org/ama/how-to-add-cors-support-on-the-server-side-in-java-with-jersey/
    register(CORSResponseFilter.class);
    // Sort out Spring Selector singleton dependency
    register(new AbstractBinder() {
      @Override
      protected void configure() {
        /*
         * Immediate scope is singleton and created on startup (not first request)
         * http://www.riptutorial.com/jersey/example/23632/basic-dependency-injection-
         * using-jersey-s-hk2 As we're creating a local instance first, we don't need to
         * specify the scope, otherwise we would add <code>.in(Immediate.class);</code>
         * to the end
         */
        bind(mSpringSelector).to(SpringSelector.class);
      }
    });
  }

  public static void main(String[] args) {
    try {
      final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, createApp(DEFAULT_DATABASE_PATH), false);
      Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
        @Override
        public void run() {
          server.shutdownNow();
        }
      }));
      server.start();

      System.out.println(String.format("Application started.%nStop the application using CTRL+C"));

      Thread.currentThread().join();
    } catch (IOException | InstantiationException | InterruptedException ex) {
      Logger.getLogger(WebServerMoxy.class.getName()).log(Level.SEVERE, "Problem starting web server", ex);
    }
  }

  public static ResourceConfig createApp(String databasePath) throws InstantiationException {
    return new WebServerMoxy(databasePath);
  }

  public static ContextResolver<MoxyJsonConfig> createMoxyJsonResolver() {
    final MoxyJsonConfig moxyJsonConfig = new MoxyJsonConfig();
    Map<String, String> namespacePrefixMapper = new HashMap<String, String>(1);
    namespacePrefixMapper.put("http://www.w3.org/2001/XMLSchema-instance", "xsi");
    moxyJsonConfig.setNamespacePrefixMapper(namespacePrefixMapper).setNamespaceSeparator(':');
    return moxyJsonConfig.resolver();
  }
}