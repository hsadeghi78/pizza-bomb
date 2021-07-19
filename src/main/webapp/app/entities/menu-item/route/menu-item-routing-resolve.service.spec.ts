jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMenuItem, MenuItem } from '../menu-item.model';
import { MenuItemService } from '../service/menu-item.service';

import { MenuItemRoutingResolveService } from './menu-item-routing-resolve.service';

describe('Service Tests', () => {
  describe('MenuItem routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: MenuItemRoutingResolveService;
    let service: MenuItemService;
    let resultMenuItem: IMenuItem | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(MenuItemRoutingResolveService);
      service = TestBed.inject(MenuItemService);
      resultMenuItem = undefined;
    });

    describe('resolve', () => {
      it('should return IMenuItem returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMenuItem = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMenuItem).toEqual({ id: 123 });
      });

      it('should return new IMenuItem if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMenuItem = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultMenuItem).toEqual(new MenuItem());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMenuItem = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMenuItem).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
