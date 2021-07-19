jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ClassificationService } from '../service/classification.service';
import { IClassification, Classification } from '../classification.model';
import { IClassType } from 'app/entities/class-type/class-type.model';
import { ClassTypeService } from 'app/entities/class-type/service/class-type.service';

import { ClassificationUpdateComponent } from './classification-update.component';

describe('Component Tests', () => {
  describe('Classification Management Update Component', () => {
    let comp: ClassificationUpdateComponent;
    let fixture: ComponentFixture<ClassificationUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let classificationService: ClassificationService;
    let classTypeService: ClassTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ClassificationUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ClassificationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClassificationUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      classificationService = TestBed.inject(ClassificationService);
      classTypeService = TestBed.inject(ClassTypeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ClassType query and add missing value', () => {
        const classification: IClassification = { id: 456 };
        const classType: IClassType = { id: 51126 };
        classification.classType = classType;

        const classTypeCollection: IClassType[] = [{ id: 48402 }];
        spyOn(classTypeService, 'query').and.returnValue(of(new HttpResponse({ body: classTypeCollection })));
        const additionalClassTypes = [classType];
        const expectedCollection: IClassType[] = [...additionalClassTypes, ...classTypeCollection];
        spyOn(classTypeService, 'addClassTypeToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ classification });
        comp.ngOnInit();

        expect(classTypeService.query).toHaveBeenCalled();
        expect(classTypeService.addClassTypeToCollectionIfMissing).toHaveBeenCalledWith(classTypeCollection, ...additionalClassTypes);
        expect(comp.classTypesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const classification: IClassification = { id: 456 };
        const classType: IClassType = { id: 3159 };
        classification.classType = classType;

        activatedRoute.data = of({ classification });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(classification));
        expect(comp.classTypesSharedCollection).toContain(classType);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const classification = { id: 123 };
        spyOn(classificationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ classification });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: classification }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(classificationService.update).toHaveBeenCalledWith(classification);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const classification = new Classification();
        spyOn(classificationService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ classification });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: classification }));
        saveSubject.complete();

        // THEN
        expect(classificationService.create).toHaveBeenCalledWith(classification);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const classification = { id: 123 };
        spyOn(classificationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ classification });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(classificationService.update).toHaveBeenCalledWith(classification);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackClassTypeById', () => {
        it('Should return tracked ClassType primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackClassTypeById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
