import { Injectable } from "@angular/core";
import { Http } from "@angular/http";
import { AccommodationAdd } from "../models/accommodation.model";
import { UserService } from "../../../shared/userServices/user.service";
import { environment } from "environments/environment";

@Injectable()
export class AdsListService {

    constructor(public http: Http,
        private userService: UserService) { }

    setUserVisitedAdd(accommodationAdd: AccommodationAdd) {
        return this.userService.getLoginStatus()
            .filter(resp => resp)
            .switchMap(resp => this.setUserVisistedAddApi(environment.setUserVisitedAdds, accommodationAdd))
            .filter(e => e.response == environment.success)
    }

    setUserVisistedAddApi(url: string, add: AccommodationAdd) {
        return this.http.put(url, add)
            .map(res => res.json());

    }
}