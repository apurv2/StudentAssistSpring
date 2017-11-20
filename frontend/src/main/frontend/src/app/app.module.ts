import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { FacebookModule, FacebookService, InitParams } from 'ngx-facebook';
import { TopHeader } from 'app/shared/topHeader/top.header';
import { LeftNav } from 'app/shared/leftNav/left.nav';
import { routing } from 'app/app.routing';
import { AdvancedSearch } from 'app/accommodation/advancedSearch/accommodation.advanced.search';
import { SimpleSearch } from 'app/accommodation/simpleSearch/accommodation.simple.search';
import { AddDetails } from 'app/accommodation/shared/adDetails/accommodation.details.add';
import { Login } from 'app/shared/login/login';
import { UniversitiesService } from 'app/universities/universities.list.service';
import { Http, HttpModule, XHRBackend, RequestOptions } from '@angular/http';
import { httpFactory } from '@angular/http/src/http_module';
import { HttpInterceptorService } from 'app/shared/Interceptor/HttpInterceptorService';
import { Dashboard } from 'app/dashboard/landing.dashboard';
import { DriverComponent } from 'app/airport/dashboards/driver/driver.dashboard';
import { OrganizerComponent } from 'app/airport/dashboards/organizer/organizer.dashboard';
import { RidesComponent } from 'app/airport/dashboards/rides/rides.component';
import { UpcomingComponent } from 'app/airport/dashboards/driver/upcoming/upcoming.rides';
import { CalendarComponent } from 'app/airport/dashboards/driver/calendar/calendar.rides';
import { LandingSearch } from 'app/dashboard/search/landing.search';
import { LandingSearchService } from 'app/dashboard/search/landing.search.service';
import { LandingSearchResults } from './dashboard/results/landing.search.results';

@NgModule({
  declarations: [
    AppComponent,
    TopHeader,
    LeftNav,
     SimpleSearch,
    AdvancedSearch,
    AddDetails,
    Login,
    Dashboard,
    DriverComponent,
    OrganizerComponent,
    RidesComponent,
    UpcomingComponent,
    CalendarComponent,
    LandingSearch,
    LandingSearchResults
  ],
  imports: [
    BrowserModule,
    FacebookModule.forRoot(),
    routing,
    HttpModule

  ],
  providers: [UniversitiesService, HttpInterceptorService,LandingSearchService

  ],
  bootstrap: [AppComponent, TopHeader, LeftNav]
})
export class AppModule {

  constructor(private fb: FacebookService
  ) {
    let initParams: InitParams = {
      appId: '931333680308184',
      xfbml: true,
      version: 'v2.8'
    };

    // fb.init(initParams);

  }
}
