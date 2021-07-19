import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IMenuItem, MenuItem } from '../menu-item.model';
import { MenuItemService } from '../service/menu-item.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';
import { IFood } from 'app/entities/food/food.model';
import { FoodService } from 'app/entities/food/service/food.service';

@Component({
  selector: 'jhi-menu-item-update',
  templateUrl: './menu-item-update.component.html',
})
export class MenuItemUpdateComponent implements OnInit {
  isSaving = false;

  partiesSharedCollection: IParty[] = [];
  foodsSharedCollection: IFood[] = [];

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required, Validators.maxLength(200)]],
    expirationDate: [null, [Validators.required]],
    description: [null, [Validators.maxLength(1000)]],
    party: [null, Validators.required],
    food: [null, Validators.required],
  });

  constructor(
    protected menuItemService: MenuItemService,
    protected partyService: PartyService,
    protected foodService: FoodService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ menuItem }) => {
      if (menuItem.id === undefined) {
        const today = dayjs().startOf('day');
        menuItem.expirationDate = today;
      }

      this.updateForm(menuItem);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const menuItem = this.createFromForm();
    if (menuItem.id !== undefined) {
      this.subscribeToSaveResponse(this.menuItemService.update(menuItem));
    } else {
      this.subscribeToSaveResponse(this.menuItemService.create(menuItem));
    }
  }

  trackPartyById(index: number, item: IParty): number {
    return item.id!;
  }

  trackFoodById(index: number, item: IFood): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMenuItem>>): void {
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

  protected updateForm(menuItem: IMenuItem): void {
    this.editForm.patchValue({
      id: menuItem.id,
      title: menuItem.title,
      expirationDate: menuItem.expirationDate ? menuItem.expirationDate.format(DATE_TIME_FORMAT) : null,
      description: menuItem.description,
      party: menuItem.party,
      food: menuItem.food,
    });

    this.partiesSharedCollection = this.partyService.addPartyToCollectionIfMissing(this.partiesSharedCollection, menuItem.party);
    this.foodsSharedCollection = this.foodService.addFoodToCollectionIfMissing(this.foodsSharedCollection, menuItem.food);
  }

  protected loadRelationshipsOptions(): void {
    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing(parties, this.editForm.get('party')!.value)))
      .subscribe((parties: IParty[]) => (this.partiesSharedCollection = parties));

    this.foodService
      .query()
      .pipe(map((res: HttpResponse<IFood[]>) => res.body ?? []))
      .pipe(map((foods: IFood[]) => this.foodService.addFoodToCollectionIfMissing(foods, this.editForm.get('food')!.value)))
      .subscribe((foods: IFood[]) => (this.foodsSharedCollection = foods));
  }

  protected createFromForm(): IMenuItem {
    return {
      ...new MenuItem(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      expirationDate: this.editForm.get(['expirationDate'])!.value
        ? dayjs(this.editForm.get(['expirationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      description: this.editForm.get(['description'])!.value,
      party: this.editForm.get(['party'])!.value,
      food: this.editForm.get(['food'])!.value,
    };
  }
}
