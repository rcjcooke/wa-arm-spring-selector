import { Component, OnInit, Inject } from '@angular/core';
import { Spring } from '../spring';
import { Scenario } from '../scenario';
import { DataModelService } from '../data-model.service';
import { MatSliderChange, MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'app-spring-detail-scenario',
  templateUrl: './spring-detail-scenario.component.html',
  styleUrls: ['./spring-detail-scenario.component.css']
})
export class SpringDetailScenarioComponent implements OnInit {

  spring: Spring;
  scenario: Scenario;

  aValue: number;
  r2Value: number;
  aValueLocked: boolean = true;
  r2ValueLocked: boolean = false;
  aZeroValue: number;
  r2ZeroValue: number;

  constructor(
    private dataModelService: DataModelService,
    public dialog: MatDialog
  ) {}

  ngOnInit() {
    this.dataModelService.selectedSpring$.subscribe(sp => {
      if (sp) {
        this.spring = sp;
        this.aValue = sp.mOptimumConnectionPointA;
        this.r2Value = sp.mMaxPayloadAnchorPointFactor/this.aValue;
        this.calculateZeroPayloadValues();
      }
    });
    this.dataModelService.scenario$.subscribe(scenario => {
      this.scenario = scenario;
      if (scenario.mDynamicBalancingRequired) {
        switch (scenario.mFixedVariable) {
          case "A":
            this.aValueLocked = true;
            this.r2ValueLocked = false;
            break;
          case "R":
            this.aValueLocked = false;
            this.r2ValueLocked = true;
            break;
          default:
            this.aValueLocked = false;
            this.r2ValueLocked = false;
          break;
        }
      } else {
        this.aValueLocked = true;
        this.r2ValueLocked = true;
      }
    });
  }

  openBigChartDialog() {
    const dialogRef = this.dialog.open(SpringConnectionChartDialog, {
      width: '800px',
      height: '500px',
      data: this.spring
    });
  }

  onR2ValueChange(event: MatSliderChange) {
    var newA = this.spring.mMaxPayloadAnchorPointFactor/event.value;
    if (this.aValue != newA) this.aValue = newA;
    this.calculateZeroPayloadValues();
  }

  onAValueChange(event: MatSliderChange) {
    var newR2 = this.spring.mMaxPayloadAnchorPointFactor/event.value;
    if (this.r2Value != newR2) this.r2Value = newR2;
    this.calculateZeroPayloadValues();
  }

  calculateZeroPayloadValues() {
    this.aZeroValue = this.spring.mZeroPayloadAnchorPointFactor/this.r2Value;
    this.r2ZeroValue = this.spring.mZeroPayloadAnchorPointFactor/this.aValue;
  }

  formatSliderLabel(value: number | null) {
    if (!value) {
      return 0;
    }
    return Math.round(value);
  }
}

@Component({
  selector: 'spring-connection-chart-dialog',
  templateUrl: 'spring-connection-chart-dialog.html',
})
export class SpringConnectionChartDialog {

  constructor(
    public dialogRef: MatDialogRef<SpringConnectionChartDialog>,
    @Inject(MAT_DIALOG_DATA) public data: Spring)
  {}

}
