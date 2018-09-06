/**
 * 
 */
package wa.arm.springselector.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import wa.arm.springselector.Scenario;
import wa.arm.springselector.Spring;
import wa.arm.springselector.SpringSelector;

/**
 * @author rcjco
 *
 */
//@WebService
@Path("springselector")
public class SpringSelectorWebService {
  
  private SpringSelector mSpringSelector;

  public SpringSelectorWebService() {
    try {
      mSpringSelector = new SpringSelector();
    } catch (InstantiationException e) {
      System.err.println("Failed to create Spring Selector");
      e.printStackTrace();
    }
  }
  
  //@WebMethod(action="runScenario")
  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String runScenario() {
    return mSpringSelector.runScenario(new Scenario()).toString();
  }
  
}
