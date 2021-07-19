jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CommentService } from '../service/comment.service';
import { IComment, Comment } from '../comment.model';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';

import { CommentUpdateComponent } from './comment-update.component';

describe('Component Tests', () => {
  describe('Comment Management Update Component', () => {
    let comp: CommentUpdateComponent;
    let fixture: ComponentFixture<CommentUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let commentService: CommentService;
    let partyService: PartyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CommentUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CommentUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CommentUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      commentService = TestBed.inject(CommentService);
      partyService = TestBed.inject(PartyService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Comment query and add missing value', () => {
        const comment: IComment = { id: 456 };
        const parent: IComment = { id: 7255 };
        comment.parent = parent;

        const commentCollection: IComment[] = [{ id: 57746 }];
        spyOn(commentService, 'query').and.returnValue(of(new HttpResponse({ body: commentCollection })));
        const additionalComments = [parent];
        const expectedCollection: IComment[] = [...additionalComments, ...commentCollection];
        spyOn(commentService, 'addCommentToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ comment });
        comp.ngOnInit();

        expect(commentService.query).toHaveBeenCalled();
        expect(commentService.addCommentToCollectionIfMissing).toHaveBeenCalledWith(commentCollection, ...additionalComments);
        expect(comp.commentsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Party query and add missing value', () => {
        const comment: IComment = { id: 456 };
        const writerParty: IParty = { id: 58740 };
        comment.writerParty = writerParty;
        const audienceParty: IParty = { id: 38954 };
        comment.audienceParty = audienceParty;

        const partyCollection: IParty[] = [{ id: 36285 }];
        spyOn(partyService, 'query').and.returnValue(of(new HttpResponse({ body: partyCollection })));
        const additionalParties = [writerParty, audienceParty];
        const expectedCollection: IParty[] = [...additionalParties, ...partyCollection];
        spyOn(partyService, 'addPartyToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ comment });
        comp.ngOnInit();

        expect(partyService.query).toHaveBeenCalled();
        expect(partyService.addPartyToCollectionIfMissing).toHaveBeenCalledWith(partyCollection, ...additionalParties);
        expect(comp.partiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const comment: IComment = { id: 456 };
        const parent: IComment = { id: 20305 };
        comment.parent = parent;
        const writerParty: IParty = { id: 77255 };
        comment.writerParty = writerParty;
        const audienceParty: IParty = { id: 23697 };
        comment.audienceParty = audienceParty;

        activatedRoute.data = of({ comment });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(comment));
        expect(comp.commentsSharedCollection).toContain(parent);
        expect(comp.partiesSharedCollection).toContain(writerParty);
        expect(comp.partiesSharedCollection).toContain(audienceParty);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const comment = { id: 123 };
        spyOn(commentService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ comment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: comment }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(commentService.update).toHaveBeenCalledWith(comment);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const comment = new Comment();
        spyOn(commentService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ comment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: comment }));
        saveSubject.complete();

        // THEN
        expect(commentService.create).toHaveBeenCalledWith(comment);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const comment = { id: 123 };
        spyOn(commentService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ comment });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(commentService.update).toHaveBeenCalledWith(comment);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCommentById', () => {
        it('Should return tracked Comment primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCommentById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

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
