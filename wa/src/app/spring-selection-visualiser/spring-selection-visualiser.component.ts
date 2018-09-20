import { Component, OnInit, AfterViewInit } from '@angular/core';
import * as c3 from 'c3';
import { DataModelService } from '../data-model.service';
import { Spring } from '../spring';

@Component({
  selector: 'app-spring-selection-visualiser',
  templateUrl: './spring-selection-visualiser.component.html',
  styleUrls: ['./spring-selection-visualiser.component.css']
})
export class SpringSelectionVisualiserComponent implements OnInit, AfterViewInit {

  springChart: c3.ChartAPI;

  constructor(
    private dataModelService: DataModelService
  ) { }

  ngOnInit() {
  }

  ngAfterViewInit() {
    this.springChart = c3.generate({
      bindto: '#chart',
      data: {
        xs: {
          springs: 'Relevent Length / mm'
        },
        columns: [
        ],
        type: 'scatter'
      },
      axis: {
        x: {
            label: 'Relevent Length / mm',
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
      var cols = [];
      var xs = {};
      sps.forEach(s => {
        cols.push([s.mOrderNum.concat("-rl"), s.mRelevantLength]);
        cols.push([s.mOrderNum, s.mMaximumForceUnderStaticLoad]);
        xs[s.mOrderNum] = s.mOrderNum.concat("-rl");
      });

      this.springChart.load({
        xs: xs,
        columns: cols
      })
    })
  }
}
