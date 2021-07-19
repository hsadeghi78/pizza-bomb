import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMenuItem, MenuItem } from '../menu-item.model';

import { MenuItemService } from './menu-item.service';

describe('Service Tests', () => {
  describe('MenuItem Service', () => {
    let service: MenuItemService;
    let httpMock: HttpTestingController;
    let elemDefault: IMenuItem;
    let expectedResult: IMenuItem | IMenuItem[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(MenuItemService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        title: 'AAAAAAA',
        expirationDate: currentDate,
        description: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            expirationDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a MenuItem', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            expirationDate: currentDate.format(DATE_TIME_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            expirationDate: currentDate,
          },
          returnedFromService
        );

        service.create(new MenuItem()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a MenuItem', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            expirationDate: currentDate.format(DATE_TIME_FORMAT),
            description: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            expirationDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a MenuItem', () => {
        const patchObject = Object.assign(
          {
            expirationDate: currentDate.format(DATE_TIME_FORMAT),
            description: 'BBBBBB',
          },
          new MenuItem()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            expirationDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of MenuItem', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            expirationDate: currentDate.format(DATE_TIME_FORMAT),
            description: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
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

      it('should delete a MenuItem', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addMenuItemToCollectionIfMissing', () => {
        it('should add a MenuItem to an empty array', () => {
          const menuItem: IMenuItem = { id: 123 };
          expectedResult = service.addMenuItemToCollectionIfMissing([], menuItem);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(menuItem);
        });

        it('should not add a MenuItem to an array that contains it', () => {
          const menuItem: IMenuItem = { id: 123 };
          const menuItemCollection: IMenuItem[] = [
            {
              ...menuItem,
            },
            { id: 456 },
          ];
          expectedResult = service.addMenuItemToCollectionIfMissing(menuItemCollection, menuItem);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a MenuItem to an array that doesn't contain it", () => {
          const menuItem: IMenuItem = { id: 123 };
          const menuItemCollection: IMenuItem[] = [{ id: 456 }];
          expectedResult = service.addMenuItemToCollectionIfMissing(menuItemCollection, menuItem);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(menuItem);
        });

        it('should add only unique MenuItem to an array', () => {
          const menuItemArray: IMenuItem[] = [{ id: 123 }, { id: 456 }, { id: 26861 }];
          const menuItemCollection: IMenuItem[] = [{ id: 123 }];
          expectedResult = service.addMenuItemToCollectionIfMissing(menuItemCollection, ...menuItemArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const menuItem: IMenuItem = { id: 123 };
          const menuItem2: IMenuItem = { id: 456 };
          expectedResult = service.addMenuItemToCollectionIfMissing([], menuItem, menuItem2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(menuItem);
          expect(expectedResult).toContain(menuItem2);
        });

        it('should accept null and undefined values', () => {
          const menuItem: IMenuItem = { id: 123 };
          expectedResult = service.addMenuItemToCollectionIfMissing([], null, menuItem, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(menuItem);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
