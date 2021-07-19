import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IClassification, Classification } from '../classification.model';

import { ClassificationService } from './classification.service';

describe('Service Tests', () => {
  describe('Classification Service', () => {
    let service: ClassificationService;
    let httpMock: HttpTestingController;
    let elemDefault: IClassification;
    let expectedResult: IClassification | IClassification[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ClassificationService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        title: 'AAAAAAA',
        classCode: 0,
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

      it('should create a Classification', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Classification()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Classification', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            classCode: 1,
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

      it('should partial update a Classification', () => {
        const patchObject = Object.assign(
          {
            title: 'BBBBBB',
            classCode: 1,
          },
          new Classification()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Classification', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            classCode: 1,
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

      it('should delete a Classification', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addClassificationToCollectionIfMissing', () => {
        it('should add a Classification to an empty array', () => {
          const classification: IClassification = { id: 123 };
          expectedResult = service.addClassificationToCollectionIfMissing([], classification);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(classification);
        });

        it('should not add a Classification to an array that contains it', () => {
          const classification: IClassification = { id: 123 };
          const classificationCollection: IClassification[] = [
            {
              ...classification,
            },
            { id: 456 },
          ];
          expectedResult = service.addClassificationToCollectionIfMissing(classificationCollection, classification);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Classification to an array that doesn't contain it", () => {
          const classification: IClassification = { id: 123 };
          const classificationCollection: IClassification[] = [{ id: 456 }];
          expectedResult = service.addClassificationToCollectionIfMissing(classificationCollection, classification);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(classification);
        });

        it('should add only unique Classification to an array', () => {
          const classificationArray: IClassification[] = [{ id: 123 }, { id: 456 }, { id: 99054 }];
          const classificationCollection: IClassification[] = [{ id: 123 }];
          expectedResult = service.addClassificationToCollectionIfMissing(classificationCollection, ...classificationArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const classification: IClassification = { id: 123 };
          const classification2: IClassification = { id: 456 };
          expectedResult = service.addClassificationToCollectionIfMissing([], classification, classification2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(classification);
          expect(expectedResult).toContain(classification2);
        });

        it('should accept null and undefined values', () => {
          const classification: IClassification = { id: 123 };
          expectedResult = service.addClassificationToCollectionIfMissing([], null, classification, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(classification);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
