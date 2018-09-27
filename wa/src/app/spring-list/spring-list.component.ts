import { Component, OnInit, ViewChild } from '@angular/core';
import { Spring } from '../spring';
import { DataModelService } from '../data-model.service';
import { MatSort, MatTableDataSource } from '@angular/material';

@Component({
  selector: 'app-spring-list',
  templateUrl: './spring-list.component.html',
  styleUrls: ['./spring-list.component.css']
})
export class SpringListComponent implements OnInit {
  displayedColumns: string[] = ['mOrderNum', 'mManufacturer', 'mRate', 'mRelevantLength', 'mMaximumForceUnderStaticLoad', 'mMass', 'mWireDiameter', 'mOutsideDiameter'];  
  dataSource = new MatTableDataSource();
  selectedSpring: Spring;

  @ViewChild(MatSort) sort: MatSort;

  constructor(
    private dataModelService: DataModelService
  ) {}

  ngOnInit() {
    this.dataSource.sort = this.sort;
    this.dataModelService.springs$.subscribe(sps => this.dataSource.data = sps);
    this.dataModelService.selectedSpring$.subscribe(sp => this.selectedSpring = sp);
  }

  springSelected(spring: Spring) {
    if (this.selectedSpring == spring) {
      this.dataModelService.changeSelectedSpring(null);
    } else {
      this.dataModelService.changeSelectedSpring(spring);
    }
  }

}
