import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { FactorStatus } from 'app/entities/enumerations/factor-status.model';
import { FactorOrderWay } from 'app/entities/enumerations/factor-order-way.model';
import { FactorServing } from 'app/entities/enumerations/factor-serving.model';
import { IFactor, Factor } from '../factor.model';

import { FactorService } from './factor.service';

describe('Service Tests', () => {
  describe('Factor Service', () => {
    let service: FactorService;
    let httpMock: HttpTestingController;
    let elemDefault: IFactor;
    let expectedResult: IFactor | IFactor[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FactorService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        title: 'AAAAAAA',
        factorCode: 'AAAAAAA',
        lastStatus: FactorStatus.INITIATE,
        orderWay: FactorOrderWay.PHONE_CALL,
        serving: FactorServing.INSIDE,
        paymentStateClassId: 0,
        categoryClassId: 0,
        totalPrice: 0,
        discount: 0,
        tax: 0,
        netprice: 0,
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

      it('should create a Factor', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Factor()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Factor', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            factorCode: 'BBBBBB',
            lastStatus: 'BBBBBB',
            orderWay: 'BBBBBB',
            serving: 'BBBBBB',
            paymentStateClassId: 1,
            categoryClassId: 1,
            totalPrice: 1,
            discount: 1,
            tax: 1,
            netprice: 1,
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

      it('should partial update a Factor', () => {
        const patchObject = Object.assign(
          {
            title: 'BBBBBB',
            serving: 'BBBBBB',
            paymentStateClassId: 1,
            categoryClassId: 1,
            discount: 1,
            tax: 1,
            description: 'BBBBBB',
          },
          new Factor()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Factor', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            factorCode: 'BBBBBB',
            lastStatus: 'BBBBBB',
            orderWay: 'BBBBBB',
            serving: 'BBBBBB',
            paymentStateClassId: 1,
            categoryClassId: 1,
            totalPrice: 1,
            discount: 1,
            tax: 1,
            netprice: 1,
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

      it('should delete a Factor', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFactorToCollectionIfMissing', () => {
        it('should add a Factor to an empty array', () => {
          const factor: IFactor = { id: 123 };
          expectedResult = service.addFactorToCollectionIfMissing([], factor);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(factor);
        });

        it('should not add a Factor to an array that contains it', () => {
          const factor: IFactor = { id: 123 };
          const factorCollection: IFactor[] = [
            {
              ...factor,
            },
            { id: 456 },
          ];
          expectedResult = service.addFactorToCollectionIfMissing(factorCollection, factor);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Factor to an array that doesn't contain it", () => {
          const factor: IFactor = { id: 123 };
          const factorCollection: IFactor[] = [{ id: 456 }];
          expectedResult = service.addFactorToCollectionIfMissing(factorCollection, factor);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(factor);
        });

        it('should add only unique Factor to an array', () => {
          const factorArray: IFactor[] = [{ id: 123 }, { id: 456 }, { id: 4814 }];
          const factorCollection: IFactor[] = [{ id: 123 }];
          expectedResult = service.addFactorToCollectionIfMissing(factorCollection, ...factorArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const factor: IFactor = { id: 123 };
          const factor2: IFactor = { id: 456 };
          expectedResult = service.addFactorToCollectionIfMissing([], factor, factor2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(factor);
          expect(expectedResult).toContain(factor2);
        });

        it('should accept null and undefined values', () => {
          const factor: IFactor = { id: 123 };
          expectedResult = service.addFactorToCollectionIfMissing([], null, factor, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(factor);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
