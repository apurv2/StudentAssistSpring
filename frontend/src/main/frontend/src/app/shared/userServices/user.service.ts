import { Injectable } from "@angular/core";
import { UserModel } from "../models/user.model";
import { environment } from "../../../environments/environment";
import { Observable } from "rxjs/Observable";
import { Observer } from "rxjs/Observer";
import { Http } from '@angular/http';
import { LoginResponse } from "ngx-facebook/dist/esm/models/login-response";
import { FacebookService } from "ngx-facebook/dist/esm/providers/facebook";
import { SharedDataService } from "app/shared/data/shared.data.service";
import { UserInfo } from "app/shared/models/user.info.model";
import { University } from "app/universities/universities.model";

@Injectable()
export class UserService {

    constructor(private fb: FacebookService,
        private sharedDataService: SharedDataService,
        private http: Http) { }

    createOrUpdateUser() {

        let userUniversities: University[] = this.sharedDataService.
            getUserSelectedUniversitiesList();

        let universityIds: number[] = new Array();
        let user: UserModel = new UserModel();

        if (userUniversities && userUniversities.length > 0) {
            for (let university of userUniversities) {
                universityIds.push(university.universityId);
            }
            user.selectedUniversityIds = universityIds;
        }
        return this.http.put(environment.createUser, user).
            map(res => res.json());
    }

    getLoginStatus(): Observable<boolean> {
        console.log("came to login status")
        return Observable.create((observer: Observer<boolean>) =>
            this.fb.getLoginStatus().then(resp =>
                observer.next(resp.status === environment.connected ? true : false)));
    }

    login(): Observable<boolean> {

        return Observable.create((observer: Observer<boolean>) =>
            this.fb.login().then((resp: LoginResponse) =>
                observer.next(resp.status === environment.connected ? true : false)));
    }

    logout(): Observable<boolean> {

        return Observable.create((observer: Observer<boolean>) =>
            this.fb.logout().then((resp: LoginResponse) =>
                observer.next(resp.status === environment.connected ? true : false)))
    }

    getUserUniversities() {
        return this.http.get(environment.getUserUniversities).
            map(res => res.json());
    }

    getUserInfoFromFb(): Observable<UserInfo> {
        return Observable.create((observer: Observer<UserInfo>) =>
            this.fb.api('/me/', 'get').then(resp => {

                let userInfo: UserInfo = new UserInfo();
                userInfo.fullName = resp.name;
                userInfo.userId = resp.id;

                observer.next(userInfo);
            }));
    }

    returnFalseObservable(): Observable<boolean> {
        return Observable.create().next(false);
    }

}