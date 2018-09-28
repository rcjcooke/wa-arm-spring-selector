export class Scenario {
  mSystemGrams                  : number;
  mMassGrams                    : number;
  mNumberOfParallelSprings      : number;
  mMechanicalAdvantageZaehler   : number;
  mMechanicalAdvantageNenner    : number;
  mAllowedRangeR2MillimetersMin : number;
  mAllowedRangeR2MillimetersMax : number;
  mAllowedRangeAMillimetersMin  : number;
  mAllowedRangeAMillimetersMax  : number;
  mR1                           : number;
  mIncludeSpringMassInSystem    : boolean;
  mDynamicBalancingRequired     : boolean;
  mFixedVariable                : string;
}