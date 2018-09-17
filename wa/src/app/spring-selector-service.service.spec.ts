import { TestBed } from '@angular/core/testing';

import { SpringSelectorServiceService } from './spring-selector-service.service';

describe('SpringSelectorServiceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: SpringSelectorServiceService = TestBed.get(SpringSelectorServiceService);
    expect(service).toBeTruthy();
  });
});
