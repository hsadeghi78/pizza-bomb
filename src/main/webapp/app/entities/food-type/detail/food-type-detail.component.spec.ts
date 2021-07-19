import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FoodTypeDetailComponent } from './food-type-detail.component';

describe('Component Tests', () => {
  describe('FoodType Management Detail Component', () => {
    let comp: FoodTypeDetailComponent;
    let fixture: ComponentFixture<FoodTypeDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FoodTypeDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ foodType: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FoodTypeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FoodTypeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load foodType on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.foodType).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
