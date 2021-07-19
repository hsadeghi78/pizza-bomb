import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFileDocument, FileDocument } from '../file-document.model';
import { FileDocumentService } from '../service/file-document.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IParty } from 'app/entities/party/party.model';
import { PartyService } from 'app/entities/party/service/party.service';

@Component({
  selector: 'jhi-file-document-update',
  templateUrl: './file-document-update.component.html',
})
export class FileDocumentUpdateComponent implements OnInit {
  isSaving = false;

  partiesSharedCollection: IParty[] = [];

  editForm = this.fb.group({
    id: [],
    fileName: [null, [Validators.required, Validators.maxLength(250)]],
    fileContent: [],
    fileContentContentType: [],
    filePath: [null, [Validators.maxLength(2000)]],
    description: [null, [Validators.required, Validators.maxLength(3000)]],
    party: [null, Validators.required],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected fileDocumentService: FileDocumentService,
    protected partyService: PartyService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fileDocument }) => {
      this.updateForm(fileDocument);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('bombApp.error', { ...err, key: 'error.file.' + err.key })
        ),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fileDocument = this.createFromForm();
    if (fileDocument.id !== undefined) {
      this.subscribeToSaveResponse(this.fileDocumentService.update(fileDocument));
    } else {
      this.subscribeToSaveResponse(this.fileDocumentService.create(fileDocument));
    }
  }

  trackPartyById(index: number, item: IParty): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFileDocument>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(fileDocument: IFileDocument): void {
    this.editForm.patchValue({
      id: fileDocument.id,
      fileName: fileDocument.fileName,
      fileContent: fileDocument.fileContent,
      fileContentContentType: fileDocument.fileContentContentType,
      filePath: fileDocument.filePath,
      description: fileDocument.description,
      party: fileDocument.party,
    });

    this.partiesSharedCollection = this.partyService.addPartyToCollectionIfMissing(this.partiesSharedCollection, fileDocument.party);
  }

  protected loadRelationshipsOptions(): void {
    this.partyService
      .query()
      .pipe(map((res: HttpResponse<IParty[]>) => res.body ?? []))
      .pipe(map((parties: IParty[]) => this.partyService.addPartyToCollectionIfMissing(parties, this.editForm.get('party')!.value)))
      .subscribe((parties: IParty[]) => (this.partiesSharedCollection = parties));
  }

  protected createFromForm(): IFileDocument {
    return {
      ...new FileDocument(),
      id: this.editForm.get(['id'])!.value,
      fileName: this.editForm.get(['fileName'])!.value,
      fileContentContentType: this.editForm.get(['fileContentContentType'])!.value,
      fileContent: this.editForm.get(['fileContent'])!.value,
      filePath: this.editForm.get(['filePath'])!.value,
      description: this.editForm.get(['description'])!.value,
      party: this.editForm.get(['party'])!.value,
    };
  }
}
