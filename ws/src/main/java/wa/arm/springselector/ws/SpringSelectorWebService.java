package wa.arm.springselector.ws;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import wa.arm.springselector.Scenario;
import wa.arm.springselector.Spring;
import wa.arm.springselector.SpringSelector;

/**
 * JSON web wrapper around the Spring Selector.
 * 
 * @author Ray Cooke
 */
@Path("springselector")
public class SpringSelectorWebService {
  
  private final SpringSelector mSpringSelector;

  @Inject
  public SpringSelectorWebService(SpringSelector springSelector) {
    mSpringSelector = springSelector;
  }
  
  @POST
  @Path("runscenario")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public List<Spring> runScenario(Scenario scenario) {
    return mSpringSelector.runScenario(scenario);
  }
  
}
