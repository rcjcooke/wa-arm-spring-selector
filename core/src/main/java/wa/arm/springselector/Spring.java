/**
 * 
 */
package wa.arm.springselector;

/**
 * @author rcjco
 *
 */
public class Spring {

  // Spring details
  private String mOrderNum;
  private String mManufacturer;
  private double mRate;
  private double mRelevantLength;
  
  // Scenario specific values
  private double mR2Min;
  private double mR2Max;
  private double mAMin;
  private double mAMax;
  
  /**
   * @param orderNum
   * @param manufacturer
   * @param rate
   * @param relevantLength
   * @param r2Min
   * @param r2Max
   * @param aMin
   * @param aMax
   */
  public Spring(String orderNum, String manufacturer, double rate, double relevantLength, double r2Min, double r2Max,
      double aMin, double aMax) {
    mOrderNum = orderNum;
    mManufacturer = manufacturer;
    mRate = rate;
    mRelevantLength = relevantLength;
    mR2Min = r2Min;
    mR2Max = r2Max;
    mAMin = aMin;
    mAMax = aMax;
  }

  /**
   * @return the orderNum
   */
  public String getOrderNum() {
    return mOrderNum;
  }

  /**
   * @return the manufacturer
   */
  public String getManufacturer() {
    return mManufacturer;
  }

  /**
   * @return the rate
   */
  public double getRate() {
    return mRate;
  }

  /**
   * @return the relevantLength
   */
  public double getRelevantLength() {
    return mRelevantLength;
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

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("Spring[");
    sb.append(mOrderNum).append(',').append(mManufacturer);
    sb.append(',').append("rate=").append(mRate);
    sb.append(',').append("relevantLength=").append(mRelevantLength);
    sb.append(']');
    return sb.toString();
  }

}
