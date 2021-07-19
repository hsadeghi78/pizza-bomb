jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FoodService } from '../service/food.service';
import { IFood, Food } from '../food.model';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IFoodType } from 'app/entities/food-type/food-type.model';
import { FoodTypeService } from 'app/entities/food-type/service/food-type.service';

import { FoodUpdateComponent } from './food-update.component';

describe('Component Tests', () => {
  describe('Food Management Update Component', () => {
    let comp: FoodUpdateComponent;
    let fixture: ComponentFixture<FoodUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let foodService: FoodService;
    let partyService: PartyService;
    let foodTypeService: FoodTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FoodUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FoodUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FoodUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      foodService = TestBed.inject(FoodService);
      partyService = TestBed.inject(PartyService);
      foodTypeService = TestBed.inject(FoodTypeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Party query and add missing value', () => {
        const food: IFood = { id: 456 };
        const producerParty: IParty = { id: 39496 };
        food.producerParty = producerParty;
        const designerParty: IParty = { id: 30312 };
        food.designerParty = designerParty;

        const partyCollection: IParty[] = [{ id: 11402 }];
        spyOn(partyService, 'query').and.returnValue(of(new HttpResponse({ body: partyCollection })));
        const additionalParties = [producerParty, designerParty];
        const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
        spyOn(partyService, 'addPartyToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ food });
        comp.ngOnInit();

        expect(partyService.query).toHaveBeenCalled();
        expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(partyCollection, ...additionalParties);
        expect(comp.partiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call FoodType query and add missing value', () => {
        const food: IFood = { id: 456 };
        const foodType: IFoodType = { id: 51510 };
        food.foodType = foodType;

        const foodTypeCollection: IFoodType[] = [{ id: 44332 }];
        spyOn(foodTypeService, 'query').and.returnValue(of(new HttpResponse({ body: foodTypeCollection })));
        const additionalFoodTypes = [foodType];
        const expectedCollection: IFoodType[] = [...additionalFoodTypes, ...foodTypeCollection];
        spyOn(foodTypeService, 'addFoodTypeToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ food });
        comp.ngOnInit();

        expect(foodTypeService.query).toHaveBeenCalled();
        expect(foodTypeService.addFoodTypeToCollectionIfMissing).toHaveBeenCalledWith(foodTypeCollection, ...additionalFoodTypes);
        expect(comp.foodTypesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const food: IFood = { id: 456 };
        const producerParty: IParty = { id: 20268 };
        food.producerParty = producerParty;
        const designerParty: IParty = { id: 6833 };
        food.designerParty = designerParty;
        const foodType: IFoodType = { id: 15073 };
        food.foodType = foodType;

        activatedRoute.data = of({ food });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(food));
        expect(comp.partiesSharedCollection).toContain(producerParty);
        expect(comp.partiesSharedCollection).toContain(designerParty);
        expect(comp.foodTypesSharedCollection).toContain(foodType);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const food = { id: 123 };
        spyOn(foodService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ food });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: food }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(foodService.update).toHaveBeenCalledWith(food);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const food = new Food();
        spyOn(foodService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ food });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: food }));
        saveSubject.complete();

        // THEN
        expect(foodService.create).toHaveBeenCalledWith(food);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const food = { id: 123 };
        spyOn(foodService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ food });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(foodService.update).toHaveBeenCalledWith(food);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPartyById', () => {
        it('Should return tracked Party primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPartyById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackFoodTypeById', () => {
        it('Should return tracked FoodType primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackFoodTypeById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
