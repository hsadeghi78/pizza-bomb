import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFoodType, FoodType } from '../food-type.model';

import { FoodTypeService } from './food-type.service';

describe('Service Tests', () => {
  describe('FoodType Service', () => {
    let service: FoodTypeService;
    let httpMock: HttpTestingController;
    let elemDefault: IFoodType;
    let expectedResult: IFoodType | IFoodType[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(FoodTypeService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        title: 'AAAAAAA',
        typeCode: 'AAAAAAA',
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

      it('should create a FoodType', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new FoodType()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a FoodType', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            typeCode: 'BBBBBB',
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

      it('should partial update a FoodType', () => {
        const patchObject = Object.assign(
          {
            title: 'BBBBBB',
            typeCode: 'BBBBBB',
          },
          new FoodType()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of FoodType', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            typeCode: 'BBBBBB',
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

      it('should delete a FoodType', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addFoodTypeToCollectionIfMissing', () => {
        it('should add a FoodType to an empty array', () => {
          const foodType: IFoodType = { id: 123 };
          expectedResult = service.addFoodTypeToCollectionIfMissing([], foodType);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(foodType);
        });

        it('should not add a FoodType to an array that contains it', () => {
          const foodType: IFoodType = { id: 123 };
          const foodTypeCollection: IFoodType[] = [
            {
              ...foodType,
            },
            { id: 456 },
          ];
          expectedResult = service.addFoodTypeToCollectionIfMissing(foodTypeCollection, foodType);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a FoodType to an array that doesn't contain it", () => {
          const foodType: IFoodType = { id: 123 };
          const foodTypeCollection: IFoodType[] = [{ id: 456 }];
          expectedResult = service.addFoodTypeToCollectionIfMissing(foodTypeCollection, foodType);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(foodType);
        });

        it('should add only unique FoodType to an array', () => {
          const foodTypeArray: IFoodType[] = [{ id: 123 }, { id: 456 }, { id: 13657 }];
          const foodTypeCollection: IFoodType[] = [{ id: 123 }];
          expectedResult = service.addFoodTypeToCollectionIfMissing(foodTypeCollection, ...foodTypeArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const foodType: IFoodType = { id: 123 };
          const foodType2: IFoodType = { id: 456 };
          expectedResult = service.addFoodTypeToCollectionIfMissing([], foodType, foodType2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(foodType);
          expect(expectedResult).toContain(foodType2);
        });

        it('should accept null and undefined values', () => {
          const foodType: IFoodType = { id: 123 };
          expectedResult = service.addFoodTypeToCollectionIfMissing([], null, foodType, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(foodType);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
