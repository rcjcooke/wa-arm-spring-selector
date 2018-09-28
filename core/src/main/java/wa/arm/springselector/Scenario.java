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

  // NOTE: THESE FIELDS HAVE TO BE PUBLIC FOR SERIALISATION PURPOSES
  // System mass / g
  public float mSystemGrams;
  // Payload mass / g
  public float mMassGrams;
  // Number of equivalent parallel springs
  public int mNumberOfParallelSprings;
  public float mMechanicalAdvantageZaehler; // TODO
  public float mMechanicalAdvantageNenner; // TODO
  // Distance spring connection on lever to Pivot [mm]
  public float mAllowedRangeR2MillimetersMin;
  public float mAllowedRangeR2MillimetersMax;
  // Distance spring connection on fixed y-Axis to Pivot [mm]
  public float mAllowedRangeAMillimetersMin;
  public float mAllowedRangeAMillimetersMax;
  // Lever => distance CoM to Pivot [mm]
  public float mR1;
  // True if the spring(s) are to be part of the system being balanced
  public boolean mIncludeSpringMassInSystem;
  // True if the system needs to be able to balance with massPerSpring=0 as well
  // as massPerSpring
  public boolean mDynamicBalancingRequired;
  // If dynamic balancing is required, then which point will be fixed (A, R, N)
  // TODO: See if enums are supported for JSON serialisation
  public char mFixedVariable;

  /**
   * Empty constructor for de/serialisation
   */
  public Scenario() {
  }

  /**
   * @param systemGrams                  System mass (excluding the spring mass if
   *                                     relevant. See includeSpringMassInSystem.)
   *                                     [g]
   * @param massGrams                    Payload mass (excluding the spring mass
   *                                     if relevant. See
   *                                     includeSpringMassInSystem.) [g]
   * @param numberOfParallelSprings      Number of equivalent parallel springs
   * @param mechanicalAdvantageZaehler
   * @param mechanicalAdvantageNenner
   * @param allowedRangeR2MillimetersMin Minimum distance spring connection on
   *                                     lever to Pivot [mm]
   * @param allowedRangeR2MillimetersMax Maximum distance spring connection on
   *                                     lever to Pivot [mm]
   * @param allowedRangeAMillimetersMin  Minimum distance spring connection on
   *                                     fixed y-Axis to Pivot [mm]
   * @param allowedRangeAMillimetersMax  Maximum distance spring connection on
   *                                     fixed y-Axis to Pivot [mm]
   * @param r1                           Lever => distance CoM to Pivot [mm]
   * @param includeSpringMassInSystem    True if the spring(s) are to be part of
   *                                     the system being balanced
   * @param dynamicBalancingRequired     True if the system needs to be able to
   *                                     balance with massPerSpring=0 as well as
   *                                     massPerSpring
   */
  public Scenario(float systemGrams, float massGrams, int numberOfParallelSprings, float mechanicalAdvantageZaehler,
      float mechanicalAdvantageNenner, float allowedRangeR2MillimetersMin, float allowedRangeR2MillimetersMax,
      float allowedRangeAMillimetersMin, float allowedRangeAMillimetersMax, float r1, boolean includeSpringMassInSystem,
      boolean dynamicBalancingRequired, char fixedVariable) {
    mSystemGrams = systemGrams;
    mMassGrams = massGrams;
    mNumberOfParallelSprings = numberOfParallelSprings;
    mMechanicalAdvantageZaehler = mechanicalAdvantageZaehler;
    mMechanicalAdvantageNenner = mechanicalAdvantageNenner;
    mAllowedRangeR2MillimetersMin = allowedRangeR2MillimetersMin;
    mAllowedRangeR2MillimetersMax = allowedRangeR2MillimetersMax;
    mAllowedRangeAMillimetersMin = allowedRangeAMillimetersMin;
    mAllowedRangeAMillimetersMax = allowedRangeAMillimetersMax;
    mR1 = r1;
    mIncludeSpringMassInSystem = includeSpringMassInSystem;
    mDynamicBalancingRequired = dynamicBalancingRequired;
    mFixedVariable = fixedVariable;
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

  /**
   * @return the systemGrams
   */
  public float getSystemGrams() {
    return mSystemGrams;
  }
  
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
    return new float[] { mAllowedRangeR2MillimetersMin, mAllowedRangeR2MillimetersMax };
  }

  /**
   * @return the allowedRangeA
   */
  public float[] getAllowedRangeA() {
    return new float[] { mAllowedRangeAMillimetersMin, mAllowedRangeAMillimetersMax };
  }

  /**
   * @return the r1
   */
  public float getR1() {
    return mR1;
  }

  /**
   * @return true if the spring(s) are to be part of the system being balanced
   */
  public boolean includeSpringMassInSystem() {
    return mIncludeSpringMassInSystem;
  }

  /**
   * @return True if the system needs to be able to balance with massPerSpring=0
   *         as well as massPerSpring
   */
  public boolean isDynamicBalancingRequired() {
    return mDynamicBalancingRequired;
  }

  /**
   * @return the fixedVariable
   */
  public char getFixedVariable() {
    return mFixedVariable;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object arg0) {
    if (arg0 instanceof Scenario) {
      Scenario other = (Scenario) arg0;
      if (other.mSystemGrams != mSystemGrams)
        return false;
      if (other.mMassGrams != mMassGrams)
        return false;
      if (other.mNumberOfParallelSprings != mNumberOfParallelSprings)
        return false;
      if (other.mMechanicalAdvantageNenner != mMechanicalAdvantageNenner)
        return false;
      if (other.mMechanicalAdvantageZaehler != mMechanicalAdvantageZaehler)
        return false;
      if (other.mAllowedRangeAMillimetersMax != mAllowedRangeAMillimetersMax)
        return false;
      if (other.mAllowedRangeAMillimetersMin != mAllowedRangeAMillimetersMin)
        return false;
      if (other.mAllowedRangeR2MillimetersMax != mAllowedRangeR2MillimetersMax)
        return false;
      if (other.mAllowedRangeR2MillimetersMin != mAllowedRangeR2MillimetersMin)
        return false;
      if (other.mAllowedRangeR2MillimetersMin != mAllowedRangeR2MillimetersMin)
        return false;
      if (other.mR1 != mR1)
        return false;
      if (other.mIncludeSpringMassInSystem != mIncludeSpringMassInSystem)
        return false;
      if (other.mDynamicBalancingRequired != mDynamicBalancingRequired)
        return false;
      if (other.mFixedVariable != mFixedVariable)
        return false;
      return true;
    } else {
      return false;
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("Scenario[");
    sb.append("system=").append(mSystemGrams);
    sb.append(',').append("payload=").append(mMassGrams);
    sb.append(',').append("parallelSprings=").append(mNumberOfParallelSprings);
    sb.append(',').append("MANum=").append(mMechanicalAdvantageNenner);
    sb.append(',').append("MADen=").append(mMechanicalAdvantageZaehler);
    sb.append(',').append("AMax=").append(mAllowedRangeAMillimetersMax);
    sb.append(',').append("AMin=").append(mAllowedRangeAMillimetersMin);
    sb.append(',').append("R2Max=").append(mAllowedRangeR2MillimetersMax);
    sb.append(',').append("R2Min=").append(mAllowedRangeR2MillimetersMin);
    sb.append(',').append("R1=").append(mR1);
    sb.append(',').append("includeSpringMass=").append(mIncludeSpringMassInSystem);
    sb.append(',').append("dynamicBalancing=").append(mDynamicBalancingRequired);
    sb.append(',').append("fixedPosition=").append(mFixedVariable);
    sb.append(']');
    return sb.toString();
  }
  
  
}
