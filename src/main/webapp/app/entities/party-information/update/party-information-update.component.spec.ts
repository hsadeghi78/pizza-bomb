jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PartyInformationService } from '../service/party-information.service';
import { IPartyInformation, PartyInformation } from '../party-information.model';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';

import { PartyInformationUpdateComponent } from './party-information-update.component';

describe('Component Tests', () => {
  describe('PartyInformation Management Update Component', () => {
    let comp: PartyInformationUpdateComponent;
    let fixture: ComponentFixture<PartyInformationUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let partyInformationService: PartyInformationService;
    let partyService: PartyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PartyInformationUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PartyInformationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PartyInformationUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      partyInformationService = TestBed.inject(PartyInformationService);
      partyService = TestBed.inject(PartyService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Party query and add missing value', () => {
        const partyInformation: IPartyInformation = { id: 456 };
        const party: IParty = { id: 93494 };
        partyInformation.party = party;

        const partyCollection: IParty[] = [{ id: 36269 }];
        spyOn(partyService, 'query').and.returnValue(of(new HttpResponse({ body: partyCollection })));
        const additionalParties = [party];
        const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
        spyOn(partyService, 'addPartyToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ partyInformation });
        comp.ngOnInit();

        expect(partyService.query).toHaveBeenCalled();
        expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(partyCollection, ...additionalParties);
        expect(comp.partiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const partyInformation: IPartyInformation = { id: 456 };
        const party: IParty = { id: 26599 };
        partyInformation.party = party;

        activatedRoute.data = of({ partyInformation });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(partyInformation));
        expect(comp.partiesSharedCollection).toContain(party);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const partyInformation = { id: 123 };
        spyOn(partyInformationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ partyInformation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: partyInformation }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(partyInformationService.update).toHaveBeenCalledWith(partyInformation);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const partyInformation = new PartyInformation();
        spyOn(partyInformationService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ partyInformation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: partyInformation }));
        saveSubject.complete();

        // THEN
        expect(partyInformationService.create).toHaveBeenCalledWith(partyInformation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const partyInformation = { id: 123 };
        spyOn(partyInformationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ partyInformation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(partyInformationService.update).toHaveBeenCalledWith(partyInformation);
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
