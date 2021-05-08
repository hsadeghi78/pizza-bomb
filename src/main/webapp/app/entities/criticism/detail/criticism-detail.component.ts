import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICriticism } from '../criticism.model';

@Component({
  selector: 'jhi-criticism-detail',
  templateUrl: './criticism-detail.component.html',
})
export class CriticismDetailComponent implements OnInit {
  criticism: ICriticism | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ criticism }) => {
      this.criticism = criticism;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
