import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICriticism, Criticism } from '../criticism.model';

import { CriticismService } from './criticism.service';

describe('Service Tests', () => {
  describe('Criticism Service', () => {
    let service: CriticismService;
    let httpMock: HttpTestingController;
    let elemDefault: ICriticism;
    let expectedResult: ICriticism | ICriticism[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CriticismService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        fullName: 'AAAAAAA',
        email: 'AAAAAAA',
        contactNumber: 'AAAAAAA',
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

      it('should create a Criticism', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Criticism()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Criticism', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            fullName: 'BBBBBB',
            email: 'BBBBBB',
            contactNumber: 'BBBBBB',
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

      it('should partial update a Criticism', () => {
        const patchObject = Object.assign({}, new Criticism());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Criticism', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            fullName: 'BBBBBB',
            email: 'BBBBBB',
            contactNumber: 'BBBBBB',
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

      it('should delete a Criticism', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCriticismToCollectionIfMissing', () => {
        it('should add a Criticism to an empty array', () => {
          const criticism: ICriticism = { id: 123 };
          expectedResult = service.addCriticismToCollectionIfMissing([], criticism);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(criticism);
        });

        it('should not add a Criticism to an array that contains it', () => {
          const criticism: ICriticism = { id: 123 };
          const criticismCollection: ICriticism[] = [
            {
              ...criticism,
            },
            { id: 456 },
          ];
          expectedResult = service.addCriticismToCollectionIfMissing(criticismCollection, criticism);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Criticism to an array that doesn't contain it", () => {
          const criticism: ICriticism = { id: 123 };
          const criticismCollection: ICriticism[] = [{ id: 456 }];
          expectedResult = service.addCriticismToCollectionIfMissing(criticismCollection, criticism);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(criticism);
        });

        it('should add only unique Criticism to an array', () => {
          const criticismArray: ICriticism[] = [{ id: 123 }, { id: 456 }, { id: 7550 }];
          const criticismCollection: ICriticism[] = [{ id: 123 }];
          expectedResult = service.addCriticismToCollectionIfMissing(criticismCollection, ...criticismArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const criticism: ICriticism = { id: 123 };
          const criticism2: ICriticism = { id: 456 };
          expectedResult = service.addCriticismToCollectionIfMissing([], criticism, criticism2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(criticism);
          expect(expectedResult).toContain(criticism2);
        });

        it('should accept null and undefined values', () => {
          const criticism: ICriticism = { id: 123 };
          expectedResult = service.addCriticismToCollectionIfMissing([], null, criticism, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(criticism);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
