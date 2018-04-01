import { Component, HostListener, ElementRef, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs/Subject';
import { ENTER, COMMA } from '@angular/cdk/keycodes';
import { MatSnackBar } from '@angular/material';
import { SharedDataService } from '../../shared/data/shared.data.service';
import { LandingSearchService } from './landing.search.service';
import { environment } from '../../../environments/environment';
import { Subscription } from 'rxjs/Subscription';
import { UserService } from 'app/shared/userServices/user.service';
import { UserModel } from 'app/shared/models/user.model';
import { University } from 'app/universities/universities.model';

@Component({
  selector: 'landing-search',
  templateUrl: 'landing.search.html'
})
export class LandingSearch {

  universityResults: University[];

  searchTerm$ = new Subject<string>();
  searchDropdownToggle: boolean;
  searchBarText: string = "";
  toolTipText: string = "";
  @ViewChild('searchPanel') searchPanel: ElementRef;

  constructor(private router: Router,
    private searchService: LandingSearchService,
    public snackBar: MatSnackBar,
    private sharedDataService: SharedDataService,
    private userService: UserService) {
  }

  selectedUniversities: University[];
  addOnBlur: boolean = true;
  separatorKeysCodes = [ENTER, COMMA];
  searchBarSubscription: Subscription;
  dbUnivChips: Subscription;
  selectable: boolean = true;
  removable: boolean = true;
  toolTipPosition: string = "right";

  ngOnInit() {

    this.searchBarSubscription = this.searchService.search(this.searchTerm$)
      .subscribe(results => {
        this.universityResults = results;
        this.searchDropdownToggle = this.universityResults.length > 0 ? true : false;
      });

    this.getUserUnivsFromDataService();
    this.subscribeForDBChips();
  }

  subscribeForDBChips() {
    this.dbUnivChips = this.sharedDataService.observeDbUnivChips().
      subscribe(chips => this.selectedUniversities = chips);
  }

  getUserUnivsFromDataService() {

    this.selectedUniversities = this.sharedDataService.getUserSelectedUniversitiesList() != null ?
      this.sharedDataService.getUserSelectedUniversitiesList() : new Array<University>();

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
        this.universityResults.length = 0;
        this.searchBarText = '';
        this.searchTerm$.next('');
        this.selectedUniversities.push(university);
        this.sharedDataService.setUserSelectedUniversitiesList(this.selectedUniversities);
        localStorage.setItem(environment.userSelectedUnivs,
          JSON.stringify(this.selectedUniversities));
        this.updateDBChips();
      }
  }
  removeChip(university: University) {
    let index = this.selectedUniversities.indexOf(university);

    if (index >= 0) {
      this.selectedUniversities.splice(index, 1);
      this.sharedDataService.setUserSelectedUniversitiesList(this.selectedUniversities);
      localStorage.setItem(environment.userSelectedUnivs,
        JSON.stringify(this.selectedUniversities));

      this.updateDBChips();
    }
  }

  updateDBChips() {
    this.userService
      .getLoginStatus()
      .filter(resp => resp)
      .switchMap(resp => this.userService.createOrUpdateUser())
      .subscribe();

  }
  onEnterClicked() {
    if (this.selectedUniversities.length > 0)
      this.router.navigate(['/simple-search']);
  }

  @HostListener('document:keydown.escape', ['$event']) onKeydownHandler(evt: KeyboardEvent) {
    this.clearSearchResults();
  }
  private parentNode: any;
  ngAfterViewInit(): void {
    this.parentNode = this.searchPanel;
  }

  @HostListener('document:click', ['$event.path'])
  onClickOutside($event: Array<any>) {
    const elementRefInPath = $event.find(node => node === this.parentNode);
    if (!elementRefInPath) {
      this.clearSearchResults();
    }
  }

  clearSearchResults() {
    if (this.universityResults) {
      this.universityResults.length = 0;
      this.searchDropdownToggle = false;
      this.searchTerm$.next('');
      this.searchBarText = '';

    }
  }

  ngOnDestroy() {
    this.searchBarSubscription.unsubscribe();
    this.dbUnivChips.unsubscribe();
  }

}

