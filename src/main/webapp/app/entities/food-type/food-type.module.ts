import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { FoodTypeComponent } from './list/food-type.component';
import { FoodTypeDetailComponent } from './detail/food-type-detail.component';
import { FoodTypeUpdateComponent } from './update/food-type-update.component';
import { FoodTypeDeleteDialogComponent } from './delete/food-type-delete-dialog.component';
import { FoodTypeRoutingModule } from './route/food-type-routing.module';

@NgModule({
  imports: [SharedModule, FoodTypeRoutingModule],
  declarations: [FoodTypeComponent, FoodTypeDetailComponent, FoodTypeUpdateComponent, FoodTypeDeleteDialogComponent],
  entryComponents: [FoodTypeDeleteDialogComponent],
})
export class FoodTypeModule {}
