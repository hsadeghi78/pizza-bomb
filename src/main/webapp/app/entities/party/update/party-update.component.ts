import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IParty, Party } from '../party.model';
import { PartyService } from '../service/party.service';

@Component({
  selector: 'jhi-party-update',
  templateUrl: './party-update.component.html',
})
export class PartyUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required, Validators.maxLength(200)]],
    partyCode: [null, [Validators.required, Validators.maxLength(100)]],
    tradeTitle: [null, [Validators.required, Validators.maxLength(200)]],
    activationDate: [null, [Validators.required]],
    expirationDate: [],
    activationStatus: [null, [Validators.required]],
    description: [null, [Validators.maxLength(3000)]],
  });

  constructor(protected partyService: PartyService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ party }) => {
      this.updateForm(party);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const party = this.createFromForm();
    if (party.id !== undefined) {
      this.subscribeToSaveResponse(this.partyService.update(party));
    } else {
      this.subscribeToSaveResponse(this.partyService.create(party));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IParty>>): void {
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

  protected updateForm(party: IParty): void {
    this.editForm.patchValue({
      id: party.id,
      title: party.title,
      partyCode: party.partyCode,
      tradeTitle: party.tradeTitle,
      activationDate: party.activationDate,
      expirationDate: party.expirationDate,
      activationStatus: party.activationStatus,
      description: party.description,
    });
  }

  protected createFromForm(): IParty {
    return {
      ...new Party(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      partyCode: this.editForm.get(['partyCode'])!.value,
      tradeTitle: this.editForm.get(['tradeTitle'])!.value,
      activationDate: this.editForm.get(['activationDate'])!.value,
      expirationDate: this.editForm.get(['expirationDate'])!.value,
      activationStatus: this.editForm.get(['activationStatus'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }
}
