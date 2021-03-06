import { Component, OnInit } from '@angular/core';
import { Scenario } from '../scenario'
import { SpringSelectorService } from '../spring-selector.service'
import { Spring } from '../spring';
import { DataModelService } from '../data-model.service';
import { MatButtonToggleChange } from '@angular/material/button-toggle';

@Component({
  selector: 'app-scenario-selector',
  templateUrl: './scenario-selector.component.html',
  styleUrls: ['./scenario-selector.component.css']
})
export class ScenarioSelectorComponent implements OnInit {
  
  // A pre-populated default scenario for ease of use
  scenario: Scenario = {
    mSystemGrams: 2500,
    mMassGrams: 15000,
    mNumberOfParallelSprings: 1,
    mMechanicalAdvantageZaehler: 1,
    mMechanicalAdvantageNenner: 1,
    mAllowedRangeR2MillimetersMin: 50,
    mAllowedRangeR2MillimetersMax: 700,
    mAllowedRangeAMillimetersMin: 50,
    mAllowedRangeAMillimetersMax: 150,
    mR1: 750,
    mIncludeSpringMassInSystem: true,
    mDynamicBalancingRequired: true,
    mFixedVariable: 'A'
  };

  fixedPositionA: boolean = true;
  fixedPositionR2: boolean = false;

  showSpinner: boolean = false;

  constructor(
    private springSelectorService: SpringSelectorService,
    private dataModelService: DataModelService
  ) { }

  valueChanged(event: MatButtonToggleChange) {
    // It's ok to have both selected, but not neither
    // If we deselect one, we have to select the other
    if (event.value == "movePositionR2") {
      this.fixedPositionR2 = !event.source.checked;
      if (this.fixedPositionR2) this.fixedPositionA = false;
    } else if (event.value == "movePositionA") {
      this.fixedPositionA = !event.source.checked;
      if (this.fixedPositionA) this.fixedPositionR2 = false;
    }
    // Update the scenario
    this.scenario.mFixedVariable = (!this.fixedPositionA && !this.fixedPositionR2) ? 'N' : this.fixedPositionA ? 'A' : 'R'; 
  }

  ngOnInit() {
    this.findSprings();
  }

  /*
  Request should look something like the following:

    1 > POST http://localhost:9998/springselector/runscenario
    1 > accept: application/json
    1 > connection: keep-alive
    1 > content-length: 281
    1 > content-type: application/json
    1 > host: localhost:9998
    1 > user-agent: Jersey/2.27 (HttpUrlConnection 1.8.0_181)
    {"mMassGrams":15000.0,"mNumberOfParallelSprings":1,"mMechanicalAdvantageZaehler":1.0,"mMechanicalAdvantageNenner":1.0,"mAllowedRangeR2MillimetersMin":100.0,"mAllowedRangeR2MillimetersMax":200.0,"mAllowedRangeAMillimetersMin":100.0,"mAllowedRangeAMillimetersMax":200.0,"mR1":1200.0}

  PRETTY VERSION:

  {
    "mMassGrams": 15000.0,
    "mNumberOfParallelSprings": 1,
    "mMechanicalAdvantageZaehler": 1.0,
    "mMechanicalAdvantageNenner": 1.0,
    "mAllowedRangeR2MillimetersMin": 100.0,
    "mAllowedRangeR2MillimetersMax": 200.0,
    "mAllowedRangeAMillimetersMin": 100.0,
    "mAllowedRangeAMillimetersMax": 200.0,
    "mR1": 1200.0
  }
  */

  findSprings() {
    this.showSpinner = true;
    this.springSelectorService.findSprings(this.scenario, this.dataModelService.isShowAllSprings()).subscribe(sps => {
      this.dataModelService.changeSprings(sps, this.scenario);
      this.showSpinner = false;
    });
  }
}
