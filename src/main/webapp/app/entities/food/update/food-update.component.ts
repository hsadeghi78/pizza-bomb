import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFood, Food } from '../food.model';
import { FoodService } from '../service/food.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IFoodType } from 'app/entities/food-type/food-type.model';
import { FoodTypeService } from 'app/entities/food-type/service/food-type.service';

@Component({
  selector: 'jhi-food-update',
  templateUrl: './food-update.component.html',
})
export class FoodUpdateComponent implements OnInit {
  isSaving = false;

  partiesSharedCollection: IParty[] = [];
  foodTypesSharedCollection: IFoodType[] = [];

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required, Validators.maxLength(200)]],
    foodCode: [null, [Validators.required, Validators.maxLength(100)]],
    sizeClassId: [],
    photo: [],
    photoContentType: [],
    categoryClassId: [],
    lastPrice: [null, [Validators.required]],
    description: [null, [Validators.maxLength(3000)]],
    producerParty: [null, Validators.required],
    designerParty: [],
    foodType: [null, Validators.required],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected foodService: FoodService,
    protected partyService: PartyService,
    protected foodTypeService: FoodTypeService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ food }) => {
      this.updateForm(food);

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
    const food = this.createFromForm();
    if (food.id !== undefined) {
      this.subscribeToSaveResponse(this.foodService.update(food));
    } else {
      this.subscribeToSaveResponse(this.foodService.create(food));
    }
  }

  trackPartyById(index: number, item: IParty): number {
    return item.id!;
  }

  trackFoodTypeById(index: number, item: IFoodType): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFood>>): void {
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

  protected updateForm(food: IFood): void {
    this.editForm.patchValue({
      id: food.id,
      title: food.title,
      foodCode: food.foodCode,
      sizeClassId: food.sizeClassId,
      photo: food.photo,
      photoContentType: food.photoContentType,
      categoryClassId: food.categoryClassId,
      lastPrice: food.lastPrice,
      description: food.description,
      producerParty: food.producerParty,
      designerParty: food.designerParty,
      foodType: food.foodType,
    });

    this.partiesSharedCollection = this.partyService.addPartyToCollectionIfMissing(
      this.partiesSharedCollection,
      food.producerParty,
      food.designerParty
    );
    this.foodTypesSharedCollection = this.foodTypeService.addFoodTypeToCollectionIfMissing(this.foodTypesSharedCollection, food.foodType);
  }

  protected loadRelationshipsOptions(): void {
    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(
        map((parties: IParty[]) =>
          this.partyService.addPartyToCollectionIfMissing(
            parties,
            this.editForm.get('producerParty')!.value,
            this.editForm.get('designerParty')!.value
          )
        )
      )
      .subscribe((parties: IParty[]) => (this.partiesSharedCollection = parties));

    this.foodTypeService
      .query()
      .pipe(map((res: HttpResponse<IFoodType[]>) => res.body ?? []))
      .pipe(
        map((foodTypes: IFoodType[]) =>
          this.foodTypeService.addFoodTypeToCollectionIfMissing(foodTypes, this.editForm.get('foodType')!.value)
        )
      )
      .subscribe((foodTypes: IFoodType[]) => (this.foodTypesSharedCollection = foodTypes));
  }

  protected createFromForm(): IFood {
    return {
      ...new Food(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      foodCode: this.editForm.get(['foodCode'])!.value,
      sizeClassId: this.editForm.get(['sizeClassId'])!.value,
      photoContentType: this.editForm.get(['photoContentType'])!.value,
      photo: this.editForm.get(['photo'])!.value,
      categoryClassId: this.editForm.get(['categoryClassId'])!.value,
      lastPrice: this.editForm.get(['lastPrice'])!.value,
      description: this.editForm.get(['description'])!.value,
      producerParty: this.editForm.get(['producerParty'])!.value,
      designerParty: this.editForm.get(['designerParty'])!.value,
      foodType: this.editForm.get(['foodType'])!.value,
    };
  }
}
