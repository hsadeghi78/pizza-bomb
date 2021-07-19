import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IConsumeMaterial } from '../consume-material.model';

@Component({
  selector: 'jhi-consume-material-detail',
  templateUrl: './consume-material-detail.component.html',
})
export class ConsumeMaterialDetailComponent implements OnInit {
  consumeMaterial: IConsumeMaterial | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ consumeMaterial }) => {
      this.consumeMaterial = consumeMaterial;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
