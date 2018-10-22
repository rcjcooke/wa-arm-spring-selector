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

  /**
   * @param r2Min
   * @param r2Max
   * @param aMin
   * @param aMax
   * @param payloadAnchorPointFactor
   * @param optimumConnectionPointA The optimum connection point A to give the smallest maximum spring deflection
   */
  public SpringScenario(double r2Min, double r2Max, double aMin, double aMax, double payloadAnchorPointFactor, double optimumConnectionPointA) {
    mR2Min = r2Min;
    mR2Max = r2Max;
    mAMin = aMin;
    mAMax = aMax;
    mPayloadAnchorPointFactor = payloadAnchorPointFactor;
    mOptimumConnectionPointA = optimumConnectionPointA;
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
  
}
