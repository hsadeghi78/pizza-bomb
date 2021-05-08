import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICriticism, Criticism } from '../criticism.model';
import { CriticismService } from '../service/criticism.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';

@Component({
  selector: 'jhi-criticism-update',
  templateUrl: './criticism-update.component.html',
})
export class CriticismUpdateComponent implements OnInit {
  isSaving = false;

  partiesSharedCollection: IParty[] = [];

  editForm = this.fb.group({
    id: [],
    fullName: [null, [Validators.required, Validators.maxLength(150)]],
    email: [null, [Validators.maxLength(150)]],
    contactNumber: [null, [Validators.maxLength(15)]],
    description: [null, [Validators.required, Validators.maxLength(3000)]],
    party: [],
  });

  constructor(
    protected criticismService: CriticismService,
    protected partyService: PartyService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ criticism }) => {
      this.updateForm(criticism);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const criticism = this.createFromForm();
    if (criticism.id !== undefined) {
      this.subscribeToSaveResponse(this.criticismService.update(criticism));
    } else {
      this.subscribeToSaveResponse(this.criticismService.create(criticism));
    }
  }

  trackPartyById(index: number, item: IParty): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICriticism>>): void {
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

  protected updateForm(criticism: ICriticism): void {
    this.editForm.patchValue({
      id: criticism.id,
      fullName: criticism.fullName,
      email: criticism.email,
      contactNumber: criticism.contactNumber,
      description: criticism.description,
      party: criticism.party,
    });

    this.partiesSharedCollection = this.partyService.addPartyToCollectionIfMissing(this.partiesSharedCollection, criticism.party);
  }

  protected loadRelationshipsOptions(): void {
    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing(parties, this.editForm.get('party')!.value)))
      .subscribe((parties: IParty[]) => (this.partiesSharedCollection = parties));
  }

  protected createFromForm(): ICriticism {
    return {
      ...new Criticism(),
      id: this.editForm.get(['id'])!.value,
      fullName: this.editForm.get(['fullName'])!.value,
      email: this.editForm.get(['email'])!.value,
      contactNumber: this.editForm.get(['contactNumber'])!.value,
      description: this.editForm.get(['description'])!.value,
      party: this.editForm.get(['party'])!.value,
    };
  }
}
