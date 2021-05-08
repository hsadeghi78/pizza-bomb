import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IBranch, Branch } from '../branch.model';
import { BranchService } from '../service/branch.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';

@Component({
  selector: 'jhi-branch-update',
  templateUrl: './branch-update.component.html',
})
export class BranchUpdateComponent implements OnInit {
  isSaving = false;

  partiesSharedCollection: IParty[] = [];

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required, Validators.maxLength(200)]],
    branchCode: [null, [Validators.required, Validators.maxLength(100)]],
    tradeTitle: [null, [Validators.required, Validators.maxLength(200)]],
    activationDate: [null, [Validators.required]],
    expirationDate: [],
    activationStatus: [null, [Validators.required]],
    lat: [null, [Validators.required]],
    address: [null, [Validators.required, Validators.maxLength(3000)]],
    postalCode: [null, [Validators.required, Validators.maxLength(12)]],
    description: [null, [Validators.maxLength(3000)]],
    party: [null, Validators.required],
  });

  constructor(
    protected branchService: BranchService,
    protected partyService: PartyService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ branch }) => {
      this.updateForm(branch);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const branch = this.createFromForm();
    if (branch.id !== undefined) {
      this.subscribeToSaveResponse(this.branchService.update(branch));
    } else {
      this.subscribeToSaveResponse(this.branchService.create(branch));
    }
  }

  trackPartyById(index: number, item: IParty): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBranch>>): void {
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

  protected updateForm(branch: IBranch): void {
    this.editForm.patchValue({
      id: branch.id,
      title: branch.title,
      branchCode: branch.branchCode,
      tradeTitle: branch.tradeTitle,
      activationDate: branch.activationDate,
      expirationDate: branch.expirationDate,
      activationStatus: branch.activationStatus,
      lat: branch.lat,
      address: branch.address,
      postalCode: branch.postalCode,
      description: branch.description,
      party: branch.party,
    });

    this.partiesSharedCollection = this.partyService.addPartyToCollectionIfMissing(this.partiesSharedCollection, branch.party);
  }

  protected loadRelationshipsOptions(): void {
    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing(parties, this.editForm.get('party')!.value)))
      .subscribe((parties: IParty[]) => (this.partiesSharedCollection = parties));
  }

  protected createFromForm(): IBranch {
    return {
      ...new Branch(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      branchCode: this.editForm.get(['branchCode'])!.value,
      tradeTitle: this.editForm.get(['tradeTitle'])!.value,
      activationDate: this.editForm.get(['activationDate'])!.value,
      expirationDate: this.editForm.get(['expirationDate'])!.value,
      activationStatus: this.editForm.get(['activationStatus'])!.value,
      lat: this.editForm.get(['lat'])!.value,
      address: this.editForm.get(['address'])!.value,
      postalCode: this.editForm.get(['postalCode'])!.value,
      description: this.editForm.get(['description'])!.value,
      party: this.editForm.get(['party'])!.value,
    };
  }
}
