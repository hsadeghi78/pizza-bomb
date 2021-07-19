import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PartyInformationDetailComponent } from './party-information-detail.component';

describe('Component Tests', () => {
  describe('PartyInformation Management Detail Component', () => {
    let comp: PartyInformationDetailComponent;
    let fixture: ComponentFixture<PartyInformationDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PartyInformationDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ partyInformation: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PartyInformationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PartyInformationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load partyInformation on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.partyInformation).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
