import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFactorItem, FactorItem } from '../factor-item.model';

import { FactorItemService } from './factor-item.service';

describe('Service Tests', () => {
  describe('FactorItem Service', () => {
    let service: FactorItemService;
    let httpMock: HttpTestingController;
    let elemDefault: IFactorItem;
    let expectedResult: IFactorItem | IFactorItem[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FactorItemService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        rowNum: 0,
        title: 'AAAAAAA',
        count: 0,
        discount: 0,
        tax: 0,
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

      it('should create a FactorItem', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new FactorItem()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a FactorItem', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            rowNum: 1,
            title: 'BBBBBB',
            count: 1,
            discount: 1,
            tax: 1,
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

      it('should partial update a FactorItem', () => {
        const patchObject = Object.assign(
          {
            rowNum: 1,
            count: 1,
            tax: 1,
          },
          new FactorItem()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of FactorItem', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            rowNum: 1,
            title: 'BBBBBB',
            count: 1,
            discount: 1,
            tax: 1,
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

      it('should delete a FactorItem', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFactorItemToCollectionIfMissing', () => {
        it('should add a FactorItem to an empty array', () => {
          const factorItem: IFactorItem = { id: 123 };
          expectedResult = service.addFactorItemToCollectionIfMissing([], factorItem);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(factorItem);
        });

        it('should not add a FactorItem to an array that contains it', () => {
          const factorItem: IFactorItem = { id: 123 };
          const factorItemCollection: IFactorItem[] = [
            {
              ...factorItem,
            },
            { id: 456 },
          ];
          expectedResult = service.addFactorItemToCollectionIfMissing(factorItemCollection, factorItem);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a FactorItem to an array that doesn't contain it", () => {
          const factorItem: IFactorItem = { id: 123 };
          const factorItemCollection: IFactorItem[] = [{ id: 456 }];
          expectedResult = service.addFactorItemToCollectionIfMissing(factorItemCollection, factorItem);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(factorItem);
        });

        it('should add only unique FactorItem to an array', () => {
          const factorItemArray: IFactorItem[] = [{ id: 123 }, { id: 456 }, { id: 63729 }];
          const factorItemCollection: IFactorItem[] = [{ id: 123 }];
          expectedResult = service.addFactorItemToCollectionIfMissing(factorItemCollection, ...factorItemArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const factorItem: IFactorItem = { id: 123 };
          const factorItem2: IFactorItem = { id: 456 };
          expectedResult = service.addFactorItemToCollectionIfMissing([], factorItem, factorItem2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(factorItem);
          expect(expectedResult).toContain(factorItem2);
        });

        it('should accept null and undefined values', () => {
          const factorItem: IFactorItem = { id: 123 };
          expectedResult = service.addFactorItemToCollectionIfMissing([], null, factorItem, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(factorItem);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
