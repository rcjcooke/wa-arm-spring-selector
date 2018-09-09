package wa.arm.springselector;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A description of the mechanical structure and parameters relevant to spring
 * selection. Note: This class is set up to be converted to and from JSON.
 * 
 * @author Ray Cooke
 */
@XmlRootElement
public class Scenario {

  // System mass + payload mass [g]
  private float mMassGrams = 1000;
  // Number of equivalent parallel springs
  private int mNumberOfParallelSprings = 1;
  private float mMechanicalAdvantageZaehler = 1; // TODO
  private float mMechanicalAdvantageNenner = 1; // TODO
  // Distance spring connection on lever to Pivot [mm]
  private float[] mAllowedRangeR2Millimeters = { 100, 200 };
  // Distance spring connection on fixed y-Axis to Pivot [mm]
  private float[] mAllowedRangeAMillimeters = { 100, 200 };
  // Lever => distance CoM to Pivot [mm]
  private float mR1 = 1000;

  /**
   * @param massGrams                  - System mass + payload mass [g]
   * @param numberOfParallelSprings    - Number of equivalent parallel springs
   * @param mechanicalAdvantageZaehler
   * @param mechanicalAdvantageNenner
   * @param allowedRangeR2Millimeters  - Distance spring connection on lever to
   *                                   Pivot {min, max} [mm]
   * @param allowedRangeAMillimeters   - Distance spring connection on fixed
   *                                   y-Axis to Pivot {min, max} [mm]
   * @param r1                         - Lever => distance CoM to Pivot [mm]
   */
  public Scenario(float massGrams, int numberOfParallelSprings, float mechanicalAdvantageZaehler,
      float mechanicalAdvantageNenner, float[] allowedRangeR2Millimeters, float[] allowedRangeAMillimeters, float r1) {
    mMassGrams = massGrams;
    mNumberOfParallelSprings = numberOfParallelSprings;
    mMechanicalAdvantageZaehler = mechanicalAdvantageZaehler;
    mMechanicalAdvantageNenner = mechanicalAdvantageNenner;
    mAllowedRangeR2Millimeters = allowedRangeR2Millimeters;
    mAllowedRangeAMillimeters = allowedRangeAMillimeters;
    mR1 = r1;
  }

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
    return mMassGrams;
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
    return mAllowedRangeR2Millimeters;
  }

  /**
   * @return the allowedRangeA
   */
  public float[] getAllowedRangeA() {
    return mAllowedRangeAMillimeters;
  }

  /**
   * @return the r1
   */
  public float getR1() {
    return mR1;
  }

}
