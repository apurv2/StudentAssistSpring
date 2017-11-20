import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs/Subject';
import { LandingSearchService } from 'app/dashboard/search/landing.search.service';

@Component({
  selector: 'landing-search',
  templateUrl: 'landing.search.html'
})
export class LandingSearch {

  results: Object[];
  searchTerm$ = new Subject<string>();
  searchDropdownToggle: boolean;

  constructor(private router: Router,
    private searchService: LandingSearchService) {
  }

  ngOnInit() {

    this.searchService.search(this.searchTerm$)
      .subscribe(results => {
        this.results = results.results;
        this.searchDropdownToggle = this.results.length > 0 ? true : false;
      });
  }

  search() {
    console.log("came here");
    this.searchDropdownToggle = !this.searchDropdownToggle;

  }

}
