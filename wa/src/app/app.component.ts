import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { DataModelService } from './data-model.service';
import { Spring } from './spring';
import { SpringSelectionVisualiserComponent } from './spring-selection-visualiser/spring-selection-visualiser.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  selectedSpring: Spring;

  @ViewChild(SpringSelectionVisualiserComponent) 
  springSelectionVisualiserComponent: SpringSelectionVisualiserComponent

  constructor(
    private dataModelService: DataModelService
  ) {}

  ngOnInit() {
    this.dataModelService.selectedSpring$.subscribe(sp => {
      this.selectedSpring = sp;
    });
  }

  resizeChartCellEH() {
    if (this.springSelectionVisualiserComponent) this.springSelectionVisualiserComponent.resizeChart();
  }

}
