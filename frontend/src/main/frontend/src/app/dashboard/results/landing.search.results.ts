import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs/Subject';

@Component({
    selector: 'landing-search-results',
    templateUrl: 'landing.search.results.html'
})
export class LandingSearchResults {

    constructor(private router: Router) {
    }

    ngOnInit() {
    }

    searchResultCardClick() {

        this.router.navigate(['/simple-search/']);

    }

}
