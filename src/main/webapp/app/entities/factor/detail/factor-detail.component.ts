import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFactor } from '../factor.model';

@Component({
  selector: 'jhi-factor-detail',
  templateUrl: './factor-detail.component.html',
})
export class FactorDetailComponent implements OnInit {
  factor: IFactor | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ factor }) => {
      this.factor = factor;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
