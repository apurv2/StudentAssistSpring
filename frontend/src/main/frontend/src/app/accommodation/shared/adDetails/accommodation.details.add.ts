import { Input, Component } from "@angular/core";
import { AccommodationAdd } from "app/accommodation/shared/models/accommodation.model";


@Component({
    selector: 'accommodation-details',
    templateUrl: 'accommodation.details.add.html'
})


export class AddDetails {

    @Input() selectedAccommodationAdd: AccommodationAdd;

    openNotificationSettingsModal() {

    }

    makeLink() {

    }

}