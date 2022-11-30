import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CaffCardComponent } from './caff-card.component';

describe('CaffCardComponent', () => {
  let component: CaffCardComponent;
  let fixture: ComponentFixture<CaffCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CaffCardComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CaffCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
