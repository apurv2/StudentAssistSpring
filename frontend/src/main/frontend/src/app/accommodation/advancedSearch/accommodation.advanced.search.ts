import { FacebookService, LoginResponse } from 'ngx-facebook';
import { Component } from '@angular/core';

@Component({
    selector: 'advanced-search',
    templateUrl: 'accommodation.advanced.search.html'
})
export class AdvancedSearch {

    constructor(private fb: FacebookService) {

        // this.fb.getLoginStatus

    }


}