import { Component, Input } from "@angular/core";
import { AccommodationAdd } from "../../shared/models/accommodation.model";
import { UniversityAccommodationAdds } from "../../shared/models/university.accommodation.adds.model";
import { SharedDataService } from "../../../shared/data/shared.data.service";

declare var $: any;

@Component({
    selector: 'adds-list',
    templateUrl: 'ads.list.html'
})

export class AddsList {

    @Input()
    accommodationAdds: AccommodationAdd[];

    constructor(private sharedDataService: SharedDataService) { }
    ngOnInit() {
    }

    ngOnChanges() {
        if (this.accommodationAdds != null && this.accommodationAdds.length > 0) {
            this.sharedDataService.emitAccommodationAdd(
                this.accommodationAdds[0]);
        }
    }

    addClick(accommodationAdd: AccommodationAdd) {
        this.sharedDataService.emitAccommodationAdd(accommodationAdd);
    }
}