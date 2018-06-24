import { Apartment } from "../accommodation/shared/models/apartment.names.model";

export class University {

      universityId: number;
      universityName: string;
      description: string;
      url: string;
      noOfUsers: number;
      location: string;
      estdYear: number;
      noOfListings: number;
      univAcronym: string;
      isSelected: boolean;
      apartments: Apartment[];

      public constructor(init?: Partial<University>) {
            Object.assign(this, init);
      }

}