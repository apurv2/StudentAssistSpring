import { Subject } from "rxjs/Subject";
import { Observable } from "rxjs/Observable";
import { AccommodationSearchModel } from "../../accommodation/shared/models/accommodation.filter.model";
import { AccommodationAdd } from "../../accommodation/shared/models/accommodation.model";
import { MatSnackBar, MatDialog } from "@angular/material";
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
import { University } from "app/universities/universities.model";
import { SuccessOrFailureModal } from "../modals/success.or.failure";

export class SharedDataService {

    showHomepageBackground = new Subject<boolean>();
    accommomdationSearchFilters = new Subject<AccommodationSearchModel>();
    userSelectedUniversitiesList: University[];
    accommodationAdd = new Subject<AccommodationAdd>();
    dbUnivChips = new Subject<University[]>();
    userInfo = new Subject<UserInfo>();
    allUnivDetails: Apartment[];
    flashCardUniversityID: number;
    loginStatus = new Subject<boolean>();

    observeLoginStatus(): Observable<boolean> {
        return this.loginStatus;
    }

    emitLoginStatus(loginStatus: boolean) {
        this.loginStatus.next(loginStatus);
    }

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
        return this.allUnivDetails;
    }

    setApartmentNames(allUnivDetails: Apartment[]) {
        this.allUnivDetails = allUnivDetails
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

    openSuccessFailureDialog(response, dialog: MatDialog, message?: string) {
        let data: any = {};
        data.response = response.response;
        data.message = message;
        data.success = response.response === environment.success ? true : false;
        return dialog.open(SuccessOrFailureModal, { data: data }).afterClosed();
    }
    processDbUnivChips(chips: University[]) {

        if (chips != null && chips.length > 0) {
            this.setUserSelectedUniversitiesList(chips);
            localStorage.setItem(environment.userSelectedUnivs,
                JSON.stringify(chips));
            this.emitDbUnivChips(chips);
        }
    }


}
