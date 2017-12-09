import { Component, Input } from "@angular/core";
import { AccommodationAdd } from "../../shared/models/accommodation.model";

declare var $: any;

@Component({
    selector: 'simple-search-list',
    templateUrl: 'simple.search.ads.list.html'
})

export class SimpleSearchAddsList {

    @Input()
    accommodationSearchResults: AccommodationAdd[];

    ngOnInit() {
        $('.collapsible').collapsible();
    }

    

}