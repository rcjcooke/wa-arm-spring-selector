import { Component, OnInit, Input } from '@angular/core';
import { Spring } from '../spring';
import { DataModelService } from '../data-model.service';

@Component({
  selector: 'app-spring-list',
  templateUrl: './spring-list.component.html',
  styleUrls: ['./spring-list.component.css']
})
export class SpringListComponent implements OnInit {
  displayedColumns: string[] = ['mOrderNum', 'mManufacturer', 'mRate', 'mRelevantLength', 'mMaximumForceUnderStaticLoad, mMass'];
  springs: Spring[];
  
  constructor(
    private dataModelService: DataModelService
  ) {}

  ngOnInit() {
    this.dataModelService.springs.subscribe(sps => this.springs = sps);
  }

}
