import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPartyInformation } from '../party-information.model';

@Component({
  selector: 'jhi-party-information-detail',
  templateUrl: './party-information-detail.component.html',
})
export class PartyInformationDetailComponent implements OnInit {
  partyInformation: IPartyInformation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ partyInformation }) => {
      this.partyInformation = partyInformation;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
