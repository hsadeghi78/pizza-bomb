import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClassType } from '../class-type.model';

@Component({
  selector: 'jhi-class-type-detail',
  templateUrl: './class-type-detail.component.html',
})
export class ClassTypeDetailComponent implements OnInit {
  classType: IClassType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classType }) => {
      this.classType = classType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
