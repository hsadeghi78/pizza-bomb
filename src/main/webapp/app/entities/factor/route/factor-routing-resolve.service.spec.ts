jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IFactor, Factor } from '../factor.model';
import { FactorService } from '../service/factor.service';

import { FactorRoutingResolveService } from './factor-routing-resolve.service';

describe('Service Tests', () => {
  describe('Factor routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: FactorRoutingResolveService;
    let service: FactorService;
    let resultFactor: IFactor | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(FactorRoutingResolveService);
      service = TestBed.inject(FactorService);
      resultFactor = undefined;
    });

    describe('resolve', () => {
      it('should return IFactor returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFactor = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFactor).toEqual({ id: 123 });
      });

      it('should return new IFactor if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFactor = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultFactor).toEqual(new Factor());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultFactor = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultFactor).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
