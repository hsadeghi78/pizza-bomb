import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFileDocument } from '../file-document.model';
import { FileDocumentService } from '../service/file-document.service';

@Component({
  templateUrl: './file-document-delete-dialog.component.html',
})
export class FileDocumentDeleteDialogComponent {
  fileDocument?: IFileDocument;

  constructor(protected fileDocumentService: FileDocumentService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.fileDocumentService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
