import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PriceHistoryDetailComponent } from './price-history-detail.component';

describe('Component Tests', () => {
  describe('PriceHistory Management Detail Component', () => {
    let comp: PriceHistoryDetailComponent;
    let fixture: ComponentFixture<PriceHistoryDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PriceHistoryDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ priceHistory: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PriceHistoryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PriceHistoryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load priceHistory on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.priceHistory).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
