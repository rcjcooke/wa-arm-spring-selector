/**
 * 
 */
package wa.arm.springselector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Spring selection logic adapted from Frank Beinersdorf's spring selection
 * Processing app.
 * 
 * @author Ray Cooke
 */
public class SpringDB {

  // Table column constants
  private static final String ORDER_NUM = "order_num";
  private static final String SUPPLIER = "supplier";
  private static final String MAX_POTENTIAL_ENERGY_NMM = "max_potential_energy_nnm";
  private static final String MAXIMUM_DEFLECTION_MM = "maximum_deflection_mm";
  private static final String RATE_N_MM = "rate_n_mm";
  private static final String MASS_KG = "mass_kg";
  private static final String MAX_FORCE_UNDER_STATIC_LOAD = "max_force_under_static_load";
  private static final String OUTSIDE_DIAMETER_MM = "outside_diameter";
  private static final String WIRE_DIAMETER_MM = "wire_diameter";
  private static final String UNSTRESSED_LENGTH_MM = "unstressed_length_mm";
  

  // Relevant type constants
  private static final String DECIMAL = "DECIMAL(20,20)";
  private static final String VARCHAR = "VARCHAR(20)";

  // The database connection
  private Connection mConnection;

  /**
   * SpringDB Constructor. Sets up the SpringDB and imports the specified data
   * set.
   * 
   * @param dataSetPath The path to the CSV data set to import
   * @throws InstationException if there was a problem setting up the database
   */
  public SpringDB(String dataSetPath) throws InstantiationException {

    // Add a system property to allow a text table to be used in an all-in-memory DB
    Properties p = new Properties(System.getProperties());
    p.setProperty("textdb.allow_full_path", "true");
    System.setProperties(p);

    try {
      // Create an in-memory database for the Springs
      mConnection = DriverManager.getConnection("jdbc:hsqldb:mem:inMemSpringDB", "SA", "");
      // Import the Spring catalogue data to it
      // TODO: Add an ID number column to the CSV
      mConnection.createStatement().execute("CREATE TEXT TABLE Springs (" + generateSpringTableDescription() + ")");
      // NOTE: DESC because we're only reading from it
      mConnection.createStatement().execute("SET TABLE Springs SOURCE \"" + dataSetPath + ";ignore_first=true\" DESC");
      // Provide some useful logging
      ResultSet rs = mConnection.createStatement().executeQuery("SELECT COUNT(*) AS rowcount FROM Springs");
      rs.next();
      Logger.getLogger(SpringDB.class.getName()).log(Level.INFO, "Successfully loaded " + rs.getInt("rowcount") + " springs");
    } catch (SQLException e) {
      e.printStackTrace();
      throw new InstantiationException(e.getLocalizedMessage());
    }
  }

