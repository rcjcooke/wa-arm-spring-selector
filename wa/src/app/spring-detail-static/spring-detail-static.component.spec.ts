import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SpringDetailStaticComponent } from './spring-detail-static.component';

describe('SpringDetailStaticComponent', () => {
  let component: SpringDetailStaticComponent;
  let fixture: ComponentFixture<SpringDetailStaticComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SpringDetailStaticComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SpringDetailStaticComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
