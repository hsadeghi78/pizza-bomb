jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FactorService } from '../service/factor.service';
import { IFactor, Factor } from '../factor.model';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IAddress } from 'app/entities/address/address.model';
import { AddressService } from 'app/entities/address/service/address.service';

import { FactorUpdateComponent } from './factor-update.component';

describe('Component Tests', () => {
  describe('Factor Management Update Component', () => {
    let comp: FactorUpdateComponent;
    let fixture: ComponentFixture<FactorUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let factorService: FactorService;
    let partyService: PartyService;
    let addressService: AddressService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FactorUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FactorUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FactorUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      factorService = TestBed.inject(FactorService);
      partyService = TestBed.inject(PartyService);
      addressService = TestBed.inject(AddressService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Party query and add missing value', () => {
        const factor: IFactor = { id: 456 };
        const buyerParty: IParty = { id: 84862 };
        factor.buyerParty = buyerParty;
        const sellerParty: IParty = { id: 96329 };
        factor.sellerParty = sellerParty;

        const partyCollection: IParty[] = [{ id: 45211 }];
        spyOn(partyService, 'query').and.returnValue(of(new HttpResponse({ body: partyCollection })));
        const additionalParties = [buyerParty, sellerParty];
        const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
        spyOn(partyService, 'addPartyToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ factor });
        comp.ngOnInit();

        expect(partyService.query).toHaveBeenCalled();
        expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(partyCollection, ...additionalParties);
        expect(comp.partiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Address query and add missing value', () => {
        const factor: IFactor = { id: 456 };
        const deliveryAddress: IAddress = { id: 59832 };
        factor.deliveryAddress = deliveryAddress;

        const addressCollection: IAddress[] = [{ id: 17366 }];
        spyOn(addressService, 'query').and.returnValue(of(new HttpResponse({ body: addressCollection })));
        const additionalAddresses = [deliveryAddress];
        const expectedCollection: IAddress[] = [...additionalAddresses, ...addressCollection];
        spyOn(addressService, 'addAddressToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ factor });
        comp.ngOnInit();

        expect(addressService.query).toHaveBeenCalled();
        expect(addressService.addAddressToCollectionIfMissing).toHaveBeenCalledWith(addressCollection, ...additionalAddresses);
        expect(comp.addressesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const factor: IFactor = { id: 456 };
        const buyerParty: IParty = { id: 95597 };
        factor.buyerParty = buyerParty;
        const sellerParty: IParty = { id: 70409 };
        factor.sellerParty = sellerParty;
        const deliveryAddress: IAddress = { id: 97887 };
        factor.deliveryAddress = deliveryAddress;

        activatedRoute.data = of({ factor });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(factor));
        expect(comp.partiesSharedCollection).toContain(buyerParty);
        expect(comp.partiesSharedCollection).toContain(sellerParty);
        expect(comp.addressesSharedCollection).toContain(deliveryAddress);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const factor = { id: 123 };
        spyOn(factorService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ factor });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: factor }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(factorService.update).toHaveBeenCalledWith(factor);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const factor = new Factor();
        spyOn(factorService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ factor });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: factor }));
        saveSubject.complete();

        // THEN
        expect(factorService.create).toHaveBeenCalledWith(factor);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const factor = { id: 123 };
        spyOn(factorService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ factor });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(factorService.update).toHaveBeenCalledWith(factor);
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

      describe('trackAddressById', () => {
        it('Should return tracked Address primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackAddressById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
