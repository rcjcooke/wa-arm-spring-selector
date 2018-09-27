import { Component, OnInit, AfterViewInit, ViewChild, ElementRef } from '@angular/core';
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
  currentSpringIDs: string[] = []; // orderNum/manufacturer
  selectedSpring: Spring;

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
          springs: 'Relevent Length / mm'
        },
        columns: [
        ],
        type: 'scatter',
        selection: {
          enabled: true,
          multiple: false
        },
        color: function (color, d) {
          // d will be 'id' when called for legends
          return d3.rgb("#c2185b");
        },
        onclick: (d, element) => {
          var springIDElements = d.id.split('/');
          var spring = this.dataModelService.getSpring(springIDElements[0], springIDElements[1]);
          if (spring == this.selectedSpring) {
            this.dataModelService.changeSelectedSpring(null);
          } else {
            this.dataModelService.changeSelectedSpring(spring);
          }
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
    
    this.dataModelService.springs$.subscribe(sps => {
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

  private getSpringID(s: Spring): string {
    return s.mManufacturer.concat('/').concat(s.mOrderNum);
  }
}
