import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IFactorStatusHistory, FactorStatusHistory } from '../factor-status-history.model';
import { FactorStatusHistoryService } from '../service/factor-status-history.service';

@Component({
  selector: 'jhi-factor-status-history-update',
  templateUrl: './factor-status-history-update.component.html',
})
export class FactorStatusHistoryUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    factorId: [null, [Validators.required]],
    status: [null, [Validators.required]],
  });

  constructor(
    protected factorStatusHistoryService: FactorStatusHistoryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ factorStatusHistory }) => {
      this.updateForm(factorStatusHistory);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const factorStatusHistory = this.createFromForm();
    if (factorStatusHistory.id !== undefined) {
      this.subscribeToSaveResponse(this.factorStatusHistoryService.update(factorStatusHistory));
    } else {
      this.subscribeToSaveResponse(this.factorStatusHistoryService.create(factorStatusHistory));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFactorStatusHistory>>): void {
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

  protected updateForm(factorStatusHistory: IFactorStatusHistory): void {
    this.editForm.patchValue({
      id: factorStatusHistory.id,
      factorId: factorStatusHistory.factorId,
      status: factorStatusHistory.status,
    });
  }

  protected createFromForm(): IFactorStatusHistory {
    return {
      ...new FactorStatusHistory(),
      id: this.editForm.get(['id'])!.value,
      factorId: this.editForm.get(['factorId'])!.value,
      status: this.editForm.get(['status'])!.value,
    };
  }
}
