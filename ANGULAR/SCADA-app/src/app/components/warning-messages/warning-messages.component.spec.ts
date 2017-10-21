import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WarningMessagesComponent } from './warning-messages.component';

describe('WarningMessagesComponent', () => {
  let component: WarningMessagesComponent;
  let fixture: ComponentFixture<WarningMessagesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WarningMessagesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WarningMessagesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
