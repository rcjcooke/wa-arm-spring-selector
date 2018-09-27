import { Component, OnInit, EventEmitter, Output, AfterViewInit } from '@angular/core';
import { DataModelService } from '../data-model.service';

import { Spring } from '../spring';

@Component({
  selector: 'app-spring-detail',
  templateUrl: './spring-detail.component.html',
  styleUrls: ['./spring-detail.component.css']
})
export class SpringDetailComponent implements OnInit, AfterViewInit {

  @Output() viewInitialised = new EventEmitter<boolean>();

  spring: Spring;

  constructor(
    private dataModelService: DataModelService
  ) {}

  ngOnInit() {
    this.dataModelService.selectedSpring$.subscribe(sp => this.spring = sp);
  }

  ngAfterViewInit(): void {
    this.viewInitialised.emit(true);
  }

}
