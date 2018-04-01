import { Component, Input } from "@angular/core";
import { AccommodationAdd } from "../../accommodation/shared/models/accommodation.model";
import { AccommodationNotificationService } from "./notifications.accommodation.service";
import { UserService } from "../../shared/userServices/user.service";

declare var $: any;

@Component({
    selector: 'accommodation-notifications',
    templateUrl: 'notifications.accommodation.html'
})

export class AccommodationNotifications {

    accommodationAdds: AccommodationAdd[];
    paginationCount: number = 0;
    noData: boolean = false;

    constructor(private notificationService: AccommodationNotificationService,
        private userService: UserService) { }
    ngOnInit() {
        this.paginationCount = 0;
        this.getAccommodationNotifications();

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

}