jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IClassType, ClassType } from '../class-type.model';
import { ClassTypeService } from '../service/class-type.service';

import { ClassTypeRoutingResolveService } from './class-type-routing-resolve.service';

describe('Service Tests', () => {
  describe('ClassType routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ClassTypeRoutingResolveService;
    let service: ClassTypeService;
    let resultClassType: IClassType | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ClassTypeRoutingResolveService);
      service = TestBed.inject(ClassTypeService);
      resultClassType = undefined;
    });

    describe('resolve', () => {
      it('should return IClassType returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultClassType = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultClassType).toEqual({ id: 123 });
      });

      it('should return new IClassType if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultClassType = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultClassType).toEqual(new ClassType());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultClassType = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultClassType).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
