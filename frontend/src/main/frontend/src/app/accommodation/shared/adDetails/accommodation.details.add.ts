import { Input, Component, Injectable } from "@angular/core";
import { AccommodationAdd } from "../models/accommodation.model";
import { SharedDataService } from "../../../shared/data/shared.data.service";
import { MatDialog } from "@angular/material";
import { FacebookService } from "ngx-facebook/dist/esm/providers/facebook";
import { LoginModal } from "../../../shared/modals/login.modal";
import { UserService } from "../../../shared/userServices/user.service";
import { SubscribeNotificationsModal } from "app/notifications/notifications.subscribe.modal";
import { UserInfo } from "app/shared/models/user.info.model";
import { AddDetailsService } from "app/accommodation/shared/adDetails/accommodation.details.add.service";
import { environment } from "environments/environment";


@Component({
    selector: 'accommodation-details',
    templateUrl: 'accommodation.details.add.html'
})

export class AddDetails {

    @Input()
    selectedAccommodationAdd: AccommodationAdd;

    items: Array<any> = []
    loggedInUserId: number;

    constructor(private dialog: MatDialog,
        private userService: UserService,
        private fb: FacebookService,
        private addDetailService: AddDetailsService) { }
    ngOnInit() {


        this.items = [
            { name: 'http://i.telegraph.co.uk/multimedia/archive/01722/cambridge_universi_1722980b.jpg' },
            { name: 'https://www.thecompleteuniversityguide.co.uk/imagecache/file/width/650/media/3321537/lboro2.jpg' },
        ]
        this.getUserId();
    }

    getUserId() {

        this.userService.getLoginStatus()
            .filter(resp => resp)
            .switchMap(resp => this.userService.getUserInfoFromFb()).
            subscribe((userInfo: UserInfo) => this.loggedInUserId = userInfo.userId);
    }

    openNotificationSettingsModal() {

    }

    makeLink() {

    }

    subscribe() {

        this.userService.getLoginStatus().
            subscribe((status: boolean) => status ?
                this.subscribeNotifications() : this.login())
    }

    subscribeNotifications(): void {
        this.dialog.open(SubscribeNotificationsModal).
            afterClosed().subscribe(result => {
            });
    }

    login() {

        this.dialog.open(LoginModal).
            afterClosed().subscribe(result => {
            });

    }

    deleteAdd() {
        let url: string = environment.deleteAccommodationAdd
            + '?' + environment.addId + '='
            + this.selectedAccommodationAdd.addId;

        this.addDetailService.deleteAdd(url).subscribe(res => console.log(res));

    }
}