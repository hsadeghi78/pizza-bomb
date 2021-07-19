jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPartyInformation, PartyInformation } from '../party-information.model';
import { PartyInformationService } from '../service/party-information.service';

import { PartyInformationRoutingResolveService } from './party-information-routing-resolve.service';

describe('Service Tests', () => {
  describe('PartyInformation routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PartyInformationRoutingResolveService;
    let service: PartyInformationService;
    let resultPartyInformation: IPartyInformation | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PartyInformationRoutingResolveService);
      service = TestBed.inject(PartyInformationService);
      resultPartyInformation = undefined;
    });

    describe('resolve', () => {
      it('should return IPartyInformation returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPartyInformation = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPartyInformation).toEqual({ id: 123 });
      });

      it('should return new IPartyInformation if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPartyInformation = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPartyInformation).toEqual(new PartyInformation());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPartyInformation = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPartyInformation).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
