import { Component, ViewChild } from "@angular/core";
import { FacebookService } from "ngx-facebook";
import { SharedDataService } from "../../shared/data/shared.data.service";
import { AccommodationSearchModel } from "../shared/models/accommodation.filter.model";
import { Observable } from "rxjs/Observable";
import { SimpleSearchService } from "./accommodation.simple.search.service";
import { AccommodationAdd } from "../shared/models/accommodation.model";
import { UniversityAccommodationAdds } from "../shared/models/university.accommodation.adds.model";
import { Subscription } from "rxjs/Subscription";
import { LoginResponse } from "ngx-facebook/dist/esm/models/login-response";
import { UserService } from "app/shared/userServices/user.service";
import { SimpleSearchAddsFilters } from "./filters/simple.search.filters";
import { SimpleSearchAddsList } from "./adsList/simple.search.adds.list";
import { PlatformLocation } from "@angular/common";


@Component({
    selector: 'simple-search',
    templateUrl: 'accommodation.simple.search.html'
})

export class SimpleSearch {
    selectedAccommodationAdd: AccommodationAdd;
    universityAccommodationAdds: UniversityAccommodationAdds[];
    accommodationFiltersObservable: Subscription;
    accommodationAddSubscription: Subscription;
    loadedFirstTime: boolean = false;
    noData: boolean = false;
    backPressed: boolean = false;

    @ViewChild('filters') filters: SimpleSearchAddsFilters;
    @ViewChild('simpleSearchList') simpleSearchList: SimpleSearchAddsList;

    constructor(private simpleSearchService: SimpleSearchService,
        private facebookService: FacebookService,
        private sharedDataservice: SharedDataService,
        private userService: UserService,
        private location: PlatformLocation) {
        location.onPopState(() => this.backPressed = true);
    }
    ngOnInit() {
        this.subscribeToAccommodationAddsFilters();
        this.subscribeToAddClick();
    }
    subscribeToAddClick() {
        this.accommodationAddSubscription = this.sharedDataservice.observeAccommodationAdd()
            .subscribe(accommodationAdd =>
                this.selectedAccommodationAdd = accommodationAdd);
    }

    subscribeToAccommodationAddsFilters() {
        this.accommodationFiltersObservable = this.sharedDataservice.
            observeAccommomdationSearchFilters().
            switchMap(filters => this.getSimpleSearchAdds(filters)).
            subscribe(universityAccommodationAdds => {
                this.universityAccommodationAdds = universityAccommodationAdds;
                this.noData = this.universityAccommodationAdds.length == 0;
                this.filters.stopLoding();
                this.loadedFirstTime = true;
            },
                err => {
                    this.noData = true;
                    this.filters.stopLoding();
                    this.loadedFirstTime = true;
                });
    }

    getSimpleSearchAdds(filters: AccommodationSearchModel): Observable<any> {
        return this.simpleSearchService.getSimpleSearchAdds(filters);
    }

    ngOnDestroy() {

        this.accommodationAddSubscription.unsubscribe();
        this.accommodationFiltersObservable.unsubscribe();

    }
}