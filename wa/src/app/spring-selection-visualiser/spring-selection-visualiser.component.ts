import { Component, OnInit, AfterViewInit } from '@angular/core';
import * as c3 from 'c3';
import { DataModelService } from '../data-model.service';
import { Spring } from '../spring';
import * as d3 from 'd3';

@Component({
  selector: 'app-spring-selection-visualiser',
  templateUrl: './spring-selection-visualiser.component.html',
  styleUrls: ['./spring-selection-visualiser.component.css']
})
export class SpringSelectionVisualiserComponent implements OnInit, AfterViewInit {

  springChart: c3.ChartAPI;
  currentSpringIDs: string[] = []; // orderNum-manufacturer

  constructor(
    private dataModelService: DataModelService
  ) { }

  ngOnInit() {
  }

  ngAfterViewInit() {
    this.springChart = c3.generate({
      bindto: '#chart',
      size: {
        height: 544
      },
      data: {
        xs: {
          springs: 'Relevent Length / mm'
        },
        columns: [
        ],
        type: 'scatter',
        color: function (color, d) {
          // d will be 'id' when called for legends
          return d3.rgb("#c2185b");
        }
      },
      axis: {
        x: {
          label: 'Maximum Length / mm',
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
    
    this.dataModelService.springs.subscribe(sps => {
      // Put together the new data set
      var cols = [];
      var xs = {};
      sps.forEach(s => {
        cols.push([this.getSpringID(s).concat("-rl"), s.mRelevantLength]);
        cols.push([this.getSpringID(s), s.mMaximumForceUnderStaticLoad]);
        xs[this.getSpringID(s)] = this.getSpringID(s).concat("-rl");
      });

      // Work out what data sets need unloading - i.e. which springs are no longer present
      let springIDs = sps.map(s => this.getSpringID(s));
      let springsToUnload = this.currentSpringIDs.filter(curSpring => !springIDs.includes(curSpring));
      springsToUnload = [...springsToUnload, ...springsToUnload.map(s => s.concat("-rl"))];
      this.currentSpringIDs = springIDs;

      this.springChart.load({
        xs: xs,
        columns: cols,
        unload: springsToUnload
      });
    })
  }

  private getSpringID(s: Spring): string {
    return s.mManufacturer.concat('/').concat(s.mOrderNum);
  }
}
