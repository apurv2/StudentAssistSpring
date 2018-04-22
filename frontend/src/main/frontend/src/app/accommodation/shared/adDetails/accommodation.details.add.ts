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
import { Http } from "@angular/http";
import { CopyLinkModal } from "../modals/copy.link.modal";
import { ActivatedRoute, Router } from "@angular/router";


@Component({
    selector: 'accommodation-details',
    templateUrl: 'accommodation.details.add.html'
})

export class AddDetails {

    @Input()
    selectedAccommodationAdd: AccommodationAdd;
    @Input()
    enableSubscription: boolean;

    items: Array<any> = []
    loggedInUserId: number;
    deletePrompt: boolean = false;
    sharedLink: boolean = false;

    constructor(private dialog: MatDialog,
        private sharedDataService: SharedDataService,
        private userService: UserService,
        private fb: FacebookService,
        private addDetailService: AddDetailsService,
        private route: ActivatedRoute,
        private router: Router,
        private http: Http) { }
    ngOnInit() {

        this.getUserId();
        this.route
            .queryParams
            .filter(params => params['addId'] > 0)
            .flatMap(data => this.addDetailService.getAddDetailsFromAddId(data.addId))
            .do(e => this.sharedLink = true)
            .subscribe((params: AccommodationAdd) => {
                this.selectedAccommodationAdd = params;
            });
    }

    getUserId() {

        this.userService.getLoginStatus()
            .filter(resp => resp)
            .switchMap(resp => this.userService.getUserInfoFromFb()).
            subscribe((userInfo: UserInfo) => this.loggedInUserId = userInfo.userId);
    }

    share() {
        this.selectedAccommodationAdd.branch_key = environment.branchKey;
        this.selectedAccommodationAdd.channel = 'facebook';
        this.selectedAccommodationAdd.feature = 'dashboard';
        this.selectedAccommodationAdd.stage = 'old user';

        this.selectedAccommodationAdd.addPhotoIds

        var data = {};
        data['data'] = this.selectedAccommodationAdd;
        data['branch_key'] = environment.branchKey;

        this.addDetailService.makeLink(environment.branchUrl, data)
            .subscribe(response => {
                this.dialog.open(CopyLinkModal, {
                    data: response.url
                }).
                    afterClosed().subscribe(result => {
                    });

            });
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

    showDeletePrompt() {
        this.deletePrompt = true;
    }

    deleteAdd() {
        let url: string = environment.deleteAccommodationAdd
            + '?' + environment.addId + '='
            + this.selectedAccommodationAdd.addId;

        this.addDetailService.deleteAdd(url).flatMap(res => this.sharedDataService.openSuccessFailureDialog(res, this.dialog, "Your listing has been removed!"))
            .subscribe(e => this.router.navigate(['/dashboard/']))
    }

    deletePromptNo() {
        this.deletePrompt = false;
    }

    isNumber(val) {
        if (val != null) {
            return !isNaN(Number(val));
        }
        return false;
    }

}