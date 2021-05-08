import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CriticismComponent } from './list/criticism.component';
import { CriticismDetailComponent } from './detail/criticism-detail.component';
import { CriticismUpdateComponent } from './update/criticism-update.component';
import { CriticismDeleteDialogComponent } from './delete/criticism-delete-dialog.component';
import { CriticismRoutingModule } from './route/criticism-routing.module';

@NgModule({
  imports: [SharedModule, CriticismRoutingModule],
  declarations: [CriticismComponent, CriticismDetailComponent, CriticismUpdateComponent, CriticismDeleteDialogComponent],
  entryComponents: [CriticismDeleteDialogComponent],
})
export class CriticismModule {}
