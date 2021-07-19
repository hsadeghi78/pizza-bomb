import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFactorItem, FactorItem } from '../factor-item.model';
import { FactorItemService } from '../service/factor-item.service';
import { IFood } from 'app/entities/food/food.model';
import { FoodService } from 'app/entities/food/service/food.service';
import { IFactor } from 'app/entities/factor/factor.model';
import { FactorService } from 'app/entities/factor/service/factor.service';

@Component({
  selector: 'jhi-factor-item-update',
  templateUrl: './factor-item-update.component.html',
})
export class FactorItemUpdateComponent implements OnInit {
  isSaving = false;

  foodsSharedCollection: IFood[] = [];
  factorsSharedCollection: IFactor[] = [];

  editForm = this.fb.group({
    id: [],
    rowNum: [null, [Validators.required]],
    title: [null, [Validators.required, Validators.maxLength(200)]],
    count: [null, [Validators.required]],
    discount: [],
    tax: [],
    description: [null, [Validators.maxLength(300)]],
    food: [null, Validators.required],
    factor: [null, Validators.required],
  });

  constructor(
    protected factorItemService: FactorItemService,
    protected foodService: FoodService,
    protected factorService: FactorService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ factorItem }) => {
      this.updateForm(factorItem);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const factorItem = this.createFromForm();
    if (factorItem.id !== undefined) {
      this.subscribeToSaveResponse(this.factorItemService.update(factorItem));
    } else {
      this.subscribeToSaveResponse(this.factorItemService.create(factorItem));
    }
  }

  trackFoodById(index: number, item: IFood): number {
    return item.id!;
  }

  trackFactorById(index: number, item: IFactor): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFactorItem>>): void {
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

  protected updateForm(factorItem: IFactorItem): void {
    this.editForm.patchValue({
      id: factorItem.id,
      rowNum: factorItem.rowNum,
      title: factorItem.title,
      count: factorItem.count,
      discount: factorItem.discount,
      tax: factorItem.tax,
      description: factorItem.description,
      food: factorItem.food,
      factor: factorItem.factor,
    });

    this.foodsSharedCollection = this.foodService.addFoodToCollectionIfMissing(this.foodsSharedCollection, factorItem.food);
    this.factorsSharedCollection = this.factorService.addFactorToCollectionIfMissing(this.factorsSharedCollection, factorItem.factor);
  }

  protected loadRelationshipsOptions(): void {
    this.foodService
      .query()
      .pipe(map((res: HttpResponse<IFood[]>) => res.body ?? []))
      .pipe(map((foods: IFood[]) => this.foodService.addFoodToCollectionIfMissing(foods, this.editForm.get('food')!.value)))
      .subscribe((foods: IFood[]) => (this.foodsSharedCollection = foods));

    this.factorService
      .query()
      .pipe(map((res: HttpResponse<IFactor[]>) => res.body ?? []))
      .pipe(map((factors: IFactor[]) => this.factorService.addFactorToCollectionIfMissing(factors, this.editForm.get('factor')!.value)))
      .subscribe((factors: IFactor[]) => (this.factorsSharedCollection = factors));
  }

  protected createFromForm(): IFactorItem {
    return {
      ...new FactorItem(),
      id: this.editForm.get(['id'])!.value,
      rowNum: this.editForm.get(['rowNum'])!.value,
      title: this.editForm.get(['title'])!.value,
      count: this.editForm.get(['count'])!.value,
      discount: this.editForm.get(['discount'])!.value,
      tax: this.editForm.get(['tax'])!.value,
      description: this.editForm.get(['description'])!.value,
      food: this.editForm.get(['food'])!.value,
      factor: this.editForm.get(['factor'])!.value,
    };
  }
}
