import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PartyDetailComponent } from './party-detail.component';

describe('Component Tests', () => {
  describe('Party Management Detail Component', () => {
    let comp: PartyDetailComponent;
    let fixture: ComponentFixture<PartyDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PartyDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ party: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PartyDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PartyDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load party on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.party).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
