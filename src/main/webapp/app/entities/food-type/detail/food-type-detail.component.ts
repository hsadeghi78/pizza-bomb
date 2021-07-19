import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFoodType } from '../food-type.model';

@Component({
  selector: 'jhi-food-type-detail',
  templateUrl: './food-type-detail.component.html',
})
export class FoodTypeDetailComponent implements OnInit {
  foodType: IFoodType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ foodType }) => {
      this.foodType = foodType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
