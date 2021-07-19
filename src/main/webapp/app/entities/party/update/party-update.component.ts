import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IParty, Party } from '../party.model';
import { PartyService } from '../service/party.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IPartner } from 'app/entities/partner/partner.model';
import { PartnerService } from 'app/entities/partner/service/partner.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';

@Component({
  selector: 'jhi-party-update',
  templateUrl: './party-update.component.html',
})
export class PartyUpdateComponent implements OnInit {
  isSaving = false;

  partiesSharedCollection: IParty[] = [];
  partnersSharedCollection: IPartner[] = [];
  peopleSharedCollection: IPerson[] = [];

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required, Validators.maxLength(200)]],
    photo: [],
    photoContentType: [],
    partyCode: [null, [Validators.required, Validators.maxLength(100)]],
    tradeTitle: [null, [Validators.required, Validators.maxLength(200)]],
    activationDate: [null, [Validators.required]],
    expirationDate: [],
    activationStatus: [null, [Validators.required]],
    lat: [null, [Validators.required]],
    lon: [null, [Validators.required]],
    address: [null, [Validators.required, Validators.maxLength(3000)]],
    postalCode: [null, [Validators.required, Validators.maxLength(12)]],
    mobile: [null, [Validators.required, Validators.maxLength(15)]],
    partyTypeClassId: [null, [Validators.required]],
    description: [null, [Validators.maxLength(3000)]],
    parent: [],
    partner: [],
    person: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected partyService: PartyService,
    protected partnerService: PartnerService,
    protected personService: PersonService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ party }) => {
      this.updateForm(party);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('bombApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
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

  trackPartyById(index: number, item: IParty): number {
    return item.id!;
  }

  trackPartnerById(index: number, item: IPartner): number {
    return item.id!;
  }

  trackPersonById(index: number, item: IPerson): number {
    return item.id!;
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
      photo: party.photo,
      photoContentType: party.photoContentType,
      partyCode: party.partyCode,
      tradeTitle: party.tradeTitle,
      activationDate: party.activationDate,
      expirationDate: party.expirationDate,
      activationStatus: party.activationStatus,
      lat: party.lat,
      lon: party.lon,
      address: party.address,
      postalCode: party.postalCode,
      mobile: party.mobile,
      partyTypeClassId: party.partyTypeClassId,
      description: party.description,
      parent: party.parent,
      partner: party.partner,
      person: party.person,
    });

    this.partiesSharedCollection = this.partyService.addPartyToCollectionIfMissing(this.partiesSharedCollection, party.parent);
    this.partnersSharedCollection = this.partnerService.addPartnerToCollectionIfMissing(this.partnersSharedCollection, party.partner);
    this.peopleSharedCollection = this.personService.addPersonToCollectionIfMissing(this.peopleSharedCollection, party.person);
  }

  protected loadRelationshipsOptions(): void {
    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing(parties, this.editForm.get('parent')!.value)))
      .subscribe((parties: IParty[]) => (this.partiesSharedCollection = parties));

    this.partnerService
      .query()
      .pipe(map((res: HttpResponse<IPartner[]>) => res.body ?? []))
      .pipe(
        map((partners: IPartner[]) => this.partnerService.addPartnerToCollectionIfMissing(partners, this.editForm.get('partner')!.value))
      )
      .subscribe((partners: IPartner[]) => (this.partnersSharedCollection = partners));

    this.personService
      .query()
      .pipe(map((res: HttpResponse<IPerson[]>) => res.body ?? []))
      .pipe(map((people: IPerson[]) => this.personService.addPersonToCollectionIfMissing(people, this.editForm.get('person')!.value)))
      .subscribe((people: IPerson[]) => (this.peopleSharedCollection = people));
  }

  protected createFromForm(): IParty {
    return {
      ...new Party(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      photoContentType: this.editForm.get(['photoContentType'])!.value,
      photo: this.editForm.get(['photo'])!.value,
      partyCode: this.editForm.get(['partyCode'])!.value,
      tradeTitle: this.editForm.get(['tradeTitle'])!.value,
      activationDate: this.editForm.get(['activationDate'])!.value,
      expirationDate: this.editForm.get(['expirationDate'])!.value,
      activationStatus: this.editForm.get(['activationStatus'])!.value,
      lat: this.editForm.get(['lat'])!.value,
      lon: this.editForm.get(['lon'])!.value,
      address: this.editForm.get(['address'])!.value,
      postalCode: this.editForm.get(['postalCode'])!.value,
      mobile: this.editForm.get(['mobile'])!.value,
      partyTypeClassId: this.editForm.get(['partyTypeClassId'])!.value,
      description: this.editForm.get(['description'])!.value,
      parent: this.editForm.get(['parent'])!.value,
      partner: this.editForm.get(['partner'])!.value,
      person: this.editForm.get(['person'])!.value,
    };
  }
}
