/**
 * 
 */
package wa.arm.springselector;

import java.util.List;

/**
 * @author rcjco
 *
 */
public class SpringSelector {

  private static final int INVALID_NUMBER_OF_ARGUMENTS_EXIT_CODE = 1;
  private static final int INVALID_ARGUMENTS_EXIT_CODE = 2;
  private static final int DB_INIT_FAILURE_EXIT_CODE = 3;

  // The Spring Database
  private SpringDB mSpringDB;

  /**
   * Creates a new SpringSelector and runs it.
   * 
   * @param datasetPath Relative or absolute path to the Spring dataset (e.g.
   *                    "data/Databases/basicData.csv")
   * @throws InstantiationException if there's a problem setting up the database
   *                                from the dataset
   */
  public SpringSelector(String datasetPath) throws InstantiationException {
    mSpringDB = new SpringDB(datasetPath);
  }

  /**
   * @param args
   */
  public static void main(String[] args) {

    Scenario scenario = null;
    try {
      if (args.length != 11) {
        printUsage();
        System.exit(INVALID_NUMBER_OF_ARGUMENTS_EXIT_CODE);
      }
      scenario = new Scenario(Float.parseFloat(args[1]), Integer.parseInt(args[2]), Float.parseFloat(args[3]),
          Float.parseFloat(args[4]), Float.parseFloat(args[5]), Float.parseFloat(args[6]),
          Float.parseFloat(args[7]), Float.parseFloat(args[8]), Float.parseFloat(args[9]), Boolean.parseBoolean(args[10]));
    } catch (Exception e) {
      // Some kind of parsing exception, we don't care what it was
      System.err.println("Problem parsing command line arguments");
      e.printStackTrace();
      printUsage();
      System.exit(INVALID_ARGUMENTS_EXIT_CODE);
    }

    try {
      SpringSelector ss = new SpringSelector(args[0]);
      List<Spring> matchingSprings = ss.runScenario(scenario);
      for (Spring s : matchingSprings) {
        System.out.println(s.toString());
      }
    } catch (InstantiationException e) {
      System.err
          .println("Failed to initialise service, please check that the dataset file is available at: " + args[0]);
      e.printStackTrace();
      System.exit(DB_INIT_FAILURE_EXIT_CODE);
    }
  }

  public static void printUsage() {
    // TODO: Print usage
  }

