jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FileDocumentService } from '../service/file-document.service';
import { IFileDocument, FileDocument } from '../file-document.model';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';

import { FileDocumentUpdateComponent } from './file-document-update.component';

describe('Component Tests', () => {
  describe('FileDocument Management Update Component', () => {
    let comp: FileDocumentUpdateComponent;
    let fixture: ComponentFixture<FileDocumentUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let fileDocumentService: FileDocumentService;
    let partyService: PartyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FileDocumentUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FileDocumentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FileDocumentUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      fileDocumentService = TestBed.inject(FileDocumentService);
      partyService = TestBed.inject(PartyService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Party query and add missing value', () => {
        const fileDocument: IFileDocument = { id: 456 };
        const party: IParty = { id: 83870 };
        fileDocument.party = party;

        const partyCollection: IParty[] = [{ id: 45036 }];
        spyOn(partyService, 'query').and.returnValue(of(new HttpResponse({ body: partyCollection })));
        const additionalParties = [party];
        const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
        spyOn(partyService, 'addPartyToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ fileDocument });
        comp.ngOnInit();

        expect(partyService.query).toHaveBeenCalled();
        expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(partyCollection, ...additionalParties);
        expect(comp.partiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const fileDocument: IFileDocument = { id: 456 };
        const party: IParty = { id: 82448 };
        fileDocument.party = party;

        activatedRoute.data = of({ fileDocument });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(fileDocument));
        expect(comp.partiesSharedCollection).toContain(party);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const fileDocument = { id: 123 };
        spyOn(fileDocumentService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ fileDocument });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: fileDocument }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(fileDocumentService.update).toHaveBeenCalledWith(fileDocument);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const fileDocument = new FileDocument();
        spyOn(fileDocumentService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ fileDocument });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: fileDocument }));
        saveSubject.complete();

        // THEN
        expect(fileDocumentService.create).toHaveBeenCalledWith(fileDocument);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const fileDocument = { id: 123 };
        spyOn(fileDocumentService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ fileDocument });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(fileDocumentService.update).toHaveBeenCalledWith(fileDocument);
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
