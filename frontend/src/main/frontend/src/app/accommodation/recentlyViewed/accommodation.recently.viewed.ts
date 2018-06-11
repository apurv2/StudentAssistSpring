import { Component, Input, ViewChild } from "@angular/core";
import { AccommodationAdd } from "../../accommodation/shared/models/accommodation.model";
import { UserService } from "../../shared/userServices/user.service";
import { RecentlyViewedService } from "./accommodation.recently.viewed.service";
import { AccommodationSearchModel } from "../shared/models/accommodation.filter.model";
import { AddsList } from "../shared/adsList/ads.list";
import { Subscription } from "rxjs";
import { SharedDataService } from "../../shared/data/shared.data.service";
import { Accommodation } from "../accommodation";
import { PlatformLocation } from "@angular/common";


@Component({
    selector: 'accommodation-recently-viewed',
    templateUrl: 'accommodation.recently.viewed.html'
})

export class RecentlyViewedAccommodations extends Accommodation {

    accommodationAdds: AccommodationAdd[];
    noData: boolean = false;
    accommodationAddSubscription: Subscription;
    selectedAccommodationAdd: AccommodationAdd;

    constructor(private recentlyViewedService: RecentlyViewedService,
        private sharedDataservice: SharedDataService,
        private userService: UserService,
        private location: PlatformLocation) {
        super();
        location.onPopState(() => this.backPressed = true);
    }

    ngOnInit() {
        this.getRecentlyViewedAdds(0);
        this.subscribeToAddClick();
    }

    subscribeToAddClick() {
        this.accommodationAddSubscription = this.sharedDataservice.observeAccommodationAdd()
            .subscribe(accommodationAdd =>
                this.selectedAccommodationAdd = accommodationAdd);
    }

    getRecentlyViewedAdds(paginationCount: number) {
        this.userService.getLoginStatus()
            .filter(resp => resp)
            .switchMap(resp => this.recentlyViewedService.getRecentlyViewedAdds(paginationCount))
            .subscribe(adds => {
                this.accommodationAdds = adds
                this.noData = this.accommodationAdds != null && this.accommodationAdds.length > 0 ? false : true;
            });
    }

    paginationEvent(paginationCount: number) {

        this.recentlyViewedService.getRecentlyViewedAdds(paginationCount)
            .subscribe(accommodationAdds => {

                accommodationAdds.forEach(add => this.accommodationAdds.push(add));
                this.addsList.paginating = false;
                this.addsList.stopPagination = accommodationAdds.length < 10;
            });
    }

    ngOnDestroy() {
        this.accommodationAddSubscription.unsubscribe();
    }
}