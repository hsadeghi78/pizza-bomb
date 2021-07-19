import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPriceHistory, PriceHistory } from '../price-history.model';

import { PriceHistoryService } from './price-history.service';

describe('Service Tests', () => {
  describe('PriceHistory Service', () => {
    let service: PriceHistoryService;
    let httpMock: HttpTestingController;
    let elemDefault: IPriceHistory;
    let expectedResult: IPriceHistory | IPriceHistory[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PriceHistoryService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        foodId: 0,
        materialId: 0,
        price: 0,
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

      it('should create a PriceHistory', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new PriceHistory()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PriceHistory', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            foodId: 1,
            materialId: 1,
            price: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a PriceHistory', () => {
        const patchObject = Object.assign(
          {
            materialId: 1,
          },
          new PriceHistory()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PriceHistory', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            foodId: 1,
            materialId: 1,
            price: 1,
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

      it('should delete a PriceHistory', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPriceHistoryToCollectionIfMissing', () => {
        it('should add a PriceHistory to an empty array', () => {
          const priceHistory: IPriceHistory = { id: 123 };
          expectedResult = service.addPriceHistoryToCollectionIfMissing([], priceHistory);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(priceHistory);
        });

        it('should not add a PriceHistory to an array that contains it', () => {
          const priceHistory: IPriceHistory = { id: 123 };
          const priceHistoryCollection: IPriceHistory[] = [
            {
              ...priceHistory,
            },
            { id: 456 },
          ];
          expectedResult = service.addPriceHistoryToCollectionIfMissing(priceHistoryCollection, priceHistory);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a PriceHistory to an array that doesn't contain it", () => {
          const priceHistory: IPriceHistory = { id: 123 };
          const priceHistoryCollection: IPriceHistory[] = [{ id: 456 }];
          expectedResult = service.addPriceHistoryToCollectionIfMissing(priceHistoryCollection, priceHistory);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(priceHistory);
        });

        it('should add only unique PriceHistory to an array', () => {
          const priceHistoryArray: IPriceHistory[] = [{ id: 123 }, { id: 456 }, { id: 27160 }];
          const priceHistoryCollection: IPriceHistory[] = [{ id: 123 }];
          expectedResult = service.addPriceHistoryToCollectionIfMissing(priceHistoryCollection, ...priceHistoryArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const priceHistory: IPriceHistory = { id: 123 };
          const priceHistory2: IPriceHistory = { id: 456 };
          expectedResult = service.addPriceHistoryToCollectionIfMissing([], priceHistory, priceHistory2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(priceHistory);
          expect(expectedResult).toContain(priceHistory2);
        });

        it('should accept null and undefined values', () => {
          const priceHistory: IPriceHistory = { id: 123 };
          expectedResult = service.addPriceHistoryToCollectionIfMissing([], null, priceHistory, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(priceHistory);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
