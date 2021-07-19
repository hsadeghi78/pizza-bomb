import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPartyInformation } from '../party-information.model';
import { PartyInformationService } from '../service/party-information.service';

@Component({
  templateUrl: './party-information-delete-dialog.component.html',
})
export class PartyInformationDeleteDialogComponent {
  partyInformation?: IPartyInformation;

  constructor(protected partyInformationService: PartyInformationService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.partyInformationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
