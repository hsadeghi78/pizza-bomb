jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ClassTypeService } from '../service/class-type.service';
import { IClassType, ClassType } from '../class-type.model';

import { ClassTypeUpdateComponent } from './class-type-update.component';

describe('Component Tests', () => {
  describe('ClassType Management Update Component', () => {
    let comp: ClassTypeUpdateComponent;
    let fixture: ComponentFixture<ClassTypeUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let classTypeService: ClassTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ClassTypeUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ClassTypeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClassTypeUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      classTypeService = TestBed.inject(ClassTypeService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const classType: IClassType = { id: 456 };

        activatedRoute.data = of({ classType });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(classType));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const classType = { id: 123 };
        spyOn(classTypeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ classType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: classType }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(classTypeService.update).toHaveBeenCalledWith(classType);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const classType = new ClassType();
        spyOn(classTypeService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ classType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: classType }));
        saveSubject.complete();

        // THEN
        expect(classTypeService.create).toHaveBeenCalledWith(classType);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const classType = { id: 123 };
        spyOn(classTypeService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ classType });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(classTypeService.update).toHaveBeenCalledWith(classType);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
