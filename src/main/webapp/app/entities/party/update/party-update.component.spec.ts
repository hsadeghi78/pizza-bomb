jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PartyService } from '../service/party.service';
import { IParty, Party } from '../party.model';

import { PartyUpdateComponent } from './party-update.component';

describe('Component Tests', () => {
  describe('Party Management Update Component', () => {
    let comp: PartyUpdateComponent;
    let fixture: ComponentFixture<PartyUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let partyService: PartyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PartyUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PartyUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PartyUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      partyService = TestBed.inject(PartyService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const party: IParty = { id: 456 };

        activatedRoute.data = of({ party });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(party));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const party = { id: 123 };
        spyOn(partyService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ party });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: party }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(partyService.update).toHaveBeenCalledWith(party);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const party = new Party();
        spyOn(partyService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ party });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: party }));
        saveSubject.complete();

        // THEN
        expect(partyService.create).toHaveBeenCalledWith(party);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const party = { id: 123 };
        spyOn(partyService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ party });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(partyService.update).toHaveBeenCalledWith(party);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
