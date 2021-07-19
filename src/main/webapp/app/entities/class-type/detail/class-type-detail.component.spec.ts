import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ClassTypeDetailComponent } from './class-type-detail.component';

describe('Component Tests', () => {
  describe('ClassType Management Detail Component', () => {
    let comp: ClassTypeDetailComponent;
    let fixture: ComponentFixture<ClassTypeDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ClassTypeDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ classType: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ClassTypeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ClassTypeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load classType on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.classType).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
