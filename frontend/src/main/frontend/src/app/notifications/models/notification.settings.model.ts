import { UniversityApartments } from "app/accommodation/shared/models/university.apartments.model";

export class NotificationSettings {

    apartmentName: string[];
    gender: string;
    universityId: number;
    gcmId: string;
    instanceId: string;
    apartmentType: string[];
    apartmentNames: UniversityApartments[];
}