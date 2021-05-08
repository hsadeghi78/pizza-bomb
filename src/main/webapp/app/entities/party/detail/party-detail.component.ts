import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IParty } from '../party.model';

@Component({
  selector: 'jhi-party-detail',
  templateUrl: './party-detail.component.html',
})
export class PartyDetailComponent implements OnInit {
  party: IParty | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ party }) => {
      this.party = party;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
