import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFood } from '../food.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-food-detail',
  templateUrl: './food-detail.component.html',
})
export class FoodDetailComponent implements OnInit {
  food: IFood | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ food }) => {
      this.food = food;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
