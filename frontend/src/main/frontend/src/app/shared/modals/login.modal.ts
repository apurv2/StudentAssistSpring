import { Component, Inject } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material';
import { FacebookService } from 'ngx-facebook/dist/esm/providers/facebook';
import { LoginResponse } from 'ngx-facebook/dist/esm/models/login-response';
import { environment } from '../../../environments/environment';
import { SharedDataService } from '../data/shared.data.service';
import { University } from '../../accommodation/shared/models/universities.list.model';
import { UserModel } from '../models/user.model';
import { Http, Response } from '@angular/http';
import { HttpInterceptorService } from '../Interceptor/HttpInterceptorService';
import 'rxjs/add/operator/map';
import { UserService } from 'app/shared/userServices/user.service';
import { Observable } from 'rxjs/Observable';


@Component({
    selector: 'login-modal',
    templateUrl: 'login.modal.html',
})
export class LoginModal {

    constructor(public dialogRef: MatDialogRef<LoginModal>,
        private fb: FacebookService,
        private sharedDataService: SharedDataService,
        private http: Http,
        private userService: UserService) { }

    login() {
        this.userService.login().
            flatMap(resp => this.userService.createOrUpdateUser()).
            switchMap(resp => resp ? this.userService.getUserUniversities() :
                this.userService.returnFalseObservable()).
            subscribe(resp => resp ? this.processLogin() : null);

    }

    processLogin() {

        var cookies = document.cookie.split(";");
        for (var i = 0; i < cookies.length; i++) {
            var cookie = cookies[i];
            var eqPos = cookie.indexOf("=");
            var name = eqPos > -1 ? cookie.substr(0, eqPos) : cookie;
            document.cookie = name + "=n";
        }

    }

    onNoClick(): void {
        this.dialogRef.close();
    }
}