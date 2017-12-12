import { Component, Input } from "@angular/core";
import { AccommodationAdd } from "../../shared/models/accommodation.model";
import { UniversityAccommodationAdds } from "../../shared/models/university.accommodation.adds.model";
import { SharedDataService } from "../../../shared/data/shared.data.service";

declare var $: any;

@Component({
    selector: 'simple-search-list',
    templateUrl: 'simple.search.ads.list.html'
})

export class SimpleSearchAddsList {

    @Input()
    accommodationSearchResults: UniversityAccommodationAdds[];

    constructor(private sharedDataService: SharedDataService) { }
    ngOnInit() {
        $('.collapsible').collapsible();
    }

    addClick(accommodationAdd: AccommodationAdd) {
        this.sharedDataService.emitAccommodationAdd(accommodationAdd);
    }

}