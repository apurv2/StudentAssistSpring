import { Subject } from "rxjs/Subject";
import { Observable } from "rxjs/Observable";
import { AccommodationSearchModel } from "../../accommodation/shared/models/accommodation.filter.model";
import { University } from "../../accommodation/shared/models/universities.list.model";
import { AccommodationAdd } from "../../accommodation/shared/models/accommodation.model";
import { MatSnackBar } from "@angular/material";
import { UserModel } from "../models/user.model";
import { Http, Response } from '@angular/http';
import { environment } from "../../../environments/environment";
import { HttpInterceptorService } from "../Interceptor/HttpInterceptorService";
import { FacebookService } from "ngx-facebook/dist/esm/providers/facebook";
import { AuthResponse } from "ngx-facebook/dist/esm/models/auth-response";
import { LoginResponse } from "ngx-facebook/dist/esm/models/login-response";
import { Observer } from "rxjs/Observer";
import { UserInfo } from "app/shared/models/user.info.model";
import { Apartment } from "app/accommodation/shared/models/apartment.names.model";

export class SharedDataService {

    showHomepageBackground = new Subject<boolean>();
    accommomdationSearchFilters = new Subject<AccommodationSearchModel>();
    userSelectedUniversitiesList: University[];
    accommodationAdd = new Subject<AccommodationAdd>();
    dbUnivChips = new Subject<University[]>();
    userInfo = new Subject<UserInfo>();
    apartmentNames: Apartment[];

    observeUserInfo(): Observable<UserInfo> {
        return this.userInfo;
    }

    emitUserInfo(userInfo: UserInfo) {
        this.userInfo.next(userInfo);
    }

    observeDbUnivChips(): Observable<University[]> {
        return this.dbUnivChips;
    }

    emitDbUnivChips(univChips: University[]) {
        this.dbUnivChips.next(univChips);
    }
    getApartmentNames(): Apartment[] {
        return this.apartmentNames;
    }

    setApartmentNames(apartmentNames: Apartment[]) {
        this.apartmentNames = apartmentNames
    }
    emitAccommodationAdd(accommodationAdd: AccommodationAdd) {
        return this.accommodationAdd.next(accommodationAdd);
    }

    observeAccommodationAdd(): Observable<AccommodationAdd> {
        return this.accommodationAdd;
    }

    getUserSelectedUniversitiesList(): University[] {
        return this.userSelectedUniversitiesList;
    }

    setUserSelectedUniversitiesList(universities: University[]) {
        this.userSelectedUniversitiesList = universities;
    }

    observeAccommomdationSearchFilters(): Observable<AccommodationSearchModel> {
        return this.accommomdationSearchFilters;
    }

    emitAccommomdationSearchFilters(filterData: AccommodationSearchModel) {
        this.accommomdationSearchFilters.next(filterData);
    }

    openSnackBar(snackbar: MatSnackBar, message: string, action: string) {
        snackbar.open(message, action, { duration: 2000 });
    }

}