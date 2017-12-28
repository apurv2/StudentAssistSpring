import { Injectable } from "@angular/core";
import { UserModel } from "../models/user.model";
import { environment } from "../../../environments/environment";
import { Observable } from "rxjs/Observable";
import { Observer } from "rxjs/Observer";
import { Http } from '@angular/http';
import { LoginResponse } from "ngx-facebook/dist/esm/models/login-response";
import { FacebookService } from "ngx-facebook/dist/esm/providers/facebook";
import { University } from "app/accommodation/shared/models/universities.list.model";
import { SharedDataService } from "app/shared/data/shared.data.service";

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

        for (let university of userUniversities) {
            universityIds.push(university.universityId);
        }
        user.selectedUniversityIds = universityIds;
        
        return this.http.put(environment.createUser, user).
            map(res => res.json());
    }

    getLoginStatus(): Observable<boolean> {

        return Observable.create((observer: Observer<boolean>) =>
            this.fb.getLoginStatus().then((resp: LoginResponse) =>
                observer.next(resp.status === environment.connected ? true : false)))
    }

    login(): Observable<LoginResponse> {

        return Observable.create((observer: Observer<LoginResponse>) =>
            this.fb.login().then((resp: LoginResponse) =>
                observer.next(resp)))
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

}