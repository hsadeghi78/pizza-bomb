import { IParty } from 'app/entities/party/party.model';
import { PartyInfoType } from 'app/entities/enumerations/party-info-type.model';

export interface IPartyInformation {
  id?: number;
  infoType?: PartyInfoType;
  infoTitle?: string;
  infoDesc?: string | null;
  party?: IParty;
}

export class PartyInformation implements IPartyInformation {
  constructor(
    public id?: number,
    public infoType?: PartyInfoType,
    public infoTitle?: string,
    public infoDesc?: string | null,
    public party?: IParty
  ) {}
}

export function getPartyInformationIdentifier(partyInformation: IPartyInformation): number | undefined {
  return partyInformation.id;
}
