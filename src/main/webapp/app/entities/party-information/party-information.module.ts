import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { PartyInformationComponent } from './list/party-information.component';
import { PartyInformationDetailComponent } from './detail/party-information-detail.component';
import { PartyInformationUpdateComponent } from './update/party-information-update.component';
import { PartyInformationDeleteDialogComponent } from './delete/party-information-delete-dialog.component';
import { PartyInformationRoutingModule } from './route/party-information-routing.module';

@NgModule({
  imports: [SharedModule, PartyInformationRoutingModule],
  declarations: [
    PartyInformationComponent,
    PartyInformationDetailComponent,
    PartyInformationUpdateComponent,
    PartyInformationDeleteDialogComponent,
  ],
  entryComponents: [PartyInformationDeleteDialogComponent],
})
export class PartyInformationModule {}
