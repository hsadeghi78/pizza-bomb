jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BranchService } from '../service/branch.service';
import { IBranch, Branch } from '../branch.model';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';

import { BranchUpdateComponent } from './branch-update.component';

describe('Component Tests', () => {
  describe('Branch Management Update Component', () => {
    let comp: BranchUpdateComponent;
    let fixture: ComponentFixture<BranchUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let branchService: BranchService;
    let partyService: PartyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BranchUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(BranchUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BranchUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      branchService = TestBed.inject(BranchService);
      partyService = TestBed.inject(PartyService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Party query and add missing value', () => {
        const branch: IBranch = { id: 456 };
        const party: IParty = { id: 9849 };
        branch.party = party;

        const partyCollection: IParty[] = [{ id: 87051 }];
        spyOn(partyService, 'query').and.returnValue(of(new HttpResponse({ body: partyCollection })));
        const additionalParties = [party];
        const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
        spyOn(partyService, 'addPartyToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ branch });
        comp.ngOnInit();

        expect(partyService.query).toHaveBeenCalled();
        expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(partyCollection, ...additionalParties);
        expect(comp.partiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const branch: IBranch = { id: 456 };
        const party: IParty = { id: 98504 };
        branch.party = party;

        activatedRoute.data = of({ branch });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(branch));
        expect(comp.partiesSharedCollection).toContain(party);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const branch = { id: 123 };
        spyOn(branchService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ branch });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: branch }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(branchService.update).toHaveBeenCalledWith(branch);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const branch = new Branch();
        spyOn(branchService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ branch });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: branch }));
        saveSubject.complete();

        // THEN
        expect(branchService.create).toHaveBeenCalledWith(branch);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const branch = { id: 123 };
        spyOn(branchService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ branch });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(branchService.update).toHaveBeenCalledWith(branch);
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
