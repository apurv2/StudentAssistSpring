import { Component } from "@angular/core";
import { Router } from '@angular/router';
import { SharedDataService } from "app/shared/data/shared.data.service";
import { UserInfo } from "app/shared/models/user.info.model";
import { Subscription } from "rxjs/Subscription";
import { UserService } from "app/shared/userServices/user.service";

@Component({
    selector: 'top-header',
    templateUrl: 'top.header.html'
})

export class TopHeader {

    showSideNav: boolean;
    userInfo: UserInfo;
    userInfoSubscription: Subscription;


    constructor(private router: Router,
        private sharedDataService: SharedDataService,
        private userService: UserService) { }

    ngOnInit() {

        this.subscribeToUserDetails();
    }

    subscribeToUserDetails() {

        this.sharedDataService.observeUserInfo().
            subscribe(userInfo => this.userInfo = userInfo);
    }

    openNav() {
        document.getElementById("mySidenav").style.width = "250px";
        document.getElementById("navoverlap").style.display = "block";
        this.showSideNav = true;
    }

    closeNav() {
        document.getElementById("mySidenav").style.width = "0px";
        document.getElementById("navoverlap").style.display = "none";

        this.showSideNav = false;
    }

    openPostAccommodation() {
        this.router.navigate(['/post/']);
    }

    logout() {
        this.userService.logout().subscribe(res => console.log(res));
    }
}    