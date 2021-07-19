import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFactorStatusHistory } from '../factor-status-history.model';

@Component({
  selector: 'jhi-factor-status-history-detail',
  templateUrl: './factor-status-history-detail.component.html',
})
export class FactorStatusHistoryDetailComponent implements OnInit {
  factorStatusHistory: IFactorStatusHistory | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ factorStatusHistory }) => {
      this.factorStatusHistory = factorStatusHistory;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
