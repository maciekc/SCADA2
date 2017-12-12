import { TestBed, inject } from '@angular/core/testing';

import { AndonService } from './andon.service';

describe('AndonService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AndonService]
    });
  });

  it('should be created', inject([AndonService], (service: AndonService) => {
    expect(service).toBeTruthy();
  }));
});
