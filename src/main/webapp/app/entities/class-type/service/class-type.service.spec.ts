import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IClassType, ClassType } from '../class-type.model';

import { ClassTypeService } from './class-type.service';

describe('Service Tests', () => {
  describe('ClassType Service', () => {
    let service: ClassTypeService;
    let httpMock: HttpTestingController;
    let elemDefault: IClassType;
    let expectedResult: IClassType | IClassType[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ClassTypeService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        title: 'AAAAAAA',
        typeCode: 0,
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

      it('should create a ClassType', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ClassType()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ClassType', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            typeCode: 1,
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

      it('should partial update a ClassType', () => {
        const patchObject = Object.assign(
          {
            description: 'BBBBBB',
          },
          new ClassType()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ClassType', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            typeCode: 1,
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

      it('should delete a ClassType', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addClassTypeToCollectionIfMissing', () => {
        it('should add a ClassType to an empty array', () => {
          const classType: IClassType = { id: 123 };
          expectedResult = service.addClassTypeToCollectionIfMissing([], classType);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(classType);
        });

        it('should not add a ClassType to an array that contains it', () => {
          const classType: IClassType = { id: 123 };
          const classTypeCollection: IClassType[] = [
            {
              ...classType,
            },
            { id: 456 },
          ];
          expectedResult = service.addClassTypeToCollectionIfMissing(classTypeCollection, classType);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ClassType to an array that doesn't contain it", () => {
          const classType: IClassType = { id: 123 };
          const classTypeCollection: IClassType[] = [{ id: 456 }];
          expectedResult = service.addClassTypeToCollectionIfMissing(classTypeCollection, classType);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(classType);
        });

        it('should add only unique ClassType to an array', () => {
          const classTypeArray: IClassType[] = [{ id: 123 }, { id: 456 }, { id: 85165 }];
          const classTypeCollection: IClassType[] = [{ id: 123 }];
          expectedResult = service.addClassTypeToCollectionIfMissing(classTypeCollection, ...classTypeArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const classType: IClassType = { id: 123 };
          const classType2: IClassType = { id: 456 };
          expectedResult = service.addClassTypeToCollectionIfMissing([], classType, classType2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(classType);
          expect(expectedResult).toContain(classType2);
        });

        it('should accept null and undefined values', () => {
          const classType: IClassType = { id: 123 };
          expectedResult = service.addClassTypeToCollectionIfMissing([], null, classType, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(classType);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
