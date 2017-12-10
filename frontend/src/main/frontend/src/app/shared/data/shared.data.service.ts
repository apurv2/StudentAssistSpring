import { Subject } from "rxjs/Subject";
import { Observable } from "rxjs/Observable";
import { AccommodationSearchModel } from "../../accommodation/shared/models/accommodation.filter.model";

export class SharedDataService {

    showHomepageBackground = new Subject<boolean>();
    accommomdationSearchFilters = new Subject<AccommodationSearchModel>();

    getAccommomdationSearchFilters(): Subject<AccommodationSearchModel>  {
        return this.accommomdationSearchFilters;
    }

    setAccommomdationSearchFilters(filterData: AccommodationSearchModel){
        this.accommomdationSearchFilters.next(filterData);
    }

    getHomePageBackground() {
        return this.showHomepageBackground;
    }

    setHomePageBackground(status: boolean) {
        this.showHomepageBackground.next(status);
    }
}