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
        ]
      }
    });
    
    this.dataModelService.springs.subscribe({
      next(sps) {
        this.springChart.load({
          columns: [
            ["Relevent Length / mm", ...sps.map(a => a.mRelevantLength)],
            ["springs", ...sps.map(a => a.mMaximumForceUnderStaticLoad)]
          ]
        })
      }
    })
  }
}
