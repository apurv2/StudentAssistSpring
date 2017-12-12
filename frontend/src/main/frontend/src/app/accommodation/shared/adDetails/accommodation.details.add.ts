import { Input, Component } from "@angular/core";
import { AccommodationAdd } from "../models/accommodation.model";
import { SharedDataService } from "../../../shared/data/shared.data.service";


@Component({
    selector: 'accommodation-details',
    templateUrl: 'accommodation.details.add.html'
})


export class AddDetails {

    @Input()
    selectedAccommodationAdd: AccommodationAdd;


    ngOnInit() {

    }

    openNotificationSettingsModal() {

    }

    makeLink() {

    }

}