import { Component } from "@angular/core";
import { FacebookService } from "ngx-facebook";
import { SharedDataService } from "../../shared/data/shared.data.service";
import { AccommodationFilterData } from "../shared/models/accommodation.filter.model";
import { Observable } from "rxjs/Observable";
import { SimpleSearchService } from "./accommodation.simple.search.service";
import { AccommodationAdd } from "../shared/models/accommodation.model";

@Component({
    selector: 'simple-search',
    templateUrl: 'accommodation.simple.search.html'
})

export class SimpleSearch {
    selectedAccommodationAdd: AccommodationAdd;
    accommodationSearchResults: AccommodationAdd[];

    constructor(private simpleSearchService: SimpleSearchService,
        private facebookService: FacebookService,
        private sharedDataservice: SharedDataService) {

    }
    ngOnInit() {
        this.sharedDataservice.getAccommomdationSearchFilters()
            .switchMap(filters => this.getSimpleSearchAdds(filters)).
            subscribe(advertisements => this.accommodationSearchResults = advertisements);

    }


    getSimpleSearchAdds(filters: AccommodationFilterData): Observable<any> {

        let rightSpinner = encodeURIComponent(filters.rightSpinner);
        let leftSpinner = encodeURIComponent(filters.leftSpinner);
        return this.simpleSearchService.getSimpleSearchAdds(leftSpinner, rightSpinner);
    }

    makeLink() {
        var copyFrom = document.createElement("textarea");
        //  copyFrom.textContent = $scope.link;
        var body = document.getElementsByTagName('body')[0];
        body.appendChild(copyFrom);
        copyFrom.select();
        document.execCommand('copy');
        body.removeChild(copyFrom);
        //  Materialize.toast('Copied to clipboard', 4000);
    }
}