import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs/Subject';
import { LandingSearchService } from 'app/dashboard/search/landing.search.service';
import { University } from '../../accommodation/shared/models/universities.list.model';
import { ENTER, COMMA } from '@angular/cdk/keycodes';

@Component({
  selector: 'landing-search',
  templateUrl: 'landing.search.html'
})
export class LandingSearch {

  universityResults: University[];

  searchTerm$ = new Subject<string>();
  searchDropdownToggle: boolean;

  constructor(private router: Router,
    private searchService: LandingSearchService) {
  }

  selectedUniversities = new Array<string>();
  addOnBlur: boolean = true;
  separatorKeysCodes = [ENTER, COMMA];


  ngOnInit() {

    this.searchService.search(this.searchTerm$)
      .subscribe(results => {
        this.universityResults = results;
        this.searchDropdownToggle = this.universityResults.length > 0 ? true : false;
      });
  }

  search() {
    this.searchDropdownToggle = !this.searchDropdownToggle;
  }
  addChip(universityAcronym) {
    this.selectedUniversities.push(universityAcronym);
  }
  removeChip(universityAcronym) {
    this.selectedUniversities.splice(universityAcronym);
  }
}
