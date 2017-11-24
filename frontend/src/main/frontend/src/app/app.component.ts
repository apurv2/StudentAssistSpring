import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { SharedDataService } from './shared/data/shared.data.service';

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
  }
}
