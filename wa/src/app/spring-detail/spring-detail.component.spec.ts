import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SpringDetailComponent } from './spring-detail.component';

describe('SpringDetailComponent', () => {
  let component: SpringDetailComponent;
  let fixture: ComponentFixture<SpringDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SpringDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SpringDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
