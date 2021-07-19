jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ConsumeMaterialService } from '../service/consume-material.service';
import { IConsumeMaterial, ConsumeMaterial } from '../consume-material.model';
import { IFood } from 'app/entities/food/food.model';
import { FoodService } from 'app/entities/food/service/food.service';

import { ConsumeMaterialUpdateComponent } from './consume-material-update.component';

describe('Component Tests', () => {
  describe('ConsumeMaterial Management Update Component', () => {
    let comp: ConsumeMaterialUpdateComponent;
    let fixture: ComponentFixture<ConsumeMaterialUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let consumeMaterialService: ConsumeMaterialService;
    let foodService: FoodService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ConsumeMaterialUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ConsumeMaterialUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ConsumeMaterialUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      consumeMaterialService = TestBed.inject(ConsumeMaterialService);
      foodService = TestBed.inject(FoodService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Food query and add missing value', () => {
        const consumeMaterial: IConsumeMaterial = { id: 456 };
        const food: IFood = { id: 17884 };
        consumeMaterial.food = food;

        const foodCollection: IFood[] = [{ id: 11731 }];
        spyOn(foodService, 'query').and.returnValue(of(new HttpResponse({ body: foodCollection })));
        const additionalFoods = [food];
        const expectedCollection: IFood[] = [...additionalFoods, ...foodCollection];
        spyOn(foodService, 'addFoodToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ consumeMaterial });
        comp.ngOnInit();

        expect(foodService.query).toHaveBeenCalled();
        expect(foodService.addFoodToCollectionIfMissing).toHaveBeenCalledWith(foodCollection, ...additionalFoods);
        expect(comp.foodsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const consumeMaterial: IConsumeMaterial = { id: 456 };
        const food: IFood = { id: 53564 };
        consumeMaterial.food = food;

        activatedRoute.data = of({ consumeMaterial });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(consumeMaterial));
        expect(comp.foodsSharedCollection).toContain(food);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const consumeMaterial = { id: 123 };
        spyOn(consumeMaterialService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ consumeMaterial });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: consumeMaterial }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(consumeMaterialService.update).toHaveBeenCalledWith(consumeMaterial);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const consumeMaterial = new ConsumeMaterial();
        spyOn(consumeMaterialService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ consumeMaterial });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: consumeMaterial }));
        saveSubject.complete();

        // THEN
        expect(consumeMaterialService.create).toHaveBeenCalledWith(consumeMaterial);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const consumeMaterial = { id: 123 };
        spyOn(consumeMaterialService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ consumeMaterial });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(consumeMaterialService.update).toHaveBeenCalledWith(consumeMaterial);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackFoodById', () => {
        it('Should return tracked Food primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackFoodById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
