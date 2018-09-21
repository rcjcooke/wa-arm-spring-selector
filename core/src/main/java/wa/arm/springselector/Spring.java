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
  public double mMass;
  public double mOutsideDiameter;
  public double mWireDiameter;
  
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
   * @param orderNum                    The manufacturer's order number
   * @param manufacturer                The manufacturer
   * @param rate                        The spring constant for the spring / Nm-1
   * @param relevantLength              The maximum length of the spring under
   *                                    it's maximum static force / m
   * @param maximumForceUnderStaticLoad The maximum static force that can be
   *                                    applied to the spring
   * @param mass                        The mass of the spring
   * @param wireDiameter                The diameter of the spring wire
   * @param outsideDiameter             The outside diameter of the spring
   */
  public Spring(String orderNum, String manufacturer, double rate, double relevantLength,
      double maximumForceUnderStaticLoad, double mass, double wireDiameter, double outsideDiameter) {
    mOrderNum = orderNum;
    mManufacturer = manufacturer;
    mRate = rate;
    mRelevantLength = relevantLength;
    mMaximumForceUnderStaticLoad = maximumForceUnderStaticLoad;
    mMass = mass;
    mOutsideDiameter = outsideDiameter;
    mWireDiameter = wireDiameter;
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

  /**
   * @return the maximumForceUnderStaticLoad
   */
  public double getMaximumForceUnderStaticLoad() {
    return mMaximumForceUnderStaticLoad;
  }

  /**
   * @return the mass
   */
  public double getMass() {
    return mMass;
  }

  /**
   * @return the outsideDiameter
   */
  public double getOutsideDiameter() {
    return mOutsideDiameter;
  }

  /**
   * @return the wireDiameter
   */
  public double getWireDiameter() {
    return mWireDiameter;
  }

  /**
   * @param r2Min the r2Min to set
   */
  public void setR2Min(double r2Min) {
    mR2Min = r2Min;
  }

  /**
   * @param r2Max the r2Max to set
   */
  public void setR2Max(double r2Max) {
    mR2Max = r2Max;
  }

  /**
   * @param aMin the aMin to set
   */
  public void setAMin(double aMin) {
    mAMin = aMin;
  }

  /**
   * @param aMax the aMax to set
   */
  public void setAMax(double aMax) {
    mAMax = aMax;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Spring) {
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
