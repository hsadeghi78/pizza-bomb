import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { FileDocumentComponent } from './list/file-document.component';
import { FileDocumentDetailComponent } from './detail/file-document-detail.component';
import { FileDocumentUpdateComponent } from './update/file-document-update.component';
import { FileDocumentDeleteDialogComponent } from './delete/file-document-delete-dialog.component';
import { FileDocumentRoutingModule } from './route/file-document-routing.module';

@NgModule({
  imports: [SharedModule, FileDocumentRoutingModule],
  declarations: [FileDocumentComponent, FileDocumentDetailComponent, FileDocumentUpdateComponent, FileDocumentDeleteDialogComponent],
  entryComponents: [FileDocumentDeleteDialogComponent],
})
export class FileDocumentModule {}
