import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DataUtils } from 'app/core/util/data-util.service';

import { FileDocumentDetailComponent } from './file-document-detail.component';

describe('Component Tests', () => {
  describe('FileDocument Management Detail Component', () => {
    let comp: FileDocumentDetailComponent;
    let fixture: ComponentFixture<FileDocumentDetailComponent>;
    let dataUtils: DataUtils;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [FileDocumentDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ fileDocument: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(FileDocumentDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FileDocumentDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = TestBed.inject(DataUtils);
    });

    describe('OnInit', () => {
      it('Should load fileDocument on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.fileDocument).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeBase64, fakeContentType);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeBase64, fakeContentType);
      });
    });
  });
});