  void setup() {

//		SpringParameterList = new ArrayList<SpringParameter>(); // Create empty ArrayList for SpringParameters
//		TableSpringParameters = loadTable(mDataSet, "header");// GutekunstFull.csv //Load Springparameters from *.csv to
//																// Table Object TableSpringParameters
//		TableSpringsBalancingSelection = new Table(); // Create a table that holds the springs, that comply with the
//														// balancing condition for the current system
//		TableSpringsBalancingSelection.addColumn("Bestellnummer");
//		TableSpringsBalancingSelection.addColumn("SelectedMechanicalAdvantageZaehler");
//		TableSpringsBalancingSelection.addColumn("SelectedMechanicalAdvantageNenner");
//		TableSpringsBalancingSelection.addColumn("SelectedMechanicalAdvantage");
//		TableSpringsBalancingSelection.addColumn("SelectedNumberOfParallelSprings");
//		TableSpringsBalancingSelection.addColumn("R2min");
//		TableSpringsBalancingSelection.addColumn("R2max");
//		TableSpringsBalancingSelection.addColumn("Amin");
//		TableSpringsBalancingSelection.addColumn("Amax");
//		TableSpringsBalancingSelection.addColumn("A*R2");
//		TableSpringsBalancingSelection.addColumn("OverallMass");
//		TableSpringsBalancingSelection.addColumn("SpringLength");
//
//		All2DPlots = new ArrayList<ScatterPlot>();
//		All3DPlots = new ArrayList<ScatterPlot>();
//

    // TODO: Dynamic Spring parameters
//		// get all Parameternames(types) in TableSpringParameters and collect them in
//		// SpringParameterList
//		for (int i = 0; i < TableSpringParameters.getColumnCount(); i++) {
//			SpringParameterList.add(new SpringParameter(TableSpringParameters.getColumnTitle(i))); // Start by adding
//																									// one element
//		}
//		// set ColumnType (eg. 0 Table.STRING, 1 Table.INT, 2 Table.LONG, 3
//		// Table.FLOAT,4 Table.DOUBLE) for each column based on values in first row
//		for (int i = 0; i < SpringParameterList.size(); i++) {
//			if (Float.isNaN(TableSpringParameters.getFloat(0, SpringParameterList.get(i).ParameterName))) {
//				SpringParameterList.get(i).setTypeString();
//			} else {
//				SpringParameterList.get(i).setTypeFloat();
//			}
//		}
//		// Find the Min and Max values of each FLOAT Spring Parameter given in the
//		// SpringparameterTable
//		for (int i = 0; i < SpringParameterList.size(); i++) {
//			if (SpringParameterList.get(i).isTypeFloat()) {
//				SpringParameterList.get(i).Min = min(
//						TableSpringParameters.getFloatColumn(SpringParameterList.get(i).ParameterName));
//				SpringParameterList.get(i).Max = max(
//						TableSpringParameters.getFloatColumn(SpringParameterList.get(i).ParameterName));
//				print(i + " " + SpringParameterList.get(i).ParameterName + ": ");
//				println("MIN = " + SpringParameterList.get(i).Min + " Max = " + SpringParameterList.get(i).Max);
//			}
//		}
//
//		// find the Values of each STRING Spring Parameter given in the
//		// SpringparameterTable
//		for (int i = 0; i < SpringParameterList.size(); i++) {
//			if (SpringParameterList.get(i).isTypeString()) {
//				SpringParameterList.get(i).setListOfStringParameterOptions(
//						TableSpringParameters.getStringColumn(SpringParameterList.get(i).ParameterName));
//				print(i + " " + SpringParameterList.get(i).ParameterName + ": ");
//				println(SpringParameterList.get(i).ListOfStringParameterOptions.size());
//			}
//		}

    // TODO: Control UI
//
//		// Make ControlWindow
//		cf = addControlFrame("Parameter Selector -- Basic Mass Spring Balancer Calculator", SpringParameterList, 1500,
//				470); // Add a new Control frame to ControlP5 System
//
//		// Add a column Selected acting as a filter flag
//		TableSpringParameters.addColumn("Selected", Table.INT);
//
//		// Add a column "R2min" ,"R2max", "Amin", "Amax" for selected Spring Setup
//		TableSpringParameters.addColumn("R2min", Table.FLOAT);
//		TableSpringParameters.addColumn("R2max", Table.FLOAT);
//		TableSpringParameters.addColumn("Amin", Table.FLOAT);
//		TableSpringParameters.addColumn("Amax", Table.FLOAT);
//
//		// Initialize "Selected" column
//		for (TableRow row : TableSpringParameters.rows()) {
//			row.setInt("Selected", 1);
//		}
//

    // TODO: Visual Plotting
//		// 2D Scatter-Plots added to ArrayList All2DPlots
//		for (SpringParameter P : SpringParameterList) {
//			if (P.isTypeFloat()) {
//				for (SpringParameter Q : SpringParameterList) {
//					if (Q.isTypeFloat()) {
//						All2DPlots.add(new ScatterPlot(P, Q));
//					}
//				}
//			}
//		}
//
//		// 3D Scatter-Plots added to ArrayList All3DPlots
//		for (SpringParameter P : SpringParameterList) {
//			if (P.isTypeFloat()) {
//				for (SpringParameter Q : SpringParameterList) {
//					if (Q.isTypeFloat()) {
//						for (SpringParameter R : SpringParameterList) {
//							if (R.isTypeFloat()) {
//								All3DPlots.add(new ScatterPlot(Q, R, P));
//							}
//						}
//					}
//				}
//			}
//		}
//
//		// set Diagram Parameters 2D
//		for (ScatterPlot h : All2DPlots) {
//			h.setTable(TableSpringParameters).setColor(color(255, 0, 0)).setPointSize(3).setLabelSize(15)
//					.setPlotOnlySelected(true).setOriginXYZ(75, 75, 0)
//					.setMaxXYZAxis(width - 150, height - 150, height - 150);
//		}
//
//		// set Diagram Parameters 3D
//		for (ScatterPlot h : All3DPlots) {
//			h.setTable(TableSpringParameters).setColor(color(255, 0, 0)).setPointSize(3).setLabelSize(15)
//					.setPlotOnlySelected(true).setOriginXYZ(75, 75, 0)
//					.setMaxXYZAxis(width - 150, height - 150, height - 150);
//		}
//
//		TestCharacteristicsPlot = new SpringCharacteristics(0, 0, 0);
//		ortho(); // Start in ortho view mode
  }

