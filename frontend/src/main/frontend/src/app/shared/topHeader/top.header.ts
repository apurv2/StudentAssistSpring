import { Component, ElementRef, ViewChild, HostListener } from "@angular/core";
import { Router } from '@angular/router';
import { SharedDataService } from "app/shared/data/shared.data.service";
import { UserInfo } from "app/shared/models/user.info.model";
import { Subscription } from "rxjs/Subscription";
import { UserService } from "app/shared/userServices/user.service";
import { environment } from "environments/environment";
import { LoginModal } from "app/shared/modals/login.modal";
import { MatDialog } from "@angular/material";
import { University } from "../../universities/universities.model";

@Component({
    selector: 'top-header',
    templateUrl: 'top.header.html'
})

export class TopHeader {


    showSideNav: boolean;
    userInfo: UserInfo = null;
    userInfoSubscription: Subscription;
    loginLogout: string;
    loginStatusSubscription: Subscription;
    @ViewChild('overlay') overlay: ElementRef;
    @ViewChild('hamburger') hamburger: ElementRef;



    constructor(private router: Router,
        private sharedDataService: SharedDataService,
        private userService: UserService,
        private dialog: MatDialog) { }

    ngOnInit() {
        this.subscribeToUserDetails();
        this.determineLoginSituation();
        this.observeLoginStatus();
    }

    observeLoginStatus() {

        this.loginStatusSubscription = this.sharedDataService.observeLoginStatus()
            .subscribe(status => this.loginLogout = status ? environment.logout : environment.login);

    }

    subscribeToUserDetails() {
        this.sharedDataService.observeUserInfo().
            subscribe(userInfo => this.userInfo = userInfo);
    }

    openNav() {
        document.getElementById("mySidenav").style.width = "250px";
        this.showSideNav = true;
    }

    closeNav() {
        document.getElementById("mySidenav").style.width = "0px";
        this.showSideNav = false;
    }

    openPostAccommodation() {
        this.closeNav();
        this.router.navigate(['/post/']);
    }

    loginOrLogout() {
        this.userService.getLoginStatus().
            flatMap(status => status ? this.userService.logout() : this.openLoginDialog())
            .subscribe(loginResponse => {
                this.loginLogout = loginResponse ? environment.logout : environment.login;
                if (loginResponse) {
                    this.processLogin();
                }
                else {
                    this.processAfterLogout();


                }
            });
    }

    processLogin() {

        if (this.sharedDataService.getUserSelectedUniversitiesList.length > 0) {
            this.userService.createOrUpdateUser()
                .subscribe();
        }
        else {
            this.userService.getUserUniversities().
                subscribe(resp => {
                    this.sharedDataService.processDbUnivChips(resp);
                    this.closeNav();
                });
        }

    }

    determineLoginSituation() {
        this.userService.getLoginStatus().
            subscribe((status: boolean) => {
                if (status) {
                    this.loginLogout = environment.logout
                }
                else {
                    this.loginLogout = environment.login;
                    // this.processAfterLogout();
                }
            })
    }

    processAfterLogout() {
        this.userInfo = null;
        localStorage.clear;
        this.sharedDataService.setUserSelectedUniversitiesList(new Array<University>());
        this.sharedDataService.emitDbUnivChips(new Array<University>());
        this.closeNav();
        this.router.navigate(['/dashboard/']);
    }

    openLoginDialog() {
        return this.dialog.open(LoginModal).
            afterClosed();
    }

    goToAccommodationNotifications() {
        this.closeNav();
        this.router.navigate(['/accommodationNotifications/']);
    }

    goToRecentlyViewed() {
        this.closeNav();
        this.router.navigate(['/recentlyViewed/']);
    }

    ngOnDestroy() { this.loginStatusSubscription.unsubscribe(); }
}    