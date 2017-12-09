import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs/Subject';
import { LandingSearchService } from 'app/dashboard/search/landing.search.service';
import { University } from '../../accommodation/shared/models/universities.list.model';
import { ENTER, COMMA } from '@angular/cdk/keycodes';
import { MatSnackBar } from '@angular/material';

@Component({
  selector: 'landing-search',
  templateUrl: 'landing.search.html'
})
export class LandingSearch {

  universityResults: University[];

  searchTerm$ = new Subject<string>();
  searchDropdownToggle: boolean;

  constructor(private router: Router,
    private searchService: LandingSearchService,
    public snackBar: MatSnackBar) {
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

    if (this.selectedUniversities.length == 4) {
      this.openSnackBar("you can select a max of 4 univs", 'Dismiss');
      return;
    }

    if (this.selectedUniversities.indexOf(universityAcronym) == -1) {
      this.selectedUniversities.push(universityAcronym);
    }
    else {
      this.openSnackBar("Already Selected", universityAcronym);

    }
  }
  removeChip(universityAcronym) {
    let index = this.selectedUniversities.indexOf(universityAcronym);

    if (index >= 0) {
      this.selectedUniversities.splice(index, 1);
    }
  }

  openSnackBar(message: string, action: string) {
    this.snackBar.open(message, action, { duration: 2000 });

  }
}
