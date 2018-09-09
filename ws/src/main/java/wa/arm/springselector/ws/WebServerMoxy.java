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

import org.glassfish.grizzly.http.server.HttpServer;

/**
 * @author rcjco
 *
 */
public class WebServerMoxy {

  private static final URI BASE_URI = URI.create("http://localhost:8080/springselector/");

  public static void main(String[] args) {
    try {
      final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, createApp(), false);
      Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
        @Override
        public void run() {
          server.shutdownNow();
        }
      }));
      server.start();
      
      System.out.println(String.format("Application started.%nStop the application using CTRL+C"));

      Thread.currentThread().join();
    } catch (IOException | InterruptedException ex) {
      Logger.getLogger(WebServerMoxy.class.getName()).log(Level.SEVERE, "Problem starting web server", ex);
    }
  }

  public static ResourceConfig createApp() {
    return new ResourceConfig()
        .packages("wa.arm.springselector.ws")
        .register(createMoxyJsonResolver());
  }

  public static ContextResolver<MoxyJsonConfig> createMoxyJsonResolver() {
    final MoxyJsonConfig moxyJsonConfig = new MoxyJsonConfig();
    Map<String, String> namespacePrefixMapper = new HashMap<String, String>(1);
    namespacePrefixMapper.put("http://www.w3.org/2001/XMLSchema-instance", "xsi");
    moxyJsonConfig.setNamespacePrefixMapper(namespacePrefixMapper).setNamespaceSeparator(':');
    return moxyJsonConfig.resolver();
  }
}