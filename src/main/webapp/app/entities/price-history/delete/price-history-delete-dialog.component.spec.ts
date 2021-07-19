jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { PriceHistoryService } from '../service/price-history.service';

import { PriceHistoryDeleteDialogComponent } from './price-history-delete-dialog.component';

describe('Component Tests', () => {
  describe('PriceHistory Management Delete Component', () => {
    let comp: PriceHistoryDeleteDialogComponent;
    let fixture: ComponentFixture<PriceHistoryDeleteDialogComponent>;
    let service: PriceHistoryService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PriceHistoryDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(PriceHistoryDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PriceHistoryDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PriceHistoryService);
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
