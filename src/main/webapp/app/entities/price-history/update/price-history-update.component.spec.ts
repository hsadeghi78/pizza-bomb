jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PriceHistoryService } from '../service/price-history.service';
import { IPriceHistory, PriceHistory } from '../price-history.model';

import { PriceHistoryUpdateComponent } from './price-history-update.component';

describe('Component Tests', () => {
  describe('PriceHistory Management Update Component', () => {
    let comp: PriceHistoryUpdateComponent;
    let fixture: ComponentFixture<PriceHistoryUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let priceHistoryService: PriceHistoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PriceHistoryUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PriceHistoryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PriceHistoryUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      priceHistoryService = TestBed.inject(PriceHistoryService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const priceHistory: IPriceHistory = { id: 456 };

        activatedRoute.data = of({ priceHistory });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(priceHistory));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const priceHistory = { id: 123 };
        spyOn(priceHistoryService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ priceHistory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: priceHistory }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(priceHistoryService.update).toHaveBeenCalledWith(priceHistory);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const priceHistory = new PriceHistory();
        spyOn(priceHistoryService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ priceHistory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: priceHistory }));
        saveSubject.complete();

        // THEN
        expect(priceHistoryService.create).toHaveBeenCalledWith(priceHistory);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const priceHistory = { id: 123 };
        spyOn(priceHistoryService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ priceHistory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(priceHistoryService.update).toHaveBeenCalledWith(priceHistory);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
