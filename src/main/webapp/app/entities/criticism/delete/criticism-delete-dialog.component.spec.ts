jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { CriticismService } from '../service/criticism.service';

import { CriticismDeleteDialogComponent } from './criticism-delete-dialog.component';

describe('Component Tests', () => {
  describe('Criticism Management Delete Component', () => {
    let comp: CriticismDeleteDialogComponent;
    let fixture: ComponentFixture<CriticismDeleteDialogComponent>;
    let service: CriticismService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CriticismDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(CriticismDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CriticismDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CriticismService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
