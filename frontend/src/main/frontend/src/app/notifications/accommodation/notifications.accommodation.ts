import { Component, Input } from "@angular/core";
import { AccommodationAdd } from "../../accommodation/shared/models/accommodation.model";
import { AccommodationNotificationService } from "./notifications.accommodation.service";
import { UserService } from "../../shared/userServices/user.service";
import { SharedDataService } from "../../shared/data/shared.data.service";
import { Subscription } from "rxjs";

declare var $: any;

@Component({
    selector: 'accommodation-notifications',
    templateUrl: 'notifications.accommodation.html'
})

export class AccommodationNotifications {

    accommodationAdds: AccommodationAdd[];
    paginationCount: number = 0;
    noData: boolean = false;
    selectedAccommodationAdd: AccommodationAdd;
    accommodationAddSubscription: Subscription;

    constructor(private notificationService: AccommodationNotificationService,
        private sharedDataservice: SharedDataService,
        private userService: UserService) { }

    ngOnInit() {
        this.paginationCount = 0;
        this.getAccommodationNotifications();
        this.subscribeToAddClick();
    }

    subscribeToAddClick() {
        this.accommodationAddSubscription = this.sharedDataservice.observeAccommodationAdd()
            .subscribe(accommodationAdd =>
                this.selectedAccommodationAdd = accommodationAdd);
    }

    getAccommodationNotifications() {
        this.userService.getLoginStatus()
            .filter(resp => resp)
            .switchMap(resp => this.notificationService.getAccommodationNotifications(this.paginationCount))
            .subscribe(adds => {
                this.accommodationAdds = adds
                this.noData = this.accommodationAdds != null && this.accommodationAdds.length > 0 ? false : true;
            });
    }

    getNextSetOfUserPosts() {
        this.paginationCount++;
        this.getAccommodationNotifications();
    }

    ngOnDestroy() {
        this.accommodationAddSubscription.unsubscribe();
    }

}