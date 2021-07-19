import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FactorItemDetailComponent } from './factor-item-detail.component';

describe('Component Tests', () => {
  describe('FactorItem Management Detail Component', () => {
    let comp: FactorItemDetailComponent;
    let fixture: ComponentFixture<FactorItemDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FactorItemDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ factorItem: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FactorItemDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FactorItemDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load factorItem on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.factorItem).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
