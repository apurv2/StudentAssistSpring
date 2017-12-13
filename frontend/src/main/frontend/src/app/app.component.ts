import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { SharedDataService } from './shared/data/shared.data.service';
import { environment } from '../environments/environment';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'app';
  showSideNav: boolean;

  constructor(private router: Router,
    private sharedDataService: SharedDataService) {
  }
  ngOnInit() {
    this.sharedDataService.getHomePageBackground().
      subscribe(status => this.showSideNav = status);

    this.getUserUniversitiesFromLocalStorage();
  }

  getUserUniversitiesFromLocalStorage() {

    if (localStorage.getItem(environment.userSelectedUnivs) != null) {
      this.sharedDataService.setUserSelectedUniversitiesList(
        JSON.parse(localStorage.getItem(environment.userSelectedUnivs)));
    }
  }
}
