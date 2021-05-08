jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICriticism, Criticism } from '../criticism.model';
import { CriticismService } from '../service/criticism.service';

import { CriticismRoutingResolveService } from './criticism-routing-resolve.service';

describe('Service Tests', () => {
  describe('Criticism routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CriticismRoutingResolveService;
    let service: CriticismService;
    let resultCriticism: ICriticism | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CriticismRoutingResolveService);
      service = TestBed.inject(CriticismService);
      resultCriticism = undefined;
    });

    describe('resolve', () => {
      it('should return ICriticism returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCriticism = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCriticism).toEqual({ id: 123 });
      });

      it('should return new ICriticism if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCriticism = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCriticism).toEqual(new Criticism());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCriticism = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCriticism).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
