import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SpringListComponent } from './spring-list.component';

describe('SpringListComponent', () => {
  let component: SpringListComponent;
  let fixture: ComponentFixture<SpringListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SpringListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SpringListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
