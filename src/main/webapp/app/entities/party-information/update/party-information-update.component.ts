import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPartyInformation, PartyInformation } from '../party-information.model';
import { PartyInformationService } from '../service/party-information.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';

@Component({
  selector: 'jhi-party-information-update',
  templateUrl: './party-information-update.component.html',
})
export class PartyInformationUpdateComponent implements OnInit {
  isSaving = false;

  partiesSharedCollection: IParty[] = [];

  editForm = this.fb.group({
    id: [],
    infoType: [null, [Validators.required]],
    infoTitle: [null, [Validators.required, Validators.maxLength(200)]],
    infoDesc: [null, [Validators.maxLength(2000)]],
    party: [null, Validators.required],
  });

  constructor(
    protected partyInformationService: PartyInformationService,
    protected partyService: PartyService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ partyInformation }) => {
      this.updateForm(partyInformation);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const partyInformation = this.createFromForm();
    if (partyInformation.id !== undefined) {
      this.subscribeToSaveResponse(this.partyInformationService.update(partyInformation));
    } else {
      this.subscribeToSaveResponse(this.partyInformationService.create(partyInformation));
    }
  }

  trackPartyById(index: number, item: IParty): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPartyInformation>>): void {
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

  protected updateForm(partyInformation: IPartyInformation): void {
    this.editForm.patchValue({
      id: partyInformation.id,
      infoType: partyInformation.infoType,
      infoTitle: partyInformation.infoTitle,
      infoDesc: partyInformation.infoDesc,
      party: partyInformation.party,
    });

    this.partiesSharedCollection = this.partyService.addPartyToCollectionIfMissing(this.partiesSharedCollection, partyInformation.party);
  }

  protected loadRelationshipsOptions(): void {
    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing(parties, this.editForm.get('party')!.value)))
      .subscribe((parties: IParty[]) => (this.partiesSharedCollection = parties));
  }

  protected createFromForm(): IPartyInformation {
    return {
      ...new PartyInformation(),
      id: this.editForm.get(['id'])!.value,
      infoType: this.editForm.get(['infoType'])!.value,
      infoTitle: this.editForm.get(['infoTitle'])!.value,
      infoDesc: this.editForm.get(['infoDesc'])!.value,
      party: this.editForm.get(['party'])!.value,
    };
  }
}
