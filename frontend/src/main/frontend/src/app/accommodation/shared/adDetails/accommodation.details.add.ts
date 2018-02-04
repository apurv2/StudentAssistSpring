import { Input, Component, Injectable } from "@angular/core";
import { AccommodationAdd } from "../models/accommodation.model";
import { SharedDataService } from "../../../shared/data/shared.data.service";
import { MatDialog } from "@angular/material";
import { FacebookService } from "ngx-facebook/dist/esm/providers/facebook";
import { LoginModal } from "../../../shared/modals/login.modal";
import { UserService } from "../../../shared/userServices/user.service";
import { SubscribeNotificationsModal } from "app/notifications/notifications.subscribe.modal";


@Component({
    selector: 'accommodation-details',
    templateUrl: 'accommodation.details.add.html'
})

export class AddDetails {

    @Input()
    selectedAccommodationAdd: AccommodationAdd;

    items: Array<any> = []

    constructor(private dialog: MatDialog,
        private userService: UserService,
        private fb: FacebookService) { }
    ngOnInit() {


        this.items = [
            { name: 'http://i.telegraph.co.uk/multimedia/archive/01722/cambridge_universi_1722980b.jpg' },
            { name: 'https://www.thecompleteuniversityguide.co.uk/imagecache/file/width/650/media/3321537/lboro2.jpg' },
        ]

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
                console.log('The dialog was closed');
            });
    }

    login() {

        this.dialog.open(LoginModal).
            afterClosed().subscribe(result => {
                console.log('The dialog was closed');
            });

    }
}