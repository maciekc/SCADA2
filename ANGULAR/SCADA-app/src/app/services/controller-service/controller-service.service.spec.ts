import { TestBed, inject } from '@angular/core/testing';

import { ControllerServiceService } from './controller-service.service';

describe('ControllerServiceService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ControllerServiceService]
    });
  });

  it('should be created', inject([ControllerServiceService], (service: ControllerServiceService) => {
    expect(service).toBeTruthy();
  }));
});
