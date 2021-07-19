import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ConsumeMaterialDetailComponent } from './consume-material-detail.component';

describe('Component Tests', () => {
  describe('ConsumeMaterial Management Detail Component', () => {
    let comp: ConsumeMaterialDetailComponent;
    let fixture: ComponentFixture<ConsumeMaterialDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ConsumeMaterialDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ consumeMaterial: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ConsumeMaterialDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ConsumeMaterialDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load consumeMaterial on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.consumeMaterial).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
