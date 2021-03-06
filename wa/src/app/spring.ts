export class Spring {
  // Note: Values required for reflecton to work properly for Spring table (spring-list component)
  mOrderNum: string = "";
  mManufacturer: string = "";
  mRate: number = 0;
  mMaximumDeflection: number = 0;
  mMaximumForceUnderStaticLoad: number = 0;
  mMass: number = 0;
  mOutsideDiameter: number = 0;
  mWireDiameter: number = 0;
  mUnstressedLength: number = 0;

  mR2Min: number = 0;
  mR2Max: number = 0;
  mAMin: number = 0;
  mAMax: number = 0;
  mSpringCPMin: number = 0;
  mSpringCPMax: number = 0;
  mInScenario: boolean = false;

  mMaxPayloadAnchorPointFactor: number = 0;
  mZeroPayloadAnchorPointFactor: number = 0;
  mOptimumConnectionPointA: number = 0;
  mOptimumMaxLengthInScenario: number = 0;
}