  /**
   * A list of springs from the spring database that matches the criteria.
   * 
   * @param systemMassPerSpring       System mass handled by each spring
   * @param payloadMassPerSpring      Additional payload mass handled by each spring
   * @param lengthToCOM               the length from the pivot to the Centre of
   *                                  Mass of the system being balanced
   * @param allowedRangeA_sc          The allowed vertical connection range
   * @param allowedRangeR2_sc         The allowed "along arm" connection range
   * @param mechanicalAdvantage       The mechanical advantage of the system
   * @param includeSpringMassInSystem True of the spring is to be situated inside
   *                                  the system being balanced - i.e. the mass of
   *                                  the spring is being balanced as well
   * @param dynamicBalancingRequired  True if the system needs to be able to
   *                                  balance with massPerSpring=0 as well as
   *                                  massPerSpring
   * @param fixedPosition             A = A, R = R2 or N = Neither: The position that will be fixed in a dynamically balancing system.
   * @param returnAllSprings          If true then all springs are returned regardless of whether they match. Springs that fit the 
   *                                  scenario have the "mInScenario" flag set to true on them. 
   * @return A list of springs that match the requirements
   */
  public List<Spring> getMatchingSprings(double systemMassPerSpring, double payloadMassPerSpring, double lengthToCOM,
      double[] allowedRangeA_sc, double[] allowedRangeR2_sc, double mechanicalAdvantage,
      boolean includeSpringMassInSystem, boolean dynamicBalancingRequired, char fixedPosition, boolean returnAllSprings) {

    ArrayList<Spring> selectedSpringList = new ArrayList<Spring>();
    ResultSet allEnergySpringsRS = null;
    // Different query based on whether the mass of the spring is included in the
    // problem
    String query = "";
    if (!returnAllSprings) {
      if (includeSpringMassInSystem) {
        query = "SELECT * FROM SPRINGS WHERE " + MAX_POTENTIAL_ENERGY_NMM + " >= ("
            + (systemMassPerSpring + payloadMassPerSpring) + " + " + MASS_KG + ")*"
            + PhysicalConstants.GRAVITY * 2 * lengthToCOM;
      } else {
        query = "SELECT * FROM SPRINGS WHERE " + MAX_POTENTIAL_ENERGY_NMM + " >= "
            + (systemMassPerSpring + payloadMassPerSpring) * PhysicalConstants.GRAVITY * lengthToCOM * 2;
      }
    } else {
      query = "SELECT * FROM SPRINGS";
    }
    try {
      Statement s = mConnection.createStatement();
      // Get the springs that fit the energy bracket only
      allEnergySpringsRS = s.executeQuery(query);
    } catch (SQLException e) {
      Logger.getLogger(SpringDB.class.getName()).log(Level.SEVERE, "Problem executing query: " + query, e);
    }

    if (allEnergySpringsRS != null) {
      // Now only keep the springs that fit the criteria
      try {
        while (allEnergySpringsRS.next()) {
          
          try {
            
            // Create the spring
            Spring spring = createNewSpringFromCurrentResultSetRow(allEnergySpringsRS);
            if (returnAllSprings) {
              // We want every spring, even if it doesn't fit the scenario
              selectedSpringList.add(spring);
            }
            
            // First check energy requirements if we haven't already
            if (returnAllSprings) {
              if (includeSpringMassInSystem) {
                if (spring.getMaximumPotentialEnergy() < (systemMassPerSpring + payloadMassPerSpring + spring.getMass()) * PhysicalConstants.GRAVITY * lengthToCOM * 2) {
                  // This spring doesn't cut it - next!
                  continue;
                }
              } else {
                if (spring.getMaximumPotentialEnergy() < (systemMassPerSpring + payloadMassPerSpring) * PhysicalConstants.GRAVITY * lengthToCOM * 2) {
                  // This spring doesn't cut it - next!
                  continue;
                }
              }
            }

            double springMaxlength = spring.getMaximumDeflection();
            double springConstant = spring.getRate();
            double springMass = spring.getMass();

            SpringScenario ssFP = testSpringAgainstScenario(
                springMaxlength, 
                springConstant, 
                springMass, 
                includeSpringMassInSystem, 
                payloadMassPerSpring, 
                systemMassPerSpring, 
                lengthToCOM, 
                allowedRangeA_sc, 
                allowedRangeR2_sc);
            
            // Check that the spring passed the scenario
            if (ssFP == null) continue;
            SpringScenario ssZP = null;
            /*
             * If we're dynamically balancing then we also need to know that this spring can
             * work within the A and R2 allowed ranges with no load
             */
            if (dynamicBalancingRequired) {
              /* We've picked a spring that can cope with the max mass, now check it can still cope with
               payloadMass=0 */
              double[] a_range = allowedRangeA_sc;
              double[] r2_range = allowedRangeR2_sc;
              switch (fixedPosition) {
              case 'A':
                a_range[0] = ssFP.getAMin();
                a_range[1] = ssFP.getAMax();
                break;
              case 'R':
                r2_range[0] = ssFP.getR2Min();
                r2_range[1] = ssFP.getR2Max();
                break;
              default:
                a_range = allowedRangeA_sc;
                r2_range = allowedRangeR2_sc;
                break;
              }
              ssZP = testSpringAgainstScenario(
                  springMaxlength, 
                  springConstant, 
                  springMass, 
                  includeSpringMassInSystem, 
                  0, 
                  systemMassPerSpring, 
                  lengthToCOM, 
                  a_range, 
                  r2_range);
              // Check that the spring passed the scenario
              if (ssZP == null) continue;
            }
            
            // We've got this far so we DO want this Spring
            spring.setInScenario(true);
            if (!returnAllSprings) {
              // We only return the springs that fit the scenario
              selectedSpringList.add(spring);
            }
            
            /*
             * Record real values for "R2min" ,"R2max", "Amin", "Amax" for the Spring given
             * the selection scenario
             */
            spring.setAMin(mechanicalAdvantage * ssFP.getAMin());
            spring.setAMax(mechanicalAdvantage * ssFP.getAMax());
            spring.setR2Min(mechanicalAdvantage * ssFP.getR2Min());
            spring.setR2Max(mechanicalAdvantage * ssFP.getR2Max());
            spring.setSpringCPMin(mechanicalAdvantage * ssFP.getSpringCPMin());
            spring.setSpringCPMax(mechanicalAdvantage * ssFP.getSpringCPMax());
            // Record the multiple of A and R2 for the maximum payload against the spring (for client-side calcs)
            spring.setMaxPayloadAnchorPointFactor(mechanicalAdvantage * mechanicalAdvantage * ssFP.getPayloadAnchorPointFactor());
            // Record the optimum connection point for minimising the max deflection
            spring.setOptimumConnectionPointA(mechanicalAdvantage * ssFP.getOptimumConnectionPointA());
            spring.setOptimumMaxLengthInScenario(spring.getUnstressedLength() + mechanicalAdvantage * (ssFP.getOptimumConnectionPointA() + ssFP.getPayloadAnchorPointFactor() / ssFP.getOptimumConnectionPointA()));
            // Record the multiple of A and R2 for zero payload against the spring if required (for client-side calcs)
            if (dynamicBalancingRequired) spring.setZeroPayloadAnchorPointFactor(mechanicalAdvantage * mechanicalAdvantage * ssZP.getPayloadAnchorPointFactor());

          } catch (SQLException e) {
            Logger.getLogger(SpringDB.class.getName()).log(Level.WARNING, "Problem processing spring at row "
                + allEnergySpringsRS.getRow() + " in result set returned from query: " + query, e);
          }
        }
      } catch (SQLException e) {
        Logger.getLogger(SpringDB.class.getName()).log(Level.SEVERE,
            "Problem processing result set from query: " + query, e);
      }
    }

    return selectedSpringList;
  }
  
