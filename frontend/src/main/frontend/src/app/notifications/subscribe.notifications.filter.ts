import { Pipe, PipeTransform } from '@angular/core';
import { Apartment } from 'app/accommodation/shared/models/apartment.names.model';

@Pipe({
    name: 'notificationSettingsFilter',
    pure: false
})
export class NotificationSettingsFilter implements PipeTransform {
    transform(allApartments: Apartment[], currentApartmentType: string): any {
        if (!allApartments || !currentApartmentType) {
            return allApartments;
        }
        return allApartments.filter(apartment =>
            apartment.apartmentType == currentApartmentType);
    }
}