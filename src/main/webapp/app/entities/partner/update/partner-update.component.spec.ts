jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PartnerService } from '../service/partner.service';
import { IPartner, Partner } from '../partner.model';

import { PartnerUpdateComponent } from './partner-update.component';

describe('Component Tests', () => {
  describe('Partner Management Update Component', () => {
    let comp: PartnerUpdateComponent;
    let fixture: ComponentFixture<PartnerUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let partnerService: PartnerService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PartnerUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PartnerUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PartnerUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      partnerService = TestBed.inject(PartnerService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const partner: IPartner = { id: 456 };

        activatedRoute.data = of({ partner });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(partner));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const partner = { id: 123 };
        spyOn(partnerService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ partner });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: partner }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(partnerService.update).toHaveBeenCalledWith(partner);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const partner = new Partner();
        spyOn(partnerService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ partner });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: partner }));
        saveSubject.complete();

        // THEN
        expect(partnerService.create).toHaveBeenCalledWith(partner);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const partner = { id: 123 };
        spyOn(partnerService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ partner });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(partnerService.update).toHaveBeenCalledWith(partner);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
