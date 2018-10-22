import { Component, OnInit } from '@angular/core';
import { DataModelService } from '../data-model.service';
import { Spring } from '../spring';

@Component({
  selector: 'app-spring-detail-static',
  templateUrl: './spring-detail-static.component.html',
  styleUrls: ['./spring-detail-static.component.css']
})
export class SpringDetailStaticComponent implements OnInit {

  spring: Spring;
    
  constructor(
    private dataModelService: DataModelService
  ) {}
  
  ngOnInit() {
    this.dataModelService.selectedSpring$.subscribe(sp => {
      if (sp) {
        this.spring = sp;
      }
    });
  }

}
