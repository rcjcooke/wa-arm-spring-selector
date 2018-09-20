/**
 * 
 */
package wa.arm.springselector;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A description of a specific Spring. Note that this is serialisable to and
 * from JSON.
 * 
 * @author Ray Cooke
 */
@XmlRootElement
public class Spring {

  // NOTE: THESE FIELDS HAVE TO BE PUBLIC FOR SERIALISATION PURPOSES
  // Spring details
  public String mOrderNum;
  public String mManufacturer;
  public double mRate;
  public double mRelevantLength;
  public double mMaximumForceUnderStaticLoad;
  // Scenario specific values
  public double mR2Min;
  public double mR2Max;
  public double mAMin;
  public double mAMax;

  /**
   * Empty constructor for de/serialisation 
   */
  public Spring() {
  }
  
  /**
   * @param orderNum
   * @param manufacturer
   * @param rate
   * @param relevantLength
   * @param maximumForceUnderStaticLoad
   * @param r2Min
   * @param r2Max
   * @param aMin
   * @param aMax
   */
  public Spring(String orderNum, String manufacturer, double rate, double relevantLength, double maximumForceUnderStaticLoad, double r2Min, double r2Max,
      double aMin, double aMax) {
    mOrderNum = orderNum;
    mManufacturer = manufacturer;
    mRate = rate;
    mRelevantLength = relevantLength;
    mMaximumForceUnderStaticLoad = maximumForceUnderStaticLoad;
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
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Spring)  {
      Spring s = (Spring) obj;
      return (s.mOrderNum.equals(mOrderNum) && s.mManufacturer.equals(mManufacturer));
    } else {
      return false;
    }
  }

  /*
   * (non-Javadoc)
   * 
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
