package wa.arm.springselector.ws;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
  public List<Spring> runScenario(
      @DefaultValue("false") @QueryParam("returnAllSprings") boolean returnAllSprings, 
      Scenario scenario) {
    Logger.getLogger(SpringSelectorWebService.class.getName()).log(Level.INFO, "Running scenario: " + scenario);
    // Note: This gets ALL springs in the database by default (with those that match flagged as such)
    return mSpringSelector.runScenario(scenario, returnAllSprings);
  }
  
}
