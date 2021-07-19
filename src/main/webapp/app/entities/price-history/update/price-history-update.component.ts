import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPriceHistory, PriceHistory } from '../price-history.model';
import { PriceHistoryService } from '../service/price-history.service';

@Component({
  selector: 'jhi-price-history-update',
  templateUrl: './price-history-update.component.html',
})
export class PriceHistoryUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    foodId: [],
    materialId: [],
    price: [null, [Validators.required]],
  });

  constructor(protected priceHistoryService: PriceHistoryService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ priceHistory }) => {
      this.updateForm(priceHistory);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const priceHistory = this.createFromForm();
    if (priceHistory.id !== undefined) {
      this.subscribeToSaveResponse(this.priceHistoryService.update(priceHistory));
    } else {
      this.subscribeToSaveResponse(this.priceHistoryService.create(priceHistory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPriceHistory>>): void {
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

  protected updateForm(priceHistory: IPriceHistory): void {
    this.editForm.patchValue({
      id: priceHistory.id,
      foodId: priceHistory.foodId,
      materialId: priceHistory.materialId,
      price: priceHistory.price,
    });
  }

  protected createFromForm(): IPriceHistory {
    return {
      ...new PriceHistory(),
      id: this.editForm.get(['id'])!.value,
      foodId: this.editForm.get(['foodId'])!.value,
      materialId: this.editForm.get(['materialId'])!.value,
      price: this.editForm.get(['price'])!.value,
    };
  }
}
