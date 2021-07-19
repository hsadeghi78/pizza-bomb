import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FactorDetailComponent } from './factor-detail.component';

describe('Component Tests', () => {
  describe('Factor Management Detail Component', () => {
    let comp: FactorDetailComponent;
    let fixture: ComponentFixture<FactorDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FactorDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ factor: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FactorDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FactorDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load factor on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.factor).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
