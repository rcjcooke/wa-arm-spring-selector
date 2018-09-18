import { TestBed } from '@angular/core/testing';

import { SpringSelectorService } from './spring-selector.service';

describe('SpringSelectorServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: SpringSelectorService = TestBed.get(SpringSelectorService);
    expect(service).toBeTruthy();
  });
});
