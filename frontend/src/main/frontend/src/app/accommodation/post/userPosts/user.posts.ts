import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs/Subject';
import { UserPostsService } from 'app/accommodation/post/userPosts/user.posts.service';
import { AccommodationAdd } from 'app/accommodation/shared/models/accommodation.model';

@Component({
    selector: 'user-post',
    templateUrl: 'user.posts.html'
})
export class UserPosts {

    constructor(private userPostsService: UserPostsService) {
    }

    accommodationAdds: AccommodationAdd[] = [];
    paginationCount: number = 0;
    ngOnInit() {
        this.getUserPosts();
    }

    getUserPosts() {
        this.userPostsService.getUserPosts(this.paginationCount)
            .subscribe(adds => this.accommodationAdds = adds);

    }

    getNextSetOfUserPosts() {
        this.paginationCount++;
        this.getUserPosts();
    }

}
