import { Component, OnInit, AfterViewInit, ViewChild, ElementRef } from '@angular/core';
import * as c3 from 'c3';
import { DataModelService } from '../data-model.service';
import { Spring } from '../spring';
import * as d3 from 'd3';
import { MatSlideToggleChange, MatSelectChange } from '@angular/material';

export interface AxisField {
  name: string;
  axisLabel: string;
}

@Component({
  selector: 'app-spring-selection-visualiser',
  templateUrl: './spring-selection-visualiser.component.html',
  styleUrls: ['./spring-selection-visualiser.component.css']
})
export class SpringSelectionVisualiserComponent implements OnInit, AfterViewInit {

  /* 
    Feature Toggle for turning on / off the "show all springs" toggle.
    Note: There are serious performance issues with using this at present, hence
    it being unavailable by default at this time.
  */
  private ft_showAllSpringsToggle: boolean = false;

  springChart: c3.ChartAPI;
  currentSpringIDs: string[] = []; // orderNum/manufacturer
  selectedSpring: Spring;
  springs: Spring[];
  maxMass: number;
  minMass: number;
  showAllSprings: boolean = false;

  axisFields: AxisField[] = [
    { name: 'mMaximumDeflection', axisLabel: 'Maximum Deflection / mm' },
    { name: 'mOptimumMaxLengthInScenario', axisLabel: 'Maximum Length / mm' }
  ];
  selectedAxisFieldName: string = this.axisFields[0].name;

  constructor(
    private dataModelService: DataModelService
  ) { }

  ngOnInit(): void {
  }

  public resizeChart() {
    if (this.springChart) this.springChart.resize();
  }

  ngAfterViewInit(): void {
    this.springChart = c3.generate({
      bindto: d3.select('#chart'),
      data: {
        xs: {
          springs: this.axisFields.find(f => f.name == this.selectedAxisFieldName).axisLabel
        },
        columns: [
        ],
        type: 'scatter',
        selection: {
          enabled: true,
          multiple: false
        },
        color: (color, d) => {
          // d will be 'id' when called for legends
          if (d.id) {
            var spring = this.getSpringForID(d.id);
            if (spring) {
              var massFactor = 1 - (spring.mMass - this.minMass) / (this.maxMass - this.minMass);
              let col = d3.interpolateWarm(massFactor);
              if (spring.mInScenario) {
                return col;
              } else {
                // Darken springs that aren't in the scenario
                return d3.rgb(col).darker(3).toString();
              }
            } else {
              return d3.interpolateWarm(0);
            }
          } else {
            return d3.interpolateWarm(0);
          }
        },
        onclick: (d, element) => {
          var spring = this.getSpringForID(d.id);
          if (spring == this.selectedSpring) {
            this.dataModelService.changeSelectedSpring(null);
          } else {
            this.dataModelService.changeSelectedSpring(spring);
          }
        }
      },
      axis: {
        x: {
          label: this.axisFields.find(f => f.name == this.selectedAxisFieldName).axisLabel,
          tick: {
            fit: false
          }
        },
        y: {
          label: 'Maximum Force under Static Load / N'
        }
      },
      legend: {
        show: false
      }
    });

    this.springChart.resize();

    this.dataModelService.springs$.subscribe(sps => this.regenerateChart(sps));

    this.dataModelService.selectedSpring$.subscribe(sp => {
      if (sp) {
        this.springChart.select([this.getSpringID(sp)], [0], true);
      } else {
        this.springChart.unselect();
      }
      this.selectedSpring = sp;
    });
  }

  onXAxisFieldChange(event: MatSelectChange) {
    console.log(this.selectedAxisFieldName);
    this.regenerateChart(this.springs);
  }

  onShowAllSpringsChange(event: MatSlideToggleChange) {
    this.dataModelService.changeShowAllSprings(this.showAllSprings);
    /* 
      Note: At the moment, this doesn't trigger a call to the underlying REST service. 
      Once it does, the round trip will result in the chart regenerating. At that point, 
      the call below should be removed.
    */
    this.regenerateChart(this.springs);
  }

  private regenerateChart(sps: Spring[]) {
    // Put together the new data set
    var cols = [];
    var xs = {};
    sps.forEach(s => {
      if (this.showAllSprings || (!this.showAllSprings && s.mInScenario)) {
        cols.push([this.getSpringID(s).concat("-rl"), s[this.selectedAxisFieldName]]);
        cols.push([this.getSpringID(s), s.mMaximumForceUnderStaticLoad]);
        xs[this.getSpringID(s)] = this.getSpringID(s).concat("-rl");
      }
    });

    // Work out what data sets need unloading - i.e. which springs are no longer present
    let springsToUnload = [];
    let springIDs = [];
    if (!this.showAllSprings) {
      springIDs = sps.filter(s => s.mInScenario).map(s => this.getSpringID(s));
      springsToUnload = this.currentSpringIDs.filter(curSpring => !springIDs.includes(curSpring));
      springsToUnload = [...springsToUnload, ...springsToUnload.map(s => s.concat("-rl"))];
    } else {
      springIDs = sps.map(s => this.getSpringID(s));
    }
    this.currentSpringIDs = springIDs;

    // Retain the full spring details for the colour function
    this.springs = sps;
    this.maxMass = Math.max.apply(Math, sps.map(sp => sp.mMass));
    this.minMass = Math.min.apply(Math, sps.map(sp => sp.mMass));

    // Update the axis labels
    this.springChart.axis.labels({
      x: this.axisFields.find(f => f.name == this.selectedAxisFieldName).axisLabel,
      y: 'Maximum Force under Static Load / N'
    })
    
    // Load up the new data
    this.springChart.load({
      xs: xs,
      columns: cols,
      unload: springsToUnload
    });
  }

  private getSpringForID(id: String): Spring {
    var manufacturer = id.split('/', 1)[0];
    var orderNum = id.substr(manufacturer.length + 1);
    return this.springs.find(spring => spring.mManufacturer == manufacturer && spring.mOrderNum == orderNum);
  }

  private getSpringID(s: Spring): string {
    return s.mManufacturer.concat('/').concat(s.mOrderNum);
  }
}