  private SpringScenario testSpringAgainstScenario(double springMaxlength, double springConstant, double springMass, boolean includeSpringMassInSystem, double payloadMassPerSpring, double systemMassPerSpring, double lengthToCOM, double[] allowedRangeA_sc, double[] allowedRangeR2_sc) {
    /*
     * Calculate TheoAmin, TheoAmax, TheoR2min, TheoR2max for the current spring
     * based on Lr,k and the balancing condition ???
     * 
     * k = spring constant = rate Lr = length = relevent length = maximum length the
     * spring can extend to based on the maximum static force that can be applied to
     * it
     */
    double halfMassPotentialEnergy;
    if (includeSpringMassInSystem) {
      /*
       * If we're not including the mass of the spring then we've already calculated
       * this earlier and it applies equally to all springs, hence no "else"
       * statement.
       */
      halfMassPotentialEnergy = (systemMassPerSpring + payloadMassPerSpring + springMass)
          * PhysicalConstants.GRAVITY * lengthToCOM;
    } else {
      halfMassPotentialEnergy = (systemMassPerSpring + payloadMassPerSpring) * PhysicalConstants.GRAVITY * lengthToCOM;
    }

    double[] theoR2 = { 0, 0 };
    double[] theoA = { 0, 0 };
    double[] finalR2 = { 0, 0 };
    double[] finalA = { 0, 0 };

    theoR2[0] = springMaxlength / 2 - Math.sqrt(Math.pow(springMaxlength / 2, 2) - (halfMassPotentialEnergy / springConstant));
    theoR2[1] = springMaxlength / 2 + Math.sqrt(Math.pow(springMaxlength / 2, 2) - (halfMassPotentialEnergy / springConstant));
    theoA[0] = theoR2[0];
    theoA[1] = theoR2[1];

    /*
     * Calculate rect[FinalAmin,FinalR2min,FinalAmax,FinalR2max] by intersecting
     * quad[TheoAmin,TheoR2min,TheoAmin,TheoAmax] and
     * rect[AllowedRangeAmin,AllowedRangeAmax,AllowedRangeR2min,AllowedRangeR2max]
     */
    finalA[0] = Math.max(theoA[0], allowedRangeA_sc[0]);
    finalA[1] = Math.min(theoA[1], allowedRangeA_sc[1]);
    finalR2[0] = Math.max(theoR2[0], allowedRangeR2_sc[0]);
    finalR2[1] = Math.min(theoR2[1], allowedRangeR2_sc[1]);

    if ((finalA[0] > finalA[1]) || (finalR2[0] > finalR2[1])) {
      // This one doesn't fit the scenario - on to the next!
      return null;
    }

    if (!(halfMassPotentialEnergy / springConstant / finalR2[0] >= finalA[0])
        || !(halfMassPotentialEnergy / springConstant / finalR2[1] <= finalA[1])) {
      // This one doesn't fit the scenario - on to the next!
      return null;
    }

    /*
     * Calculate intersection points of the characteristics with
     * rect[FinalA[0],FinalR2[0],FinalA[1],FinalR2[1]] of the real spring (behind
     * ratio)
     */
   finalA[0] = Math.max(halfMassPotentialEnergy / springConstant / finalR2[1], finalA[0]);
   finalA[1] = Math.min(halfMassPotentialEnergy / springConstant / finalR2[0], finalA[1]);
   finalR2[0] = halfMassPotentialEnergy / springConstant / finalA[1];
   finalR2[1] = halfMassPotentialEnergy / springConstant / finalA[0];
    
    double payloadAnchorPointFactor = halfMassPotentialEnergy / springConstant;
    
    // Calculate the optimum connection points to minimise maximum deflection
    double optConnectionPointA = Math.sqrt(payloadAnchorPointFactor);
    double optConnectionPointR2 = optConnectionPointA;
    // Fit these to what's available
    if (optConnectionPointA > finalA[1]) { // above rectangle
      optConnectionPointA = finalA[1];
      optConnectionPointR2 = payloadAnchorPointFactor / optConnectionPointA;
    }
    if (optConnectionPointR2 < finalR2[0]) { // left of rectangle
      optConnectionPointR2 = finalR2[0];
      optConnectionPointA = payloadAnchorPointFactor / optConnectionPointR2;
    }
    if (optConnectionPointA < finalA[0]) { // below rectangle
      optConnectionPointA = finalA[0];
      optConnectionPointR2 = payloadAnchorPointFactor / optConnectionPointA;
    }
    if (optConnectionPointR2 > finalR2[1]) { // right of rectangle
      optConnectionPointR2 = finalR2[1];
      optConnectionPointA = payloadAnchorPointFactor / optConnectionPointR2;
    }
    
    return new SpringScenario(finalR2[0], finalR2[1], finalA[0], finalA[1], payloadAnchorPointFactor, optConnectionPointA, theoA[0], theoA[1]);
  }

