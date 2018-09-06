package wa.arm.springselector;

public class Scenario {

  private float mMass = 1000; // system mass + payload mass [g]
  private int mNumberOfParallelSprings = 1; // Number of equivalent parallel springs
  private float mMechanicalAdvantageZaehler = 1; // TODO
  private float mMechanicalAdvantageNenner = 1; // TODO
  private float[] mAllowedRangeR2 = { 100, 200 }; // distance spring connection on lever to Pivot [mm]
  private float[] mAllowedRangeA = { 100, 200 }; // distance spring connection on fixxed y-Axis to Pivot [mm]
  private float mR1 = 1000; // Lever => distance CoM to Pivot [mm]

//  // constrains for the balancing system
//  private int MaxNumberOfParallelSprings = 20;
//  private float MaxMechanicalAdvantageZaehler = 10;
//  private float MaxMechanicalAdvantageNenner = 10; //
//
//  //
//  // draw()
//  private float[] AllowedRangeLr = { 0, 0 };
//  private float[] AllowedRangeFn = { 0, 0 };
//
//  private int WorkContext = 1;
//
////  private Table TableSpringParameters; // Table to hold SpringParamters imported from *.csv-file
////  private Table TableSpringsBalancingSelection; // Table to hold SpringParamters imported from *.csv-file
//
//  private ArrayList<SpringParameter> SpringParameterList; // dynamic list of SpringParameters
//
//  private float MinimumMass = 1000000000;
//  private int CounterX = 0;
//  private int NumberOfSelectedSprings = 0;
//  private int SelectionCounter = 0;
//
//  private boolean PlotMinimumMass = false;
//  private boolean FindAllPossibleSprings = false;
//  private boolean ExportVariations = false;

  public double getMass() {
    return mMass;
  }

  /**
   * @return the numberOfParallelSprings
   */
  public int getNumberOfParallelSprings() {
    return mNumberOfParallelSprings;
  }

  /**
   * @return the mechanicalAdvantageZaehler
   */
  public float getMechanicalAdvantageZaehler() {
    return mMechanicalAdvantageZaehler;
  }

  /**
   * @return the mechanicalAdvantageNenner
   */
  public float getMechanicalAdvantageNenner() {
    return mMechanicalAdvantageNenner;
  }

  /**
   * @return the allowedRangeR2
   */
  public float[] getAllowedRangeR2() {
    return mAllowedRangeR2;
  }

  /**
   * @return the allowedRangeA
   */
  public float[] getAllowedRangeA() {
    return mAllowedRangeA;
  }

  /**
   * @return the r1
   */
  public float getR1() {
    return mR1;
  }
  
}
