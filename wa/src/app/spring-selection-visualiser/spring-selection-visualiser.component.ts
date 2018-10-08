import { Component, OnInit, AfterViewInit, ViewChild, ElementRef } from '@angular/core';
import * as c3 from 'c3';
import { DataModelService } from '../data-model.service';
import { Spring } from '../spring';
import * as d3 from 'd3';
import { MatMenuTrigger } from '@angular/material';

@Component({
  selector: 'app-spring-selection-visualiser',
  templateUrl: './spring-selection-visualiser.component.html',
  styleUrls: ['./spring-selection-visualiser.component.css']
})
export class SpringSelectionVisualiserComponent implements OnInit, AfterViewInit {

  springChart: c3.ChartAPI;
  currentSpringIDs: string[] = []; // orderNum/manufacturer
  selectedSpring: Spring;
  springs: Spring[];
  maxMass: number;
  minMass: number;

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
          springs: 'Maximum Deflection / mm'
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
              var massFactor = 1-(spring.mMass-this.minMass)/(this.maxMass - this.minMass);
              return d3.interpolateWarm(massFactor);
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
          label: 'Maximum Deflection / mm',
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
    
    this.dataModelService.springs$.subscribe(sps => {
      // Put together the new data set
      var cols = [];
      var xs = {};
      sps.forEach(s => {
        cols.push([this.getSpringID(s).concat("-rl"), s.mMaximumDeflection]);
        cols.push([this.getSpringID(s), s.mMaximumForceUnderStaticLoad]);
        xs[this.getSpringID(s)] = this.getSpringID(s).concat("-rl");
      });

      // Work out what data sets need unloading - i.e. which springs are no longer present
      let springIDs = sps.map(s => this.getSpringID(s));
      let springsToUnload = this.currentSpringIDs.filter(curSpring => !springIDs.includes(curSpring));
      springsToUnload = [...springsToUnload, ...springsToUnload.map(s => s.concat("-rl"))];
      this.currentSpringIDs = springIDs;

      // Retain the full spring details for the colour function
      this.springs = sps;
      this.maxMass = Math.max.apply(Math, sps.map(sp => sp.mMass));
      this.minMass = Math.min.apply(Math, sps.map(sp => sp.mMass));

      // Load up the new data
      this.springChart.load({
        xs: xs,
        columns: cols,
        unload: springsToUnload
      });
    });

    this.dataModelService.selectedSpring$.subscribe(sp => {
      if (sp) {
        this.springChart.select([this.getSpringID(sp)], [0], true);
      } else {
        this.springChart.unselect();
      }
      this.selectedSpring = sp;
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
