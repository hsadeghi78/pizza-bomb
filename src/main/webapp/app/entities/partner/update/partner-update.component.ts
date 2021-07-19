import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPartner, Partner } from '../partner.model';
import { PartnerService } from '../service/partner.service';

@Component({
  selector: 'jhi-partner-update',
  templateUrl: './partner-update.component.html',
})
export class PartnerUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required, Validators.maxLength(200)]],
    partnerCode: [null, [Validators.required, Validators.maxLength(100)]],
    tradeTitle: [null, [Validators.required, Validators.maxLength(200)]],
    economicCode: [null, [Validators.maxLength(100)]],
    activityDate: [],
  });

  constructor(protected partnerService: PartnerService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ partner }) => {
      this.updateForm(partner);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const partner = this.createFromForm();
    if (partner.id !== undefined) {
      this.subscribeToSaveResponse(this.partnerService.update(partner));
    } else {
      this.subscribeToSaveResponse(this.partnerService.create(partner));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPartner>>): void {
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

  protected updateForm(partner: IPartner): void {
    this.editForm.patchValue({
      id: partner.id,
      title: partner.title,
      partnerCode: partner.partnerCode,
      tradeTitle: partner.tradeTitle,
      economicCode: partner.economicCode,
      activityDate: partner.activityDate,
    });
  }

  protected createFromForm(): IPartner {
    return {
      ...new Partner(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      partnerCode: this.editForm.get(['partnerCode'])!.value,
      tradeTitle: this.editForm.get(['tradeTitle'])!.value,
      economicCode: this.editForm.get(['economicCode'])!.value,
      activityDate: this.editForm.get(['activityDate'])!.value,
    };
  }
}
