import { Component } from "@angular/core";
import { FacebookService } from "ngx-facebook";
import { SharedDataService } from "../../shared/data/shared.data.service";
import { AccommodationSearchModel } from "../shared/models/accommodation.filter.model";
import { Observable } from "rxjs/Observable";
import { SimpleSearchService } from "./accommodation.simple.search.service";
import { AccommodationAdd } from "../shared/models/accommodation.model";
import { UniversityAccommodationAdds } from "../shared/models/university.accommodation.adds.model";

@Component({
    selector: 'simple-search',
    templateUrl: 'accommodation.simple.search.html'
})

export class SimpleSearch {
    selectedAccommodationAdd: AccommodationAdd;
    universityAccommodationAdds: UniversityAccommodationAdds[];

    constructor(private simpleSearchService: SimpleSearchService,
        private facebookService: FacebookService,
        private sharedDataservice: SharedDataService) {

    }
    ngOnInit() {
        this.subscribeToAccommodationAddsFilters();
        this.subscribeToAddClick();

    }
    subscribeToAddClick() {

        this.sharedDataservice.observeAccommodationAdd()
            .subscribe(accommodationAdd =>
                this.selectedAccommodationAdd = accommodationAdd);
    }

    subscribeToAccommodationAddsFilters() {
        this.sharedDataservice.observeAccommomdationSearchFilters().
            switchMap(filters => this.getSimpleSearchAdds(filters)).
            subscribe(universityAccommodationAdds =>
                this.universityAccommodationAdds = universityAccommodationAdds);
    }

    getSimpleSearchAdds(filters: AccommodationSearchModel): Observable<any> {
        // this.universityAccommodationAdds = [];
        return this.simpleSearchService.getSimpleSearchAdds(filters);
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

    ngOnDestroy() { }
}