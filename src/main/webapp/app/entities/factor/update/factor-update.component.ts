import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFactor, Factor } from '../factor.model';
import { FactorService } from '../service/factor.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IAddress } from 'app/entities/address/address.model';
import { AddressService } from 'app/entities/address/service/address.service';

@Component({
  selector: 'jhi-factor-update',
  templateUrl: './factor-update.component.html',
})
export class FactorUpdateComponent implements OnInit {
  isSaving = false;

  partiesSharedCollection: IParty[] = [];
  addressesSharedCollection: IAddress[] = [];

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required, Validators.maxLength(200)]],
    factorCode: [null, [Validators.required, Validators.maxLength(100)]],
    lastStatus: [null, [Validators.required]],
    orderWay: [null, [Validators.required]],
    serving: [null, [Validators.required]],
    paymentStateClassId: [null, [Validators.required]],
    categoryClassId: [],
    totalPrice: [null, [Validators.required]],
    discount: [],
    tax: [],
    netprice: [null, [Validators.required]],
    description: [null, [Validators.maxLength(1000)]],
    buyerParty: [null, Validators.required],
    sellerParty: [null, Validators.required],
    deliveryAddress: [],
  });

  constructor(
    protected factorService: FactorService,
    protected partyService: PartyService,
    protected addressService: AddressService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ factor }) => {
      this.updateForm(factor);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const factor = this.createFromForm();
    if (factor.id !== undefined) {
      this.subscribeToSaveResponse(this.factorService.update(factor));
    } else {
      this.subscribeToSaveResponse(this.factorService.create(factor));
    }
  }

  trackPartyById(index: number, item: IParty): number {
    return item.id!;
  }

  trackAddressById(index: number, item: IAddress): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFactor>>): void {
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

  protected updateForm(factor: IFactor): void {
    this.editForm.patchValue({
      id: factor.id,
      title: factor.title,
      factorCode: factor.factorCode,
      lastStatus: factor.lastStatus,
      orderWay: factor.orderWay,
      serving: factor.serving,
      paymentStateClassId: factor.paymentStateClassId,
      categoryClassId: factor.categoryClassId,
      totalPrice: factor.totalPrice,
      discount: factor.discount,
      tax: factor.tax,
      netprice: factor.netprice,
      description: factor.description,
      buyerParty: factor.buyerParty,
      sellerParty: factor.sellerParty,
      deliveryAddress: factor.deliveryAddress,
    });

    this.partiesSharedCollection = this.partyService.addPartyToCollectionIfMissing(
      this.partiesSharedCollection,
      factor.buyerParty,
      factor.sellerParty
    );
    this.addressesSharedCollection = this.addressService.addAddressToCollectionIfMissing(
      this.addressesSharedCollection,
      factor.deliveryAddress
    );
  }

  protected loadRelationshipsOptions(): void {
    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(
        map((parties: IParty[]) =>
          this.partyService.addPartyToCollectionIfMissing(
            parties,
            this.editForm.get('buyerParty')!.value,
            this.editForm.get('sellerParty')!.value
          )
        )
      )
      .subscribe((parties: IParty[]) => (this.partiesSharedCollection = parties));

    this.addressService
      .query()
      .pipe(map((res: HttpResponse<IAddress[]>) => res.body ?? []))
      .pipe(
        map((addresses: IAddress[]) =>
          this.addressService.addAddressToCollectionIfMissing(addresses, this.editForm.get('deliveryAddress')!.value)
        )
      )
      .subscribe((addresses: IAddress[]) => (this.addressesSharedCollection = addresses));
  }

  protected createFromForm(): IFactor {
    return {
      ...new Factor(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      factorCode: this.editForm.get(['factorCode'])!.value,
      lastStatus: this.editForm.get(['lastStatus'])!.value,
      orderWay: this.editForm.get(['orderWay'])!.value,
      serving: this.editForm.get(['serving'])!.value,
      paymentStateClassId: this.editForm.get(['paymentStateClassId'])!.value,
      categoryClassId: this.editForm.get(['categoryClassId'])!.value,
      totalPrice: this.editForm.get(['totalPrice'])!.value,
      discount: this.editForm.get(['discount'])!.value,
      tax: this.editForm.get(['tax'])!.value,
      netprice: this.editForm.get(['netprice'])!.value,
      description: this.editForm.get(['description'])!.value,
      buyerParty: this.editForm.get(['buyerParty'])!.value,
      sellerParty: this.editForm.get(['sellerParty'])!.value,
      deliveryAddress: this.editForm.get(['deliveryAddress'])!.value,
    };
  }
}
