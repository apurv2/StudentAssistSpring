import { FacebookService, LoginResponse, InitParams } from 'ngx-facebook';
import { Component } from '@angular/core';
import { UniversitiesService } from 'app/universities/universities.list.service';
import { Router } from '@angular/router';

@Component({
    selector: 'login',
    templateUrl: 'login.html',
    styleUrls: ['login.component.css'],
})
export class Login {

    constructor(private fb: FacebookService,
        private universitiesService: UniversitiesService,
        private router: Router,
    ) { }

    ngOnInit() {

        this.fb.getLoginStatus().then((response: LoginResponse) => {
            if (response.status === 'connected') {
                this.universitiesService.getUniversityDetailsForUser().subscribe(res => {
                    if (res.response === 'true') {
                        this.router.navigate(['/dashboard']);
                    } else {
                        this.router.navigate(['/universitiesList']);
                    }
                });
            }
        });
    }

    facebookLogin() {
        this.fb.login()
            .then((response: LoginResponse) => {
                console.log(response);
                console.log("logged In");

            })
            .catch((error: any) => console.error(error));
    }
}