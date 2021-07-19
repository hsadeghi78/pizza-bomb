import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { PartyInfoType } from 'app/entities/enumerations/party-info-type.model';
import { IPartyInformation, PartyInformation } from '../party-information.model';

import { PartyInformationService } from './party-information.service';

describe('Service Tests', () => {
  describe('PartyInformation Service', () => {
    let service: PartyInformationService;
    let httpMock: HttpTestingController;
    let elemDefault: IPartyInformation;
    let expectedResult: IPartyInformation | IPartyInformation[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PartyInformationService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        infoType: PartyInfoType.WORK_TIME,
        infoTitle: 'AAAAAAA',
        infoDesc: 'AAAAAAA',
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

      it('should create a PartyInformation', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new PartyInformation()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a PartyInformation', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            infoType: 'BBBBBB',
            infoTitle: 'BBBBBB',
            infoDesc: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a PartyInformation', () => {
        const patchObject = Object.assign(
          {
            infoTitle: 'BBBBBB',
            infoDesc: 'BBBBBB',
          },
          new PartyInformation()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of PartyInformation', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            infoType: 'BBBBBB',
            infoTitle: 'BBBBBB',
            infoDesc: 'BBBBBB',
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

      it('should delete a PartyInformation', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPartyInformationToCollectionIfMissing', () => {
        it('should add a PartyInformation to an empty array', () => {
          const partyInformation: IPartyInformation = { id: 123 };
          expectedResult = service.addPartyInformationToCollectionIfMissing([], partyInformation);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(partyInformation);
        });

        it('should not add a PartyInformation to an array that contains it', () => {
          const partyInformation: IPartyInformation = { id: 123 };
          const partyInformationCollection: IPartyInformation[] = [
            {
              ...partyInformation,
            },
            { id: 456 },
          ];
          expectedResult = service.addPartyInformationToCollectionIfMissing(partyInformationCollection, partyInformation);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a PartyInformation to an array that doesn't contain it", () => {
          const partyInformation: IPartyInformation = { id: 123 };
          const partyInformationCollection: IPartyInformation[] = [{ id: 456 }];
          expectedResult = service.addPartyInformationToCollectionIfMissing(partyInformationCollection, partyInformation);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(partyInformation);
        });

        it('should add only unique PartyInformation to an array', () => {
          const partyInformationArray: IPartyInformation[] = [{ id: 123 }, { id: 456 }, { id: 50841 }];
          const partyInformationCollection: IPartyInformation[] = [{ id: 123 }];
          expectedResult = service.addPartyInformationToCollectionIfMissing(partyInformationCollection, ...partyInformationArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const partyInformation: IPartyInformation = { id: 123 };
          const partyInformation2: IPartyInformation = { id: 456 };
          expectedResult = service.addPartyInformationToCollectionIfMissing([], partyInformation, partyInformation2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(partyInformation);
          expect(expectedResult).toContain(partyInformation2);
        });

        it('should accept null and undefined values', () => {
          const partyInformation: IPartyInformation = { id: 123 };
          expectedResult = service.addPartyInformationToCollectionIfMissing([], null, partyInformation, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(partyInformation);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
