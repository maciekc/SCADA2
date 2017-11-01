import { TestBed, inject } from '@angular/core/testing';

import { StateVariablesService } from './state-variables.service';

describe('StateVariablesService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [StateVariablesService]
    });
  });

  it('should be created', inject([StateVariablesService], (service: StateVariablesService) => {
    expect(service).toBeTruthy();
  }));
});
