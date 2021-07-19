jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { MenuItemService } from '../service/menu-item.service';
import { IMenuItem, MenuItem } from '../menu-item.model';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IFood } from 'app/entities/food/food.model';
import { FoodService } from 'app/entities/food/service/food.service';

import { MenuItemUpdateComponent } from './menu-item-update.component';

describe('Component Tests', () => {
  describe('MenuItem Management Update Component', () => {
    let comp: MenuItemUpdateComponent;
    let fixture: ComponentFixture<MenuItemUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let menuItemService: MenuItemService;
    let partyService: PartyService;
    let foodService: FoodService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [MenuItemUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(MenuItemUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MenuItemUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      menuItemService = TestBed.inject(MenuItemService);
      partyService = TestBed.inject(PartyService);
      foodService = TestBed.inject(FoodService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Party query and add missing value', () => {
        const menuItem: IMenuItem = { id: 456 };
        const party: IParty = { id: 61265 };
        menuItem.party = party;

        const partyCollection: IParty[] = [{ id: 91663 }];
        spyOn(partyService, 'query').and.returnValue(of(new HttpResponse({ body: partyCollection })));
        const additionalParties = [party];
        const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
        spyOn(partyService, 'addPartyToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ menuItem });
        comp.ngOnInit();

        expect(partyService.query).toHaveBeenCalled();
        expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(partyCollection, ...additionalParties);
        expect(comp.partiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Food query and add missing value', () => {
        const menuItem: IMenuItem = { id: 456 };
        const food: IFood = { id: 93701 };
        menuItem.food = food;

        const foodCollection: IFood[] = [{ id: 57944 }];
        spyOn(foodService, 'query').and.returnValue(of(new HttpResponse({ body: foodCollection })));
        const additionalFoods = [food];
        const expectedCollection: IFood[] = [...additionalFoods, ...foodCollection];
        spyOn(foodService, 'addFoodToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ menuItem });
        comp.ngOnInit();

        expect(foodService.query).toHaveBeenCalled();
        expect(foodService.addFoodToCollectionIfMissing).toHaveBeenCalledWith(foodCollection, ...additionalFoods);
        expect(comp.foodsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const menuItem: IMenuItem = { id: 456 };
        const party: IParty = { id: 68158 };
        menuItem.party = party;
        const food: IFood = { id: 7099 };
        menuItem.food = food;

        activatedRoute.data = of({ menuItem });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(menuItem));
        expect(comp.partiesSharedCollection).toContain(party);
        expect(comp.foodsSharedCollection).toContain(food);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const menuItem = { id: 123 };
        spyOn(menuItemService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ menuItem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: menuItem }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(menuItemService.update).toHaveBeenCalledWith(menuItem);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const menuItem = new MenuItem();
        spyOn(menuItemService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ menuItem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: menuItem }));
        saveSubject.complete();

        // THEN
        expect(menuItemService.create).toHaveBeenCalledWith(menuItem);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const menuItem = { id: 123 };
        spyOn(menuItemService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ menuItem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(menuItemService.update).toHaveBeenCalledWith(menuItem);
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
