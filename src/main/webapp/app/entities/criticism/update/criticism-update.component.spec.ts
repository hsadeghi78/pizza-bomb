jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CriticismService } from '../service/criticism.service';
import { ICriticism, Criticism } from '../criticism.model';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';

import { CriticismUpdateComponent } from './criticism-update.component';

describe('Component Tests', () => {
  describe('Criticism Management Update Component', () => {
    let comp: CriticismUpdateComponent;
    let fixture: ComponentFixture<CriticismUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let criticismService: CriticismService;
    let partyService: PartyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CriticismUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CriticismUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CriticismUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      criticismService = TestBed.inject(CriticismService);
      partyService = TestBed.inject(PartyService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Party query and add missing value', () => {
        const criticism: ICriticism = { id: 456 };
        const party: IParty = { id: 94749 };
        criticism.party = party;

        const partyCollection: IParty[] = [{ id: 21639 }];
        spyOn(partyService, 'query').and.returnValue(of(new HttpResponse({ body: partyCollection })));
        const additionalParties = [party];
        const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
        spyOn(partyService, 'addPartyToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ criticism });
        comp.ngOnInit();

        expect(partyService.query).toHaveBeenCalled();
        expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(partyCollection, ...additionalParties);
        expect(comp.partiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const criticism: ICriticism = { id: 456 };
        const party: IParty = { id: 78977 };
        criticism.party = party;

        activatedRoute.data = of({ criticism });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(criticism));
        expect(comp.partiesSharedCollection).toContain(party);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const criticism = { id: 123 };
        spyOn(criticismService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ criticism });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: criticism }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(criticismService.update).toHaveBeenCalledWith(criticism);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const criticism = new Criticism();
        spyOn(criticismService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ criticism });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: criticism }));
        saveSubject.complete();

        // THEN
        expect(criticismService.create).toHaveBeenCalledWith(criticism);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const criticism = { id: 123 };
        spyOn(criticismService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ criticism });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(criticismService.update).toHaveBeenCalledWith(criticism);
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
