import { FacebookService, LoginResponse } from 'ngx-facebook';
import { Component, ViewChild } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';
import { SharedDataService } from '../../shared/data/shared.data.service';
import { AccommodationAdd } from '../shared/models/accommodation.model';
import { AccommodationSearchModel } from '../shared/models/accommodation.filter.model';
import { AdvanceSearchService } from './accommodation.advanced.search.service';
import { AdvancedSearchFilters } from './filters/advanced.search.filters';
import { AddsList } from '../shared/adsList/ads.list';
import { Accommodation } from '../accommodation';
import { PlatformLocation } from '@angular/common';

@Component({
    selector: 'advanced-search',
    templateUrl: 'accommodation.advanced.search.html'
})
export class AdvancedSearch extends Accommodation {
    accommodationFiltersObservable: Subscription;
    accommodationAdds: AccommodationAdd[];
    selectedAccommodationAdd: AccommodationAdd;
    noData: boolean = true;
    accommodationAddSubscription: Subscription;

    @ViewChild("filters")
    filters: AdvancedSearchFilters;

    constructor(private sharedDataservice: SharedDataService,
        private advancedSearchService: AdvanceSearchService,
        private location: PlatformLocation) {
        super();
        location.onPopState(() => this.backPressed = true);

    }

    ngOnInit() {
        this.subscribeToAccommodationAddsFilters();
        this.subscribeToAddClick();
    }

    subscribeToAccommodationAddsFilters() {
        this.accommodationFiltersObservable = this.sharedDataservice.observeAccommomdationSearchFilters()
            .do(e => this.filters.loading = true)
            .switchMap(filters => this.getAdvancedSearchAdds(filters)).
            subscribe(accommodationAdds => {
                this.accommodationAdds = accommodationAdds;
                this.filters.loading = false;
                this.noData = this.accommodationAdds.length < 1;
            });
    }

    getAdvancedSearchAdds(filters: AccommodationSearchModel) {
        return this.advancedSearchService.getAdvancedSearchAdds(filters);
    }

    subscribeToAddClick() {
        this.accommodationAddSubscription = this.sharedDataservice.observeAccommodationAdd()
            .subscribe(accommodationAdd =>
                this.selectedAccommodationAdd = accommodationAdd);
    }

    ngOnDestroy() {

        this.accommodationAddSubscription.unsubscribe();
        this.accommodationFiltersObservable.unsubscribe();
    }

    paginationEvent(paginationCount: number) {

        let filters: AccommodationSearchModel = this.filters.getCurrentFilters();
        filters.paginationPosition = paginationCount;

        this.getAdvancedSearchAdds(filters)
            .subscribe(accommodationAdds => {
                accommodationAdds.forEach(add => this.accommodationAdds.push(add));
                this.addsList.paginating = false;
                this.addsList.stopPagination = accommodationAdds.length < 10;

            });
    }

}