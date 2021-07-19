import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IConsumeMaterial, ConsumeMaterial } from '../consume-material.model';

import { ConsumeMaterialService } from './consume-material.service';

describe('Service Tests', () => {
  describe('ConsumeMaterial Service', () => {
    let service: ConsumeMaterialService;
    let httpMock: HttpTestingController;
    let elemDefault: IConsumeMaterial;
    let expectedResult: IConsumeMaterial | IConsumeMaterial[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ConsumeMaterialService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        title: 'AAAAAAA',
        type: 'AAAAAAA',
        amount: 0,
        amountUnitClassId: 0,
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

      it('should create a ConsumeMaterial', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new ConsumeMaterial()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a ConsumeMaterial', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            type: 'BBBBBB',
            amount: 1,
            amountUnitClassId: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a ConsumeMaterial', () => {
        const patchObject = Object.assign(
          {
            type: 'BBBBBB',
            amount: 1,
          },
          new ConsumeMaterial()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of ConsumeMaterial', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            type: 'BBBBBB',
            amount: 1,
            amountUnitClassId: 1,
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

      it('should delete a ConsumeMaterial', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addConsumeMaterialToCollectionIfMissing', () => {
        it('should add a ConsumeMaterial to an empty array', () => {
          const consumeMaterial: IConsumeMaterial = { id: 123 };
          expectedResult = service.addConsumeMaterialToCollectionIfMissing([], consumeMaterial);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(consumeMaterial);
        });

        it('should not add a ConsumeMaterial to an array that contains it', () => {
          const consumeMaterial: IConsumeMaterial = { id: 123 };
          const consumeMaterialCollection: IConsumeMaterial[] = [
            {
              ...consumeMaterial,
            },
            { id: 456 },
          ];
          expectedResult = service.addConsumeMaterialToCollectionIfMissing(consumeMaterialCollection, consumeMaterial);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a ConsumeMaterial to an array that doesn't contain it", () => {
          const consumeMaterial: IConsumeMaterial = { id: 123 };
          const consumeMaterialCollection: IConsumeMaterial[] = [{ id: 456 }];
          expectedResult = service.addConsumeMaterialToCollectionIfMissing(consumeMaterialCollection, consumeMaterial);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(consumeMaterial);
        });

        it('should add only unique ConsumeMaterial to an array', () => {
          const consumeMaterialArray: IConsumeMaterial[] = [{ id: 123 }, { id: 456 }, { id: 14141 }];
          const consumeMaterialCollection: IConsumeMaterial[] = [{ id: 123 }];
          expectedResult = service.addConsumeMaterialToCollectionIfMissing(consumeMaterialCollection, ...consumeMaterialArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const consumeMaterial: IConsumeMaterial = { id: 123 };
          const consumeMaterial2: IConsumeMaterial = { id: 456 };
          expectedResult = service.addConsumeMaterialToCollectionIfMissing([], consumeMaterial, consumeMaterial2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(consumeMaterial);
          expect(expectedResult).toContain(consumeMaterial2);
        });

        it('should accept null and undefined values', () => {
          const consumeMaterial: IConsumeMaterial = { id: 123 };
          expectedResult = service.addConsumeMaterialToCollectionIfMissing([], null, consumeMaterial, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(consumeMaterial);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
