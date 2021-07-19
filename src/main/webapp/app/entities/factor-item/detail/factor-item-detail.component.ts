import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFactorItem } from '../factor-item.model';

@Component({
  selector: 'jhi-factor-item-detail',
  templateUrl: './factor-item-detail.component.html',
})
export class FactorItemDetailComponent implements OnInit {
  factorItem: IFactorItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ factorItem }) => {
      this.factorItem = factorItem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
