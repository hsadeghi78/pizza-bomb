import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IClassType } from '../class-type.model';
import { ClassTypeService } from '../service/class-type.service';

@Component({
  templateUrl: './class-type-delete-dialog.component.html',
})
export class ClassTypeDeleteDialogComponent {
  classType?: IClassType;

  constructor(protected classTypeService: ClassTypeService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.classTypeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
