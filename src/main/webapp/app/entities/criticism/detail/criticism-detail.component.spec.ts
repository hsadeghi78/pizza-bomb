import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CriticismDetailComponent } from './criticism-detail.component';

describe('Component Tests', () => {
  describe('Criticism Management Detail Component', () => {
    let comp: CriticismDetailComponent;
    let fixture: ComponentFixture<CriticismDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CriticismDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ criticism: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CriticismDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CriticismDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load criticism on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.criticism).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
