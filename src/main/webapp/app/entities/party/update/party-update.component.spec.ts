jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PartyService } from '../service/party.service';
import { IParty, Party } from '../party.model';
import { IPartner } from 'app/entities/partner/partner.model';
import { PartnerService } from 'app/entities/partner/service/partner.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';

import { PartyUpdateComponent } from './party-update.component';

describe('Component Tests', () => {
  describe('Party Management Update Component', () => {
    let comp: PartyUpdateComponent;
    let fixture: ComponentFixture<PartyUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let partyService: PartyService;
    let partnerService: PartnerService;
    let personService: PersonService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PartyUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PartyUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PartyUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      partyService = TestBed.inject(PartyService);
      partnerService = TestBed.inject(PartnerService);
      personService = TestBed.inject(PersonService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Party query and add missing value', () => {
        const party: IParty = { id: 456 };
        const parent: IParty = { id: 84939 };
        party.parent = parent;

        const partyCollection: IParty[] = [{ id: 69903 }];
        spyOn(partyService, 'query').and.returnValue(of(new HttpResponse({ body: partyCollection })));
        const additionalParties = [parent];
        const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
        spyOn(partyService, 'addPartyToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ party });
        comp.ngOnInit();

        expect(partyService.query).toHaveBeenCalled();
        expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(partyCollection, ...additionalParties);
        expect(comp.partiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Partner query and add missing value', () => {
        const party: IParty = { id: 456 };
        const partner: IPartner = { id: 31979 };
        party.partner = partner;

        const partnerCollection: IPartner[] = [{ id: 72097 }];
        spyOn(partnerService, 'query').and.returnValue(of(new HttpResponse({ body: partnerCollection })));
        const additionalPartners = [partner];
        const expectedCollection: IPartner[] = [...additionalPartners, ...partnerCollection];
        spyOn(partnerService, 'addPartnerToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ party });
        comp.ngOnInit();

        expect(partnerService.query).toHaveBeenCalled();
        expect(partnerService.addPartnerToCollectionIfMissing).toHaveBeenCalledWith(partnerCollection, ...additionalPartners);
        expect(comp.partnersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Person query and add missing value', () => {
        const party: IParty = { id: 456 };
        const person: IPerson = { id: 14161 };
        party.person = person;

        const personCollection: IPerson[] = [{ id: 4928 }];
        spyOn(personService, 'query').and.returnValue(of(new HttpResponse({ body: personCollection })));
        const additionalPeople = [person];
        const expectedCollection: IPerson[] = [...additionalPeople, ...personCollection];
        spyOn(personService, 'addPersonToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ party });
        comp.ngOnInit();

        expect(personService.query).toHaveBeenCalled();
        expect(personService.addPersonToCollectionIfMissing).toHaveBeenCalledWith(personCollection, ...additionalPeople);
        expect(comp.peopleSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const party: IParty = { id: 456 };
        const parent: IParty = { id: 8314 };
        party.parent = parent;
        const partner: IPartner = { id: 67544 };
        party.partner = partner;
        const person: IPerson = { id: 82849 };
        party.person = person;

        activatedRoute.data = of({ party });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(party));
        expect(comp.partiesSharedCollection).toContain(parent);
        expect(comp.partnersSharedCollection).toContain(partner);
        expect(comp.peopleSharedCollection).toContain(person);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const party = { id: 123 };
        spyOn(partyService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ party });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: party }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(partyService.update).toHaveBeenCalledWith(party);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const party = new Party();
        spyOn(partyService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ party });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: party }));
        saveSubject.complete();

        // THEN
        expect(partyService.create).toHaveBeenCalledWith(party);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const party = { id: 123 };
        spyOn(partyService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ party });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(partyService.update).toHaveBeenCalledWith(party);
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

      describe('trackPartnerById', () => {
        it('Should return tracked Partner primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPartnerById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackPersonById', () => {
        it('Should return tracked Person primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPersonById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
