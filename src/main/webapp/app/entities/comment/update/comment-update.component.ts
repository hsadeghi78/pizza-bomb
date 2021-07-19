import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IComment, Comment } from '../comment.model';
import { CommentService } from '../service/comment.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';

@Component({
  selector: 'jhi-comment-update',
  templateUrl: './comment-update.component.html',
})
export class CommentUpdateComponent implements OnInit {
  isSaving = false;

  commentsSharedCollection: IComment[] = [];
  partiesSharedCollection: IParty[] = [];

  editForm = this.fb.group({
    id: [],
    rating: [],
    description: [null, [Validators.maxLength(3000)]],
    writerParty: [null, Validators.required],
    audienceParty: [null, Validators.required],
    parent: [],
  });

  constructor(
    protected commentService: CommentService,
    protected partyService: PartyService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ comment }) => {
      this.updateForm(comment);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const comment = this.createFromForm();
    if (comment.id !== undefined) {
      this.subscribeToSaveResponse(this.commentService.update(comment));
    } else {
      this.subscribeToSaveResponse(this.commentService.create(comment));
    }
  }

  trackCommentById(index: number, item: IComment): number {
    return item.id!;
  }

  trackPartyById(index: number, item: IParty): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IComment>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(comment: IComment): void {
    this.editForm.patchValue({
      id: comment.id,
      rating: comment.rating,
      description: comment.description,
      writerParty: comment.writerParty,
      audienceParty: comment.audienceParty,
      parent: comment.parent,
    });

    this.commentsSharedCollection = this.commentService.addCommentToCollectionIfMissing(this.commentsSharedCollection, comment.parent);
    this.partiesSharedCollection = this.partyService.addPartyToCollectionIfMissing(
      this.partiesSharedCollection,
      comment.writerParty,
      comment.audienceParty
    );
  }

  protected loadRelationshipsOptions(): void {
    this.commentService
      .query()
      .pipe(map((res: HttpResponse<IComment[]>) => res.body ?? []))
      .pipe(
        map((comments: IComment[]) => this.commentService.addCommentToCollectionIfMissing(comments, this.editForm.get('parent')!.value))
      )
      .subscribe((comments: IComment[]) => (this.commentsSharedCollection = comments));

    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(
        map((parties: IParty[]) =>
          this.partyService.addPartyToCollectionIfMissing(
            parties,
            this.editForm.get('writerParty')!.value,
            this.editForm.get('audienceParty')!.value
          )
        )
      )
      .subscribe((parties: IParty[]) => (this.partiesSharedCollection = parties));
  }

  protected createFromForm(): IComment {
    return {
      ...new Comment(),
      id: this.editForm.get(['id'])!.value,
      rating: this.editForm.get(['rating'])!.value,
      description: this.editForm.get(['description'])!.value,
      writerParty: this.editForm.get(['writerParty'])!.value,
      audienceParty: this.editForm.get(['audienceParty'])!.value,
      parent: this.editForm.get(['parent'])!.value,
    };
  }
}
