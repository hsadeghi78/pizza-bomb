import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FactorStatusHistoryDetailComponent } from './factor-status-history-detail.component';

describe('Component Tests', () => {
  describe('FactorStatusHistory Management Detail Component', () => {
    let comp: FactorStatusHistoryDetailComponent;
    let fixture: ComponentFixture<FactorStatusHistoryDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FactorStatusHistoryDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ factorStatusHistory: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FactorStatusHistoryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FactorStatusHistoryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load factorStatusHistory on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.factorStatusHistory).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
