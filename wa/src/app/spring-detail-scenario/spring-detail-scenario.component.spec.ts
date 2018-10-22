import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SpringDetailScenarioComponent } from './spring-detail-scenario.component';

describe('SpringDetailScenarioComponent', () => {
  let component: SpringDetailScenarioComponent;
  let fixture: ComponentFixture<SpringDetailScenarioComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SpringDetailScenarioComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SpringDetailScenarioComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
