import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFileDocument, FileDocument } from '../file-document.model';

import { FileDocumentService } from './file-document.service';

describe('Service Tests', () => {
  describe('FileDocument Service', () => {
    let service: FileDocumentService;
    let httpMock: HttpTestingController;
    let elemDefault: IFileDocument;
    let expectedResult: IFileDocument | IFileDocument[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FileDocumentService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        fileName: 'AAAAAAA',
        fileContentContentType: 'image/png',
        fileContent: 'AAAAAAA',
        filePath: 'AAAAAAA',
        description: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a FileDocument', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new FileDocument()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a FileDocument', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            fileName: 'BBBBBB',
            fileContent: 'BBBBBB',
            filePath: 'BBBBBB',
            description: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a FileDocument', () => {
        const patchObject = Object.assign(
          {
            fileName: 'BBBBBB',
            description: 'BBBBBB',
          },
          new FileDocument()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of FileDocument', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            fileName: 'BBBBBB',
            fileContent: 'BBBBBB',
            filePath: 'BBBBBB',
            description: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a FileDocument', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFileDocumentToCollectionIfMissing', () => {
        it('should add a FileDocument to an empty array', () => {
          const fileDocument: IFileDocument = { id: 123 };
          expectedResult = service.addFileDocumentToCollectionIfMissing([], fileDocument);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(fileDocument);
        });

        it('should not add a FileDocument to an array that contains it', () => {
          const fileDocument: IFileDocument = { id: 123 };
          const fileDocumentCollection: IFileDocument[] = [
            {
              ...fileDocument,
            },
            { id: 456 },
          ];
          expectedResult = service.addFileDocumentToCollectionIfMissing(fileDocumentCollection, fileDocument);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a FileDocument to an array that doesn't contain it", () => {
          const fileDocument: IFileDocument = { id: 123 };
          const fileDocumentCollection: IFileDocument[] = [{ id: 456 }];
          expectedResult = service.addFileDocumentToCollectionIfMissing(fileDocumentCollection, fileDocument);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(fileDocument);
        });

        it('should add only unique FileDocument to an array', () => {
          const fileDocumentArray: IFileDocument[] = [{ id: 123 }, { id: 456 }, { id: 8848 }];
          const fileDocumentCollection: IFileDocument[] = [{ id: 123 }];
          expectedResult = service.addFileDocumentToCollectionIfMissing(fileDocumentCollection, ...fileDocumentArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const fileDocument: IFileDocument = { id: 123 };
          const fileDocument2: IFileDocument = { id: 456 };
          expectedResult = service.addFileDocumentToCollectionIfMissing([], fileDocument, fileDocument2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(fileDocument);
          expect(expectedResult).toContain(fileDocument2);
        });

        it('should accept null and undefined values', () => {
          const fileDocument: IFileDocument = { id: 123 };
          expectedResult = service.addFileDocumentToCollectionIfMissing([], null, fileDocument, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(fileDocument);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
