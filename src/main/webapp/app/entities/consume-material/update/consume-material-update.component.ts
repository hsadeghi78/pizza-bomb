import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IConsumeMaterial, ConsumeMaterial } from '../consume-material.model';
import { ConsumeMaterialService } from '../service/consume-material.service';
import { IFood } from 'app/entities/food/food.model';
import { FoodService } from 'app/entities/food/service/food.service';

@Component({
  selector: 'jhi-consume-material-update',
  templateUrl: './consume-material-update.component.html',
})
export class ConsumeMaterialUpdateComponent implements OnInit {
  isSaving = false;

  foodsSharedCollection: IFood[] = [];

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required, Validators.maxLength(200)]],
    type: [null, [Validators.maxLength(100)]],
    amount: [null, [Validators.required]],
    amountUnitClassId: [null, [Validators.required]],
    food: [null, Validators.required],
  });

  constructor(
    protected consumeMaterialService: ConsumeMaterialService,
    protected foodService: FoodService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ consumeMaterial }) => {
      this.updateForm(consumeMaterial);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const consumeMaterial = this.createFromForm();
    if (consumeMaterial.id !== undefined) {
      this.subscribeToSaveResponse(this.consumeMaterialService.update(consumeMaterial));
    } else {
      this.subscribeToSaveResponse(this.consumeMaterialService.create(consumeMaterial));
    }
  }

  trackFoodById(index: number, item: IFood): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConsumeMaterial>>): void {
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

  protected updateForm(consumeMaterial: IConsumeMaterial): void {
    this.editForm.patchValue({
      id: consumeMaterial.id,
      title: consumeMaterial.title,
      type: consumeMaterial.type,
      amount: consumeMaterial.amount,
      amountUnitClassId: consumeMaterial.amountUnitClassId,
      food: consumeMaterial.food,
    });

    this.foodsSharedCollection = this.foodService.addFoodToCollectionIfMissing(this.foodsSharedCollection, consumeMaterial.food);
  }

  protected loadRelationshipsOptions(): void {
    this.foodService
      .query()
      .pipe(map((res: HttpResponse<IFood[]>) => res.body ?? []))
      .pipe(map((foods: IFood[]) => this.foodService.addFoodToCollectionIfMissing(foods, this.editForm.get('food')!.value)))
      .subscribe((foods: IFood[]) => (this.foodsSharedCollection = foods));
  }

  protected createFromForm(): IConsumeMaterial {
    return {
      ...new ConsumeMaterial(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      type: this.editForm.get(['type'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      amountUnitClassId: this.editForm.get(['amountUnitClassId'])!.value,
      food: this.editForm.get(['food'])!.value,
    };
  }
}
