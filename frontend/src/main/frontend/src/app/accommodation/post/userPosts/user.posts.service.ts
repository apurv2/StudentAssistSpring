import { Injectable } from "@angular/core";
import { environment } from "environments/environment";
import { Http } from '@angular/http';

@Injectable()
export class UserPostsService {

    constructor(private http: Http) { }

    getUserPosts(position: number) {
        return this.http.get(environment.getUserPosts + "?position=" + position)
            .map(res => res.json());
    }

}