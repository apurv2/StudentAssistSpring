import { FacebookService, LoginResponse } from 'ngx-facebook';
import { Component } from '@angular/core';
import { Subscription } from 'rxjs/Subscription';
import { SharedDataService } from '../../shared/data/shared.data.service';
import { AccommodationAdd } from '../shared/models/accommodation.model';
import { AccommodationSearchModel } from '../shared/models/accommodation.filter.model';
import { AdvanceSearchService } from './accommodation.advanced.search.service';

@Component({
    selector: 'advanced-search',
    templateUrl: 'accommodation.advanced.search.html'
})
export class AdvancedSearch {
    accommodationFiltersObservable: Subscription;
    accommodationAdds: AccommodationAdd[];
    selectedAccommodationAdd: AccommodationAdd;


    constructor(private sharedDataservice: SharedDataService,
        private advancedSearchService: AdvanceSearchService) {

    }

    ngOnInit() {
        this.subscribeToAccommodationAddsFilters();
    }

    subscribeToAccommodationAddsFilters() {
        this.accommodationFiltersObservable = this.sharedDataservice.observeAccommomdationSearchFilters().
            switchMap(filters => this.getAdvancedSearchAdds(filters)).
            subscribe(accommodationAdds =>
                this.accommodationAdds = accommodationAdds);
    }

    getAdvancedSearchAdds(filters: AccommodationSearchModel) {
        return this.advancedSearchService.getAdvancedSearchAdds(filters);
    }

    ngOnDestroy() {
        this.accommodationFiltersObservable.unsubscribe();
    }

}