import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IBranch, Branch } from '../branch.model';

import { BranchService } from './branch.service';

describe('Service Tests', () => {
  describe('Branch Service', () => {
    let service: BranchService;
    let httpMock: HttpTestingController;
    let elemDefault: IBranch;
    let expectedResult: IBranch | IBranch[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(BranchService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        title: 'AAAAAAA',
        branchCode: 'AAAAAAA',
        tradeTitle: 'AAAAAAA',
        activationDate: currentDate,
        expirationDate: currentDate,
        activationStatus: false,
        lat: 0,
        address: 'AAAAAAA',
        postalCode: 'AAAAAAA',
        description: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            activationDate: currentDate.format(DATE_FORMAT),
            expirationDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Branch', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            activationDate: currentDate.format(DATE_FORMAT),
            expirationDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            activationDate: currentDate,
            expirationDate: currentDate,
          },
          returnedFromService
        );

        service.create(new Branch()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Branch', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            branchCode: 'BBBBBB',
            tradeTitle: 'BBBBBB',
            activationDate: currentDate.format(DATE_FORMAT),
            expirationDate: currentDate.format(DATE_FORMAT),
            activationStatus: true,
            lat: 1,
            address: 'BBBBBB',
            postalCode: 'BBBBBB',
            description: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            activationDate: currentDate,
            expirationDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Branch', () => {
        const patchObject = Object.assign(
          {
            title: 'BBBBBB',
            tradeTitle: 'BBBBBB',
            expirationDate: currentDate.format(DATE_FORMAT),
            lat: 1,
            postalCode: 'BBBBBB',
          },
          new Branch()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            activationDate: currentDate,
            expirationDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Branch', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            branchCode: 'BBBBBB',
            tradeTitle: 'BBBBBB',
            activationDate: currentDate.format(DATE_FORMAT),
            expirationDate: currentDate.format(DATE_FORMAT),
            activationStatus: true,
            lat: 1,
            address: 'BBBBBB',
            postalCode: 'BBBBBB',
            description: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            activationDate: currentDate,
            expirationDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Branch', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addBranchToCollectionIfMissing', () => {
        it('should add a Branch to an empty array', () => {
          const branch: IBranch = { id: 123 };
          expectedResult = service.addBranchToCollectionIfMissing([], branch);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(branch);
        });

        it('should not add a Branch to an array that contains it', () => {
          const branch: IBranch = { id: 123 };
          const branchCollection: IBranch[] = [
            {
              ...branch,
            },
            { id: 456 },
          ];
          expectedResult = service.addBranchToCollectionIfMissing(branchCollection, branch);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Branch to an array that doesn't contain it", () => {
          const branch: IBranch = { id: 123 };
          const branchCollection: IBranch[] = [{ id: 456 }];
          expectedResult = service.addBranchToCollectionIfMissing(branchCollection, branch);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(branch);
        });

        it('should add only unique Branch to an array', () => {
          const branchArray: IBranch[] = [{ id: 123 }, { id: 456 }, { id: 61787 }];
          const branchCollection: IBranch[] = [{ id: 123 }];
          expectedResult = service.addBranchToCollectionIfMissing(branchCollection, ...branchArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const branch: IBranch = { id: 123 };
          const branch2: IBranch = { id: 456 };
          expectedResult = service.addBranchToCollectionIfMissing([], branch, branch2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(branch);
          expect(expectedResult).toContain(branch2);
        });

        it('should accept null and undefined values', () => {
          const branch: IBranch = { id: 123 };
          expectedResult = service.addBranchToCollectionIfMissing([], null, branch, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(branch);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
