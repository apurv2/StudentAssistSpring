import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { SharedDataService } from './shared/data/shared.data.service';
import { environment } from '../environments/environment';
import { UserService } from './shared/userServices/user.service';
import { Observable } from 'rxjs/Observable';
import { University } from 'app/universities/universities.model';
import 'rxjs/add/operator/mergeMap';
import 'rxjs/add/observable/from';
import { Observer } from 'rxjs/Observer';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';
  showSideNav: boolean;

  constructor(private router: Router,
    private sharedDataService: SharedDataService,
    private userService: UserService,
  ) {
  }
  ngOnInit() {

    if (!this.getUserUniversitiesFromLocalStorage()) {

      this.userService.getLoginStatus().
        switchMap(resp => resp ? this.userService.getUserUniversities() : null).
        subscribe(resp => this.processDbUnivChips(resp));

    }
  }

  processDbUnivChips(chips: University[]) {
    this.sharedDataService.setUserSelectedUniversitiesList(chips);
    localStorage.setItem(environment.userSelectedUnivs,
      JSON.stringify(chips));
    this.sharedDataService.emitDbUnivChips(chips);
  }


  getUserUniversitiesFromLocalStorage(): boolean {

    if (localStorage.getItem(environment.userSelectedUnivs) != null) {
      this.sharedDataService.setUserSelectedUniversitiesList(
        JSON.parse(localStorage.getItem(environment.userSelectedUnivs)));
      return true;
    }
    else
      return false;
  }
}
