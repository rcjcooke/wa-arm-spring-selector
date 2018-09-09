/**
 * 
 */
package wa.arm.springselector.ws;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
      // TODO: Put the path in configuration
      mSpringSelector = new SpringSelector("data/Databases/basicData.csv");
    } catch (InstantiationException e) {
      System.err.println("Failed to create Spring Selector");
      e.printStackTrace();
    }
  }
  
  //@WebMethod(action="runScenario")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public List<Spring> runScenario(Scenario scenario) {
    return mSpringSelector.runScenario(scenario);
  }
  
}
