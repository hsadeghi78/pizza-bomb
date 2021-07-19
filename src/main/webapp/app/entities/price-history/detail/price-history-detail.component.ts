import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPriceHistory } from '../price-history.model';

@Component({
  selector: 'jhi-price-history-detail',
  templateUrl: './price-history-detail.component.html',
})
export class PriceHistoryDetailComponent implements OnInit {
  priceHistory: IPriceHistory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ priceHistory }) => {
      this.priceHistory = priceHistory;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
