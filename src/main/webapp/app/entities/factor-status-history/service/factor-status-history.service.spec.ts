import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { FactorStatus } from 'app/entities/enumerations/factor-status.model';
import { IFactorStatusHistory, FactorStatusHistory } from '../factor-status-history.model';

import { FactorStatusHistoryService } from './factor-status-history.service';

describe('Service Tests', () => {
  describe('FactorStatusHistory Service', () => {
    let service: FactorStatusHistoryService;
    let httpMock: HttpTestingController;
    let elemDefault: IFactorStatusHistory;
    let expectedResult: IFactorStatusHistory | IFactorStatusHistory[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FactorStatusHistoryService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        factorId: 0,
        status: FactorStatus.INITIATE,
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

      it('should create a FactorStatusHistory', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new FactorStatusHistory()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a FactorStatusHistory', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            factorId: 1,
            status: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a FactorStatusHistory', () => {
        const patchObject = Object.assign(
          {
            factorId: 1,
          },
          new FactorStatusHistory()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of FactorStatusHistory', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            factorId: 1,
            status: 'BBBBBB',
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

      it('should delete a FactorStatusHistory', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFactorStatusHistoryToCollectionIfMissing', () => {
        it('should add a FactorStatusHistory to an empty array', () => {
          const factorStatusHistory: IFactorStatusHistory = { id: 123 };
          expectedResult = service.addFactorStatusHistoryToCollectionIfMissing([], factorStatusHistory);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(factorStatusHistory);
        });

        it('should not add a FactorStatusHistory to an array that contains it', () => {
          const factorStatusHistory: IFactorStatusHistory = { id: 123 };
          const factorStatusHistoryCollection: IFactorStatusHistory[] = [
            {
              ...factorStatusHistory,
            },
            { id: 456 },
          ];
          expectedResult = service.addFactorStatusHistoryToCollectionIfMissing(factorStatusHistoryCollection, factorStatusHistory);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a FactorStatusHistory to an array that doesn't contain it", () => {
          const factorStatusHistory: IFactorStatusHistory = { id: 123 };
          const factorStatusHistoryCollection: IFactorStatusHistory[] = [{ id: 456 }];
          expectedResult = service.addFactorStatusHistoryToCollectionIfMissing(factorStatusHistoryCollection, factorStatusHistory);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(factorStatusHistory);
        });

        it('should add only unique FactorStatusHistory to an array', () => {
          const factorStatusHistoryArray: IFactorStatusHistory[] = [{ id: 123 }, { id: 456 }, { id: 69033 }];
          const factorStatusHistoryCollection: IFactorStatusHistory[] = [{ id: 123 }];
          expectedResult = service.addFactorStatusHistoryToCollectionIfMissing(factorStatusHistoryCollection, ...factorStatusHistoryArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const factorStatusHistory: IFactorStatusHistory = { id: 123 };
          const factorStatusHistory2: IFactorStatusHistory = { id: 456 };
          expectedResult = service.addFactorStatusHistoryToCollectionIfMissing([], factorStatusHistory, factorStatusHistory2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(factorStatusHistory);
          expect(expectedResult).toContain(factorStatusHistory2);
        });

        it('should accept null and undefined values', () => {
          const factorStatusHistory: IFactorStatusHistory = { id: 123 };
          expectedResult = service.addFactorStatusHistoryToCollectionIfMissing([], null, factorStatusHistory, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(factorStatusHistory);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
