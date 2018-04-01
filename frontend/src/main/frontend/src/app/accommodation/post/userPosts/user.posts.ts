import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs/Subject';
import { UserPostsService } from 'app/accommodation/post/userPosts/user.posts.service';
import { AccommodationAdd } from 'app/accommodation/shared/models/accommodation.model';
import { UserService } from 'app/shared/userServices/user.service';
import { MatDialog } from '@angular/material';
import { SharedDataService } from 'app/shared/data/shared.data.service';
import { AddDetailsModal } from 'app/accommodation/shared/adDetails/accommodation.details.modal';
import { Subscription } from 'rxjs/Subscription';

@Component({
    selector: 'user-post',
    templateUrl: 'user.posts.html'
})
export class UserPosts {

    constructor(private userPostsService: UserPostsService,
        private userService: UserService,
        private dialog: MatDialog,
        private sharedDataService: SharedDataService) {
    }

    accommodationAdds: AccommodationAdd[] = [];
    paginationCount: number = 0;
    addDetailsSubscription: Subscription;
    ngOnInit() {
        this.paginationCount = 0;
        this.getUserPosts();
        this.subscribeAddClick();
    }

    getUserPosts() {

        this.userService.getLoginStatus()
            .filter(resp => resp)
            .switchMap(resp => this.userPostsService.getUserPosts(this.paginationCount))
            .subscribe(adds => this.accommodationAdds = adds);

    }

    getNextSetOfUserPosts() {
        this.paginationCount++;
        this.getUserPosts();
    }

    subscribeAddClick() {
        this.addDetailsSubscription = this.sharedDataService.observeAccommodationAdd()
            .skip(1)
            .subscribe(accommodationAdd => this.openAddDetails(accommodationAdd));
    }

    openAddDetails(accommodationAdd: AccommodationAdd) {
        this.dialog.open(AddDetailsModal, {
            data: accommodationAdd
        });

    }

    ngOnDestroy() {
        this.addDetailsSubscription.unsubscribe();
    }

}
