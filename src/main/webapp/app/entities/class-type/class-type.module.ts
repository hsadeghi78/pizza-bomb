import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ClassTypeComponent } from './list/class-type.component';
import { ClassTypeDetailComponent } from './detail/class-type-detail.component';
import { ClassTypeUpdateComponent } from './update/class-type-update.component';
import { ClassTypeDeleteDialogComponent } from './delete/class-type-delete-dialog.component';
import { ClassTypeRoutingModule } from './route/class-type-routing.module';

@NgModule({
  imports: [SharedModule, ClassTypeRoutingModule],
  declarations: [ClassTypeComponent, ClassTypeDetailComponent, ClassTypeUpdateComponent, ClassTypeDeleteDialogComponent],
  entryComponents: [ClassTypeDeleteDialogComponent],
})
export class ClassTypeModule {}
