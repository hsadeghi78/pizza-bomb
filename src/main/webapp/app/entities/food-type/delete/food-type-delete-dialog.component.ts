import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFoodType } from '../food-type.model';
import { FoodTypeService } from '../service/food-type.service';

@Component({
  templateUrl: './food-type-delete-dialog.component.html',
})
export class FoodTypeDeleteDialogComponent {
  foodType?: IFoodType;

  constructor(protected foodTypeService: FoodTypeService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.foodTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
