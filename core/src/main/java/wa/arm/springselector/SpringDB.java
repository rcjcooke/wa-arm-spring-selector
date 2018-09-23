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
  private static final String RELEVENT_LENGTH_MM = "relevent_length_mm";
  private static final String RATE_N_MM = "rate_n_mm";
  private static final String MASS_KG = "mass_kg";
  private static final String MAX_FORCE_UNDER_STATIC_LOAD = "max_force_under_static_load";
  private static final String OUTSIDE_DIAMETER_MM = "outside_diameter";
  private static final String WIRE_DIAMETER_MM = "wire_diameter";

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
    } catch (SQLException e) {
      e.printStackTrace();
      throw new InstantiationException(e.getLocalizedMessage());
    }
  }

  /**
   * A list of springs from the spring database that matches the criteria.
   * 
   * @param massPerSpring             System mass handled by each spring
   * @param lengthToCOM               the length from the pivot to the Centre of
   *                                  Mass of the system being balanced
   * @param allowedRangeA_sc          The allowed vertical connection range
   * @param allowedRangeR2_sc         The allowed "along arm" connection range
   * @param mechanicalAdvantage       The mechanical advantage of the system
   * @param includeSpringMassInSystem True of the spring is to be situated inside
   *                                  the system being balanced - i.e. the mass of
   *                                  the spring is being balanced as well
   * @return A list of springs that match the requirements
   */
  public List<Spring> getMatchingSprings(double massPerSpring, double lengthToCOM, double[] allowedRangeA_sc,
      double[] allowedRangeR2_sc, double mechanicalAdvantage, boolean includeSpringMassInSystem) {

    ArrayList<Spring> selectedSpringList = new ArrayList<Spring>();
    ResultSet allEnergySpringsRS = null;
    // Different query based on whether the mass of the spring is included in the
    // problem
    String query = "";
    if (includeSpringMassInSystem) {
      query = "SELECT * FROM SPRINGS WHERE " + MAX_POTENTIAL_ENERGY_NMM + " >= (" + massPerSpring + " + " + MASS_KG
          + ")*" + PhysicalConstants.GRAVITY * 2 * lengthToCOM;
    } else {
      query = "SELECT * FROM SPRINGS WHERE " + MAX_POTENTIAL_ENERGY_NMM + " >= "
          + massPerSpring * PhysicalConstants.GRAVITY * lengthToCOM * 2;
    }
    try {
      Statement s = mConnection.createStatement();
      // Get the springs that fit the energy bracket only
      allEnergySpringsRS = s.executeQuery(query);
    } catch (SQLException e) {
      Logger.getLogger(SpringDB.class.getName()).log(Level.SEVERE, "Problem executing query: " + query, e);
    }

    if (allEnergySpringsRS != null) {
      double halfMassPotentialEnergy = massPerSpring * PhysicalConstants.GRAVITY * lengthToCOM;
      // Now only keep the springs that fit the criteria
      try {
        while (allEnergySpringsRS.next()) {
          try {
            double length = allEnergySpringsRS.getDouble(RELEVENT_LENGTH_MM);
            double springConstant = allEnergySpringsRS.getDouble(RATE_N_MM);

            /*
             * Calculate TheoAmin, TheoAmax, TheoR2min, TheoR2max for the current spring
             * based on Lr,k and the balancing condition ???
             * 
             * k = spring constant = rate Lr = length = relevent length = maximum length the
             * spring can extend to based on the maximum static force that can be applied to
             * it
             */
            if (includeSpringMassInSystem) {
              /*
               * If we're not including the mass of the spring then we've already calculated
               * this earlier and it applies equally to all springs, hence no "else"
               * statement.
               */
              halfMassPotentialEnergy = (massPerSpring + allEnergySpringsRS.getDouble(MASS_KG))
                  * PhysicalConstants.GRAVITY * lengthToCOM;
            }
            
            double[] theoR2 = { 0, 0 };
            double[] theoA = { 0, 0 };
            double[] finalR2 = { 0, 0 };
            double[] finalA = { 0, 0 };
            
            theoR2[0] = length / 2 - Math.sqrt(Math.pow(length / 2, 2) - (halfMassPotentialEnergy / springConstant));
            theoR2[1] = length / 2 + Math.sqrt(Math.pow(length / 2, 2) - (halfMassPotentialEnergy / springConstant));
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
              // We don't want this one - on to the next!
              continue;
            }

            if (!(halfMassPotentialEnergy / springConstant / finalR2[0] >= finalA[0])
                || !(halfMassPotentialEnergy / springConstant / finalR2[1] <= finalA[1])) {
              // We don't want this one - on to the next!
              continue;
            }

            // We've got this far so we DO want this Spring

            /*
             * Calculate intersection points of the characteristics with
             * rect[FinalA[0],FinalR2[0],FinalA[1],FinalR2[2]] of the real spring (behind
             * ratio)
             */
            finalA[0] = Math.max(halfMassPotentialEnergy / springConstant / finalR2[1], finalA[0]);
            finalA[1] = Math.min(halfMassPotentialEnergy / springConstant / finalR2[0], finalA[1]);
            finalR2[0] = halfMassPotentialEnergy / springConstant / finalA[1];
            finalR2[1] = halfMassPotentialEnergy / springConstant / finalA[0];

            /*
             * Record real values for "R2min" ,"R2max", "Amin", "Amax" for the Spring given
             * the selection scenario
             */
            selectedSpringList.add(createNewSpringFromCurrentResultSetRow(allEnergySpringsRS,
                mechanicalAdvantage * finalR2[0], mechanicalAdvantage * finalR2[1], mechanicalAdvantage * finalA[0],
                mechanicalAdvantage * finalA[1]));
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

  private Spring createNewSpringFromCurrentResultSetRow(ResultSet rs, double r2Min, double r2Max, double aMin,
      double aMax) throws SQLException {
    // TODO: Future upgrade: make this dynamic
    Spring spring = new Spring(rs.getString(ORDER_NUM), rs.getString(SUPPLIER), rs.getDouble(RATE_N_MM),
        rs.getDouble(RELEVENT_LENGTH_MM), rs.getDouble(MAX_FORCE_UNDER_STATIC_LOAD), rs.getDouble(MASS_KG),
        rs.getDouble(WIRE_DIAMETER_MM), rs.getDouble(OUTSIDE_DIAMETER_MM));
    spring.setR2Min(r2Min);
    spring.setR2Max(r2Max);
    spring.setAMin(aMin);
    spring.setAMax(aMax);
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
    sb.append(',').append(RELEVENT_LENGTH_MM).append(' ').append(DECIMAL); // Lr[mm] Relevante Laenge
    sb.append(',').append(MAX_POTENTIAL_ENERGY_NMM).append(' ').append(DECIMAL); // V[Nmm] Maximale potentielle Energie
    sb.append(',').append(MAX_FORCE_UNDER_STATIC_LOAD).append(' ').append(DECIMAL); // V[Nmm] Maximale potentielle
                                                                                    // Energie
    sb.append(',').append(OUTSIDE_DIAMETER_MM).append(' ').append(DECIMAL); // De
    sb.append(',').append(WIRE_DIAMETER_MM).append(' ').append(DECIMAL); // d
    return sb.toString();
  }

}
