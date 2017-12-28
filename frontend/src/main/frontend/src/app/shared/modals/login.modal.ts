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


@Component({
    selector: 'dlogin-modal',
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
            map(resp => this.userService.createOrUpdateUser()).
            subscribe(resp => this.dialogRef.close());
    }

    onNoClick(): void {
        this.dialogRef.close();
    }
}