jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FactorItemService } from '../service/factor-item.service';
import { IFactorItem, FactorItem } from '../factor-item.model';
import { IFood } from 'app/entities/food/food.model';
import { FoodService } from 'app/entities/food/service/food.service';
import { IFactor } from 'app/entities/factor/factor.model';
import { FactorService } from 'app/entities/factor/service/factor.service';

import { FactorItemUpdateComponent } from './factor-item-update.component';

describe('Component Tests', () => {
  describe('FactorItem Management Update Component', () => {
    let comp: FactorItemUpdateComponent;
    let fixture: ComponentFixture<FactorItemUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let factorItemService: FactorItemService;
    let foodService: FoodService;
    let factorService: FactorService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FactorItemUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FactorItemUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FactorItemUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      factorItemService = TestBed.inject(FactorItemService);
      foodService = TestBed.inject(FoodService);
      factorService = TestBed.inject(FactorService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Food query and add missing value', () => {
        const factorItem: IFactorItem = { id: 456 };
        const food: IFood = { id: 97026 };
        factorItem.food = food;

        const foodCollection: IFood[] = [{ id: 83920 }];
        spyOn(foodService, 'query').and.returnValue(of(new HttpResponse({ body: foodCollection })));
        const additionalFoods = [food];
        const expectedCollection: IFood[] = [...additionalFoods, ...foodCollection];
        spyOn(foodService, 'addFoodToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ factorItem });
        comp.ngOnInit();

        expect(foodService.query).toHaveBeenCalled();
        expect(foodService.addFoodToCollectionIfMissing).toHaveBeenCalledWith(foodCollection, ...additionalFoods);
        expect(comp.foodsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Factor query and add missing value', () => {
        const factorItem: IFactorItem = { id: 456 };
        const factor: IFactor = { id: 81925 };
        factorItem.factor = factor;

        const factorCollection: IFactor[] = [{ id: 80044 }];
        spyOn(factorService, 'query').and.returnValue(of(new HttpResponse({ body: factorCollection })));
        const additionalFactors = [factor];
        const expectedCollection: IFactor[] = [...additionalFactors, ...factorCollection];
        spyOn(factorService, 'addFactorToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ factorItem });
        comp.ngOnInit();

        expect(factorService.query).toHaveBeenCalled();
        expect(factorService.addFactorToCollectionIfMissing).toHaveBeenCalledWith(factorCollection, ...additionalFactors);
        expect(comp.factorsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const factorItem: IFactorItem = { id: 456 };
        const food: IFood = { id: 85472 };
        factorItem.food = food;
        const factor: IFactor = { id: 54244 };
        factorItem.factor = factor;

        activatedRoute.data = of({ factorItem });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(factorItem));
        expect(comp.foodsSharedCollection).toContain(food);
        expect(comp.factorsSharedCollection).toContain(factor);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const factorItem = { id: 123 };
        spyOn(factorItemService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ factorItem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: factorItem }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(factorItemService.update).toHaveBeenCalledWith(factorItem);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const factorItem = new FactorItem();
        spyOn(factorItemService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ factorItem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: factorItem }));
        saveSubject.complete();

        // THEN
        expect(factorItemService.create).toHaveBeenCalledWith(factorItem);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const factorItem = { id: 123 };
        spyOn(factorItemService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ factorItem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(factorItemService.update).toHaveBeenCalledWith(factorItem);
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

      describe('trackFactorById', () => {
        it('Should return tracked Factor primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackFactorById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
