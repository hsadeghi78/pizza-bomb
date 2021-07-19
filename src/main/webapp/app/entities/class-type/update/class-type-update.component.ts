import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IClassType, ClassType } from '../class-type.model';
import { ClassTypeService } from '../service/class-type.service';

@Component({
  selector: 'jhi-class-type-update',
  templateUrl: './class-type-update.component.html',
})
export class ClassTypeUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required, Validators.maxLength(200)]],
    typeCode: [null, [Validators.required]],
    description: [null, [Validators.maxLength(300)]],
  });

  constructor(protected classTypeService: ClassTypeService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classType }) => {
      this.updateForm(classType);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const classType = this.createFromForm();
    if (classType.id !== undefined) {
      this.subscribeToSaveResponse(this.classTypeService.update(classType));
    } else {
      this.subscribeToSaveResponse(this.classTypeService.create(classType));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClassType>>): void {
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

  protected updateForm(classType: IClassType): void {
    this.editForm.patchValue({
      id: classType.id,
      title: classType.title,
      typeCode: classType.typeCode,
      description: classType.description,
    });
  }

  protected createFromForm(): IClassType {
    return {
      ...new ClassType(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      typeCode: this.editForm.get(['typeCode'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }
}
