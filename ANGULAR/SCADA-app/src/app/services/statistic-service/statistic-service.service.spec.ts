import { TestBed, inject } from '@angular/core/testing';

import { StatisticServiceService } from './statistic-service.service';

describe('StatisticServiceService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [StatisticServiceService]
    });
  });

  it('should be created', inject([StatisticServiceService], (service: StatisticServiceService) => {
    expect(service).toBeTruthy();
  }));
});