  private Spring createNewSpringFromCurrentResultSetRow(ResultSet rs) throws SQLException {
    // TODO: Future upgrade: make this dynamic
    Spring spring = new Spring(rs.getString(ORDER_NUM), rs.getString(SUPPLIER), rs.getDouble(RATE_N_MM),
        rs.getDouble(MAXIMUM_DEFLECTION_MM), rs.getDouble(MAX_FORCE_UNDER_STATIC_LOAD), rs.getDouble(MASS_KG),
        rs.getDouble(WIRE_DIAMETER_MM), rs.getDouble(OUTSIDE_DIAMETER_MM), rs.getDouble(UNSTRESSED_LENGTH_MM), 
        rs.getDouble(MAX_POTENTIAL_ENERGY_NMM));
    return spring;
  }

  private String generateSpringTableDescription() {
    // TODO: Change this to auto-generate from the headers in the CSV later
    StringBuilder sb = new StringBuilder();
    /*
     * Internal Spring ID Number for referencing
     */
    sb.append(ORDER_NUM).append(' ').append(VARCHAR); // Bestellnummer
    sb.append(',').append(SUPPLIER).append(' ').append(VARCHAR); // Hersteller
    sb.append(',').append(RATE_N_MM).append(' ').append(DECIMAL); // k[N/mm] Federrate
    sb.append(',').append(MASS_KG).append(' ').append(DECIMAL); // Masse
    sb.append(',').append(MAXIMUM_DEFLECTION_MM).append(' ').append(DECIMAL); // Lr[mm] Relevante Laenge
    sb.append(',').append(MAX_POTENTIAL_ENERGY_NMM).append(' ').append(DECIMAL); // V[Nmm] Maximale potentielle Energie
    sb.append(',').append(MAX_FORCE_UNDER_STATIC_LOAD).append(' ').append(DECIMAL); // V[Nmm] Maximale potentielle
                                                                                    // Energie
    sb.append(',').append(OUTSIDE_DIAMETER_MM).append(' ').append(DECIMAL); // De
    sb.append(',').append(WIRE_DIAMETER_MM).append(' ').append(DECIMAL); // d
    sb.append(',').append(UNSTRESSED_LENGTH_MM).append(' ').append(DECIMAL); // L0
    return sb.toString();
  }

}
