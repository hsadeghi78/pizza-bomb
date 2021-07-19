import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAddress, Address } from '../address.model';
import { AddressService } from '../service/address.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';

@Component({
  selector: 'jhi-address-update',
  templateUrl: './address-update.component.html',
})
export class AddressUpdateComponent implements OnInit {
  isSaving = false;

  partiesSharedCollection: IParty[] = [];

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required, Validators.maxLength(200)]],
    lat: [null, [Validators.required]],
    lon: [null, [Validators.required]],
    street1: [null, [Validators.maxLength(200)]],
    street2: [null, [Validators.maxLength(200)]],
    address: [null, [Validators.required, Validators.maxLength(1000)]],
    postalCode: [null, [Validators.required, Validators.maxLength(12)]],
    party: [null, Validators.required],
  });

  constructor(
    protected addressService: AddressService,
    protected partyService: PartyService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ address }) => {
      this.updateForm(address);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const address = this.createFromForm();
    if (address.id !== undefined) {
      this.subscribeToSaveResponse(this.addressService.update(address));
    } else {
      this.subscribeToSaveResponse(this.addressService.create(address));
    }
  }

  trackPartyById(index: number, item: IParty): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAddress>>): void {
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

  protected updateForm(address: IAddress): void {
    this.editForm.patchValue({
      id: address.id,
      title: address.title,
      lat: address.lat,
      lon: address.lon,
      street1: address.street1,
      street2: address.street2,
      address: address.address,
      postalCode: address.postalCode,
      party: address.party,
    });

    this.partiesSharedCollection = this.partyService.addPartyToCollectionIfMissing(this.partiesSharedCollection, address.party);
  }

  protected loadRelationshipsOptions(): void {
    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing(parties, this.editForm.get('party')!.value)))
      .subscribe((parties: IParty[]) => (this.partiesSharedCollection = parties));
  }

  protected createFromForm(): IAddress {
    return {
      ...new Address(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      lat: this.editForm.get(['lat'])!.value,
      lon: this.editForm.get(['lon'])!.value,
      street1: this.editForm.get(['street1'])!.value,
      street2: this.editForm.get(['street2'])!.value,
      address: this.editForm.get(['address'])!.value,
      postalCode: this.editForm.get(['postalCode'])!.value,
      party: this.editForm.get(['party'])!.value,
    };
  }
}
