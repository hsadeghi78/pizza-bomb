import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFoodType, FoodType } from '../food-type.model';
import { FoodTypeService } from '../service/food-type.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';

@Component({
  selector: 'jhi-food-type-update',
  templateUrl: './food-type-update.component.html',
})
export class FoodTypeUpdateComponent implements OnInit {
  isSaving = false;

  partiesSharedCollection: IParty[] = [];

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required, Validators.maxLength(200)]],
    typeCode: [null, [Validators.required, Validators.maxLength(50)]],
    description: [null, [Validators.maxLength(3000)]],
    party: [null, Validators.required],
  });

  constructor(
    protected foodTypeService: FoodTypeService,
    protected partyService: PartyService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ foodType }) => {
      this.updateForm(foodType);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const foodType = this.createFromForm();
    if (foodType.id !== undefined) {
      this.subscribeToSaveResponse(this.foodTypeService.update(foodType));
    } else {
      this.subscribeToSaveResponse(this.foodTypeService.create(foodType));
    }
  }

  trackPartyById(index: number, item: IParty): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFoodType>>): void {
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

  protected updateForm(foodType: IFoodType): void {
    this.editForm.patchValue({
      id: foodType.id,
      title: foodType.title,
      typeCode: foodType.typeCode,
      description: foodType.description,
      party: foodType.party,
    });

    this.partiesSharedCollection = this.partyService.addPartyToCollectionIfMissing(this.partiesSharedCollection, foodType.party);
  }

  protected loadRelationshipsOptions(): void {
    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing(parties, this.editForm.get('party')!.value)))
      .subscribe((parties: IParty[]) => (this.partiesSharedCollection = parties));
  }

  protected createFromForm(): IFoodType {
    return {
      ...new FoodType(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      typeCode: this.editForm.get(['typeCode'])!.value,
      description: this.editForm.get(['description'])!.value,
      party: this.editForm.get(['party'])!.value,
    };
  }
}
