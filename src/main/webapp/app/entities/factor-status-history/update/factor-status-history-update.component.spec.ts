jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { FactorStatusHistoryService } from '../service/factor-status-history.service';
import { IFactorStatusHistory, FactorStatusHistory } from '../factor-status-history.model';

import { FactorStatusHistoryUpdateComponent } from './factor-status-history-update.component';

describe('Component Tests', () => {
  describe('FactorStatusHistory Management Update Component', () => {
    let comp: FactorStatusHistoryUpdateComponent;
    let fixture: ComponentFixture<FactorStatusHistoryUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let factorStatusHistoryService: FactorStatusHistoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FactorStatusHistoryUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(FactorStatusHistoryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FactorStatusHistoryUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      factorStatusHistoryService = TestBed.inject(FactorStatusHistoryService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const factorStatusHistory: IFactorStatusHistory = { id: 456 };

        activatedRoute.data = of({ factorStatusHistory });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(factorStatusHistory));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const factorStatusHistory = { id: 123 };
        spyOn(factorStatusHistoryService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ factorStatusHistory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: factorStatusHistory }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(factorStatusHistoryService.update).toHaveBeenCalledWith(factorStatusHistory);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const factorStatusHistory = new FactorStatusHistory();
        spyOn(factorStatusHistoryService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ factorStatusHistory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: factorStatusHistory }));
        saveSubject.complete();

        // THEN
        expect(factorStatusHistoryService.create).toHaveBeenCalledWith(factorStatusHistory);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const factorStatusHistory = { id: 123 };
        spyOn(factorStatusHistoryService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ factorStatusHistory });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(factorStatusHistoryService.update).toHaveBeenCalledWith(factorStatusHistory);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
