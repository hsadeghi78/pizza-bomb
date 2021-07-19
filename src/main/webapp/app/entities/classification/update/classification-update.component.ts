import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IClassification, Classification } from '../classification.model';
import { ClassificationService } from '../service/classification.service';
import { IClassType } from 'app/entities/class-type/class-type.model';
import { ClassTypeService } from 'app/entities/class-type/service/class-type.service';

@Component({
  selector: 'jhi-classification-update',
  templateUrl: './classification-update.component.html',
})
export class ClassificationUpdateComponent implements OnInit {
  isSaving = false;

  classTypesSharedCollection: IClassType[] = [];

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required, Validators.maxLength(200)]],
    classCode: [null, [Validators.required]],
    description: [null, [Validators.maxLength(300)]],
    classType: [null, Validators.required],
  });

  constructor(
    protected classificationService: ClassificationService,
    protected classTypeService: ClassTypeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classification }) => {
      this.updateForm(classification);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const classification = this.createFromForm();
    if (classification.id !== undefined) {
      this.subscribeToSaveResponse(this.classificationService.update(classification));
    } else {
      this.subscribeToSaveResponse(this.classificationService.create(classification));
    }
  }

  trackClassTypeById(index: number, item: IClassType): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClassification>>): void {
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

  protected updateForm(classification: IClassification): void {
    this.editForm.patchValue({
      id: classification.id,
      title: classification.title,
      classCode: classification.classCode,
      description: classification.description,
      classType: classification.classType,
    });

    this.classTypesSharedCollection = this.classTypeService.addClassTypeToCollectionIfMissing(
      this.classTypesSharedCollection,
      classification.classType
    );
  }

  protected loadRelationshipsOptions(): void {
    this.classTypeService
      .query()
      .pipe(map((res: HttpResponse<IClassType[]>) => res.body ?? []))
      .pipe(
        map((classTypes: IClassType[]) =>
          this.classTypeService.addClassTypeToCollectionIfMissing(classTypes, this.editForm.get('classType')!.value)
        )
      )
      .subscribe((classTypes: IClassType[]) => (this.classTypesSharedCollection = classTypes));
  }

  protected createFromForm(): IClassification {
    return {
      ...new Classification(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      classCode: this.editForm.get(['classCode'])!.value,
      description: this.editForm.get(['description'])!.value,
      classType: this.editForm.get(['classType'])!.value,
    };
  }
}
