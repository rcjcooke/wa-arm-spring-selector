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

/**
 * @author rcjco
 *
 */
public class SpringDB {

  // Table column constants
  private static final String SPRING_ID = "spring_id";

  private static final String ORDER_NUM = "order_num";
  private static final String SUPPLIER = "supplier";
  private static final String MAX_POTENTIAL_ENERGY_NMM = "max_potential_energy_nnm";
  private static final String RELEVENT_LENGTH_MM = "relevent_length_mm";
  private static final String RATE_N_MM = "rate_n_mm";
  private static final String MASS_KG = "mass_kg";

//  private static final String SELECTED = "selected";
//  private static final String R2MIN = "r2min";
//  private static final String R2MAX = "r2max";
//  private static final String AMIN = "amin";
//  private static final String AMAX = "amax";

  // Relevant type constants
  private static final String DECIMAL = "DECIMAL(20,20)";
  private static final String VARCHAR = "VARCHAR(20)";
//  private static final String BOOLEAN = "BOOLEAN";

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
      // Create the spring selection table
//      mConnection.createStatement()
//          .execute("CREATE TABLE SelectedSprings (" + generateSelectedSpringTableDescription() + ")");
    } catch (SQLException e) {
      e.printStackTrace();
      throw new InstantiationException(e.getLocalizedMessage());
    }
  }

  public List<Spring> getMatchingSprings(double halfMassPotentialEnergy, double[] allowedRangeA_sc,
      double[] allowedRangeR2_sc, double mechanicalAdvantage) {

    ArrayList<Spring> selectedSpringList = new ArrayList<Spring>();

    try {
      Statement s = mConnection.createStatement();
      // Get the springs that fit the energy bracket only
      ResultSet allEnergySpringsRS = s.executeQuery(
          "SELECT * FROM SPRINGS WHERE " + MAX_POTENTIAL_ENERGY_NMM + " >= " + halfMassPotentialEnergy * 2);

      // Now only keep the springs that fit the criteria
      while (allEnergySpringsRS.next()) {

        double length = allEnergySpringsRS.getDouble(RELEVENT_LENGTH_MM);
        double springConstant = allEnergySpringsRS.getDouble(RATE_N_MM);

        double[] theoR2 = { 0, 0 };
        double[] theoA = { 0, 0 };

        double[] finalR2 = { 0, 0 };
        double[] finalA = { 0, 0 };

        /*
         * Calculate TheoAmin, TheoAmax, TheoR2min, TheoR2max for the current spring
         * based on Lr,k and the balancing condition ???
         */
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
        selectedSpringList
            .add(createNewSpringFromCurrentResultSetRow(allEnergySpringsRS, mechanicalAdvantage * finalR2[0],
                mechanicalAdvantage * finalR2[1], mechanicalAdvantage * finalA[0], mechanicalAdvantage * finalA[1]));
      }
    } catch (SQLException e) {
      // TODO: deal with exception
      e.printStackTrace();
    }
    return selectedSpringList;
  }

  private Spring createNewSpringFromCurrentResultSetRow(ResultSet rs, double r2Min, double r2Max, double aMin,
      double aMax) throws SQLException {
    // TODO: Future upgrade: make this dynamic
//    int numColumns = rs.getMetaData().getColumnCount();
//    for (int i=0; i<numColumns; i++) {
//    }
    return new Spring(rs.getString(ORDER_NUM), rs.getString(SUPPLIER), rs.getDouble(RATE_N_MM),
        rs.getDouble(RELEVENT_LENGTH_MM), r2Min, r2Max, aMin, aMax);
  }

  private String generateSpringTableDescription() {
    // TODO: Change this to auto-generate from the headers in the CSV later
    StringBuilder sb = new StringBuilder();
    /*
     * Internal Spring ID Number for referencing
     */
    //sb.append(SPRING_ID).append(' ').append("INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY").append(',');
    sb.append(ORDER_NUM).append(' ').append(VARCHAR); // Bestellnummer
    sb.append(',').append(SUPPLIER).append(' ').append(VARCHAR); // Hersteller
    sb.append(',').append(RATE_N_MM).append(' ').append(DECIMAL); // k[N/mm] Federrate
    sb.append(',').append(MASS_KG).append(' ').append(DECIMAL); // Masse
    sb.append(',').append(RELEVENT_LENGTH_MM).append(' ').append(DECIMAL); // Lr[mm] Relevante Laenge
    sb.append(',').append(MAX_POTENTIAL_ENERGY_NMM).append(' ').append(DECIMAL); // V[Nmm] Maximale potentielle Energie
    return sb.toString();
  }

//  private String generateSelectedSpringTableDescription() {
//    // Add a column "R2min" ,"R2max", "Amin", "Amax" for selected Spring Setup
//    StringBuilder sb = new StringBuilder();
//    sb.append(SPRING_ID).append(' ').append("INTEGER NOT NULL PRIMARY KEY").append(',');
//    sb.append(SELECTED).append(' ').append(BOOLEAN).append(',');
//    sb.append(R2MIN).append(' ').append(DECIMAL).append(',');
//    sb.append(R2MAX).append(' ').append(DECIMAL).append(',');
//    sb.append(AMIN).append(' ').append(DECIMAL).append(',');
//    sb.append(AMAX).append(' ').append(DECIMAL).append(',');
//    sb.append("FOREIGN KEY ").append(SPRING_ID).append(" REFERENCES Springs (").append(SPRING_ID).append(')');
//    return sb.toString();
//  }

}
