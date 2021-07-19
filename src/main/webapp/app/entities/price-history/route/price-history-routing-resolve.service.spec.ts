jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPriceHistory, PriceHistory } from '../price-history.model';
import { PriceHistoryService } from '../service/price-history.service';

import { PriceHistoryRoutingResolveService } from './price-history-routing-resolve.service';

describe('Service Tests', () => {
  describe('PriceHistory routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PriceHistoryRoutingResolveService;
    let service: PriceHistoryService;
    let resultPriceHistory: IPriceHistory | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PriceHistoryRoutingResolveService);
      service = TestBed.inject(PriceHistoryService);
      resultPriceHistory = undefined;
    });

    describe('resolve', () => {
      it('should return IPriceHistory returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPriceHistory = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPriceHistory).toEqual({ id: 123 });
      });

      it('should return new IPriceHistory if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPriceHistory = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPriceHistory).toEqual(new PriceHistory());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPriceHistory = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPriceHistory).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
