import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs/Subject';
import { University } from '../../accommodation/shared/models/universities.list.model';
import { ENTER, COMMA } from '@angular/cdk/keycodes';
import { MatSnackBar } from '@angular/material';
import { SharedDataService } from '../../shared/data/shared.data.service';
import { LandingSearchService } from './landing.search.service';
import { environment } from '../../../environments/environment';

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
    public snackBar: MatSnackBar,
    private sharedDataService: SharedDataService) {
  }

  selectedUniversities = new Array<University>();
  addOnBlur: boolean = true;
  separatorKeysCodes = [ENTER, COMMA];

  ngOnInit() {

    this.searchService.search(this.searchTerm$)
      .subscribe(results => {
        this.universityResults = results;
        this.searchDropdownToggle = this.universityResults.length > 0 ? true : false;
      });

    this.getUserUnivsFromDataService();
  }

  getUserUnivsFromDataService() {

    this.selectedUniversities = this.sharedDataService.getUserSelectedUniversitiesList();

  }
  search() {
    this.searchDropdownToggle = !this.searchDropdownToggle;
  }

  addChip(university: University) {
    if (this.selectedUniversities.length == 4) {
      this.sharedDataService.openSnackBar(this.snackBar, "you can select a max of 4 univs", 'Dismiss');
    }
    else
      if (this.selectedUniversities.indexOf(university) != -1) {
        this.sharedDataService.openSnackBar(this.snackBar, "Already Selected", university.univAcronym);
      }
      else {
        this.selectedUniversities.push(university);
        this.sharedDataService.setUserSelectedUniversitiesList(this.selectedUniversities);
        localStorage.setItem(environment.userSelectedUnivs,
          JSON.stringify(this.selectedUniversities));
      }
  }
  removeChip(university: University) {
    let index = this.selectedUniversities.indexOf(university);

    if (index >= 0) {
      this.selectedUniversities.splice(index, 1);
      this.sharedDataService.setUserSelectedUniversitiesList(this.selectedUniversities);
    }
  }



}
