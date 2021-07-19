jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFoodType, FoodType } from '../food-type.model';
import { FoodTypeService } from '../service/food-type.service';

import { FoodTypeRoutingResolveService } from './food-type-routing-resolve.service';

describe('Service Tests', () => {
  describe('FoodType routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: FoodTypeRoutingResolveService;
    let service: FoodTypeService;
    let resultFoodType: IFoodType | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(FoodTypeRoutingResolveService);
      service = TestBed.inject(FoodTypeService);
      resultFoodType = undefined;
    });

    describe('resolve', () => {
      it('should return IFoodType returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFoodType = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFoodType).toEqual({ id: 123 });
      });

      it('should return new IFoodType if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFoodType = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultFoodType).toEqual(new FoodType());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFoodType = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFoodType).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
