import { Component, OnInit } from '@angular/core';
import { Scenario } from '../scenario'
import { SpringSelectorService } from '../spring-selector.service'
import { Spring } from '../spring';
import { DataModelService } from '../data-model.service';

@Component({
  selector: 'app-scenario-selector',
  templateUrl: './scenario-selector.component.html',
  styleUrls: ['./scenario-selector.component.css']
})
export class ScenarioSelectorComponent implements OnInit {
  
  // A pre-populated default scenario for ease of use
  scenario: Scenario = {
    mMassGrams: 15000,
    mNumberOfParallelSprings: 1,
    mMechanicalAdvantageZaehler: 1,
    mMechanicalAdvantageNenner: 1,
    mAllowedRangeR2MillimetersMin: 100,
    mAllowedRangeR2MillimetersMax: 200,
    mAllowedRangeAMillimetersMin: 100,
    mAllowedRangeAMillimetersMax: 200,
    mR1: 1200
  };

  constructor(
    private springSelectorService: SpringSelectorService,
    private dataModelService: DataModelService
  ) { }

  ngOnInit() {
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
    this.springSelectorService.findSprings(this.scenario).subscribe(sps => this.dataModelService.changeSprings(sps));
  }
}
