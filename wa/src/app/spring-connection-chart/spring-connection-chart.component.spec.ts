import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SpringConnectionChartComponent } from './spring-connection-chart.component';

describe('SpringConnectionChartComponent', () => {
  let component: SpringConnectionChartComponent;
  let fixture: ComponentFixture<SpringConnectionChartComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SpringConnectionChartComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SpringConnectionChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
