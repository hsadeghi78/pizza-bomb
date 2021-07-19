jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFactorItem, FactorItem } from '../factor-item.model';
import { FactorItemService } from '../service/factor-item.service';

import { FactorItemRoutingResolveService } from './factor-item-routing-resolve.service';

describe('Service Tests', () => {
  describe('FactorItem routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: FactorItemRoutingResolveService;
    let service: FactorItemService;
    let resultFactorItem: IFactorItem | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(FactorItemRoutingResolveService);
      service = TestBed.inject(FactorItemService);
      resultFactorItem = undefined;
    });

    describe('resolve', () => {
      it('should return IFactorItem returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFactorItem = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFactorItem).toEqual({ id: 123 });
      });

      it('should return new IFactorItem if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFactorItem = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultFactorItem).toEqual(new FactorItem());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFactorItem = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFactorItem).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
