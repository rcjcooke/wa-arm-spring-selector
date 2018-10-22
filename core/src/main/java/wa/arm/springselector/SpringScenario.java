package wa.arm.springselector;

/**
 * A container for calculated spring values specific to a scenario
 * 
 * @author Ray Cooke
 */
public class SpringScenario {

  public double mR2Min;
  public double mR2Max;
  public double mAMin;
  public double mAMax;
  
  public double mPayloadAnchorPointFactor;
  public double mOptimumConnectionPointA;
  public double mSpringCPMin;
  public double mSpringCPMax;

  /**
   * @param r2Min
   * @param r2Max
   * @param aMin
   * @param aMax
   * @param payloadAnchorPointFactor
   * @param optimumConnectionPointA The optimum connection point A to give the smallest maximum spring deflection
   * @param springCPMin The minimum theoretical value for the spring's connection point (A or R2 - it's symmetrical)
   * @param springCPMax The maximum theoretical value for the spring's connection point (A or R2 - it's symmetrical)
   */
  public SpringScenario(double r2Min, double r2Max, double aMin, double aMax, double payloadAnchorPointFactor, double optimumConnectionPointA, double springCPMin, double springCPMax) {
    mR2Min = r2Min;
    mR2Max = r2Max;
    mAMin = aMin;
    mAMax = aMax;
    mPayloadAnchorPointFactor = payloadAnchorPointFactor;
    mOptimumConnectionPointA = optimumConnectionPointA;
    mSpringCPMin = springCPMin;
    mSpringCPMax = springCPMax;
  }

  /**
   * @return the r2Min
   */
  public double getR2Min() {
    return mR2Min;
  }

  /**
   * @return the r2Max
   */
  public double getR2Max() {
    return mR2Max;
  }

  /**
   * @return the aMin
   */
  public double getAMin() {
    return mAMin;
  }

  /**
   * @return the aMax
   */
  public double getAMax() {
    return mAMax;
  }

  /**
   * @return the payloadAnchorPointFactor
   */
  public double getPayloadAnchorPointFactor() {
    return mPayloadAnchorPointFactor;
  }

  /**
   * @return the optimumConnectionPointA
   */
  public double getOptimumConnectionPointA() {
    return mOptimumConnectionPointA;
  }

  /**
   * @return the springCPMin
   */
  public double getSpringCPMin() {
    return mSpringCPMin;
  }

  /**
   * @return the springCPMax
   */
  public double getSpringCPMax() {
    return mSpringCPMax;
  }
  
}