  public List<Spring> runScenario(Scenario scenario) {
    // Calculate scaled values
    double mass_sc = scenario.getMass() / scenario.getNumberOfParallelSprings();
    double mechanicalAdvantage = scenario.getMechanicalAdvantageZaehler() / scenario.getMechanicalAdvantageNenner();

    // Scaled mass and scaled Allowed Ranges R2 and A
    double[] allowedRangeR2_sc = { 0, 0 };
    double[] allowedRangeA_sc = { 0, 0 };
    allowedRangeA_sc[0] = scenario.getAllowedRangeA()[0] / mechanicalAdvantage;
    allowedRangeA_sc[1] = scenario.getAllowedRangeA()[1] / mechanicalAdvantage;
    allowedRangeR2_sc[0] = scenario.getAllowedRangeR2()[0] / mechanicalAdvantage;
    allowedRangeR2_sc[1] = scenario.getAllowedRangeR2()[1] / mechanicalAdvantage;

    // Maximum Potential Energy of the mass in the system divided by 2 (to reduce
    // calculation overhead later)

    List<Spring> matchingSprings = mSpringDB.getMatchingSprings(mass_sc, scenario.getR1(), allowedRangeA_sc,
        allowedRangeR2_sc, mechanicalAdvantage, scenario.includeSpringMassInSystem());

    // TODO: Filter based on other dynamic parameters
//      for (int i = 0; i < SpringParameterList.size();i++) {
//        if(SpringParameterList.get(i).isTypeFloat())
//        {
//          IsSelected = IsSelected && SpringParameterList.get(i).isnMyRange(row.getFloat(SpringParameterList.get(i).ParameterName));
//        } 
//        else if(SpringParameterList.get(i).isTypeString())
//        {
//           //exclude "Bestellnummer" from isSelected-Test to optimice speed 
//          if(SpringParameterList.get(i).ParameterName.equals("Bestellnummer") == false){IsSelected = IsSelected && SpringParameterList.get(i).isInMySelectionList(row.getString(SpringParameterList.get(i).ParameterName));}
//        }
//        
    // TODO: Find minimum mass spring
    // Find minimum mass of single spring
//    int selectionCounter = 0;
//    for(TableRow row : TableSpringParameters.rows()){
//      if(row.getInt("Selected") ==1){
//        SelectionCounter++;
//        MinimumMass =min(MinimumMass,row.getFloat("Masse"));
//      } 
//    }
    // TODO: Print out minimum mass spring info
//    NumberOfSelectedSprings=SelectionCounter;
//      //Output Spring with Minimum mass in the current Selection
//    if(PlotMinimumMass){
//      cf.console.clear();  
//      println("Spring System(s) with the lowest mass in the current selection:");
//      println("--------------------------------------");
//      for(TableRow row : TableSpringParameters.rows()){
//        if((row.getInt("Selected")==1) && row.getFloat("Masse") == MinimumMass){
//          println(NumberOfParallelSprings + "  parallel  " + row.getString("Bestellnummer") + " (" + row.getString("Hersteller") +")");
//          println("Mechanical Advantage = " + MechanicalAdvantageZaehler+"/"+MechanicalAdvantageNenner);
//          println("Spring Rate = " + row.getFloat("k[N/mm] Federrate") + " N/mm");
//          println("Mass of single Spring = " + row.getFloat("Masse") + " g");
//          println("Mass of all Springs = " + row.getFloat("Masse")*NumberOfParallelSprings + " g");
//          println("Allowed Range A[mm] :  " +  nfc(row.getFloat("Amin"),2)  + " <= A <= " + nfc(row.getFloat("Amax"),2));
//          println("Allowed Range R2[mm] : " + nfc(row.getFloat("R2min"),2)  + " <= R2 <= " + nfc(row.getFloat("R2max"),2));
//          println("Condition for A and R2 : A*R2 =" + (mass*gravity*R1/row.getFloat("k[N/mm] Federrate")*MechanicalAdvantage*MechanicalAdvantage /NumberOfParallelSprings));
//          println("--------------------------------------");   
//        }
//      }
//      PlotMinimumMass =false;   
//    }
//    
    return matchingSprings;
  }

}
