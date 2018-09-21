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
  displayedColumns: string[] = ['mOrderNum', 'mManufacturer', 'mRate', 'mRelevantLength', 'mMaximumForceUnderStaticLoad', 'mMass'];  
  dataSource = new MatTableDataSource();

  @ViewChild(MatSort) sort: MatSort;

  constructor(
    private dataModelService: DataModelService
  ) {}

  ngOnInit() {
    this.dataSource.sort = this.sort;
    this.dataModelService.springs.subscribe(sps => this.dataSource.data = sps);
  }

}
