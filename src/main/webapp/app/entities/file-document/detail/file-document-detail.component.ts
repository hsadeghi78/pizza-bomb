import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFileDocument } from '../file-document.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-file-document-detail',
  templateUrl: './file-document-detail.component.html',
})
export class FileDocumentDetailComponent implements OnInit {
  fileDocument: IFileDocument | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fileDocument }) => {
      this.fileDocument = fileDocument;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
