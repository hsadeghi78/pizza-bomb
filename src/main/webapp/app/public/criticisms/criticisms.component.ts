import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { CriticismService } from 'app/entities/criticism/service/criticism.service';
import { ActivatedRoute } from '@angular/router';
import { IParty } from 'app/entities/party/party.model';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { Criticism, ICriticism } from 'app/entities/criticism/criticism.model';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'jhi-criticisms',
  templateUrl: './criticisms.component.html',
  styleUrls: ['./criticisms.component.scss'],
})
export class CriticismsComponent implements OnInit {
  editForm = this.fb.group({
    id: [],
    fullName: [null, [Validators.required, Validators.maxLength(150)]],
    email: [null, [Validators.maxLength(150)]],
    contactNumber: [null, [Validators.maxLength(15)]],
    description: [null, [Validators.required, Validators.maxLength(3000)]],
  });

  constructor(protected criticismService: CriticismService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    console.warn('dddddddddddd');
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    const criticism = this.createFromForm();
    this.subscribeToSaveResponse(this.criticismService.create(criticism));
  }

  trackPartyById(index: number, item: IParty): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICriticism>>): void {
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
    // this.isSaving = false;
  }

  protected createFromForm(): ICriticism {
    return {
      ...new Criticism(),
      id: this.editForm.get(['id'])!.value,
      fullName: this.editForm.get(['fullName'])!.value,
      email: this.editForm.get(['email'])!.value,
      contactNumber: this.editForm.get(['contactNumber'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }
}
