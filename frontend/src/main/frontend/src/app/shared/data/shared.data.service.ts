import { Subject } from "rxjs/Subject";
import { Observable } from "rxjs/Observable";
import { AccommodationFilterData } from "../../accommodation/shared/models/accommodation.filter.model";

export class SharedDataService {

    showHomepageBackground = new Subject<boolean>();
    accommomdationSearchFilters = new Subject<AccommodationFilterData>();

    getAccommomdationSearchFilters(): Subject<AccommodationFilterData>  {
        return this.accommomdationSearchFilters;
    }

    setAccommomdationSearchFilters(filterData: AccommodationFilterData){
        this.accommomdationSearchFilters.next(filterData);
    }

    getHomePageBackground() {
        return this.showHomepageBackground;
    }

    setHomePageBackground(status: boolean) {
        this.showHomepageBackground.next(status);
    }
}