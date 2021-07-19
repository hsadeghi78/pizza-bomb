jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FoodTypeService } from '../service/food-type.service';
import { IFoodType, FoodType } from '../food-type.model';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';

import { FoodTypeUpdateComponent } from './food-type-update.component';

describe('Component Tests', () => {
  describe('FoodType Management Update Component', () => {
    let comp: FoodTypeUpdateComponent;
    let fixture: ComponentFixture<FoodTypeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let foodTypeService: FoodTypeService;
    let partyService: PartyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FoodTypeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FoodTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FoodTypeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      foodTypeService = TestBed.inject(FoodTypeService);
      partyService = TestBed.inject(PartyService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Party query and add missing value', () => {
        const foodType: IFoodType = { id: 456 };
        const party: IParty = { id: 48707 };
        foodType.party = party;

        const partyCollection: IParty[] = [{ id: 24856 }];
        spyOn(partyService, 'query').and.returnValue(of(new HttpResponse({ body: partyCollection })));
        const additionalParties = [party];
        const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
        spyOn(partyService, 'addPartyToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ foodType });
        comp.ngOnInit();

        expect(partyService.query).toHaveBeenCalled();
        expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(partyCollection, ...additionalParties);
        expect(comp.partiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const foodType: IFoodType = { id: 456 };
        const party: IParty = { id: 27216 };
        foodType.party = party;

        activatedRoute.data = of({ foodType });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(foodType));
        expect(comp.partiesSharedCollection).toContain(party);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const foodType = { id: 123 };
        spyOn(foodTypeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ foodType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: foodType }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(foodTypeService.update).toHaveBeenCalledWith(foodType);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const foodType = new FoodType();
        spyOn(foodTypeService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ foodType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: foodType }));
        saveSubject.complete();

        // THEN
        expect(foodTypeService.create).toHaveBeenCalledWith(foodType);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const foodType = { id: 123 };
        spyOn(foodTypeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ foodType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(foodTypeService.update).toHaveBeenCalledWith(foodType);
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
    });
  });
});
