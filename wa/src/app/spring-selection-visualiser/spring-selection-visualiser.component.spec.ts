import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SpringSelectionVisualiserComponent } from './spring-selection-visualiser.component';

describe('SpringSelectionVisualiserComponent', () => {
  let component: SpringSelectionVisualiserComponent;
  let fixture: ComponentFixture<SpringSelectionVisualiserComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SpringSelectionVisualiserComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SpringSelectionVisualiserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
