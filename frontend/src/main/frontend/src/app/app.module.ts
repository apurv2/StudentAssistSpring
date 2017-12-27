import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { FacebookModule, FacebookService, InitParams } from 'ngx-facebook';
import { TopHeader } from 'app/shared/topHeader/top.header';
import { LeftNav } from 'app/shared/leftNav/left.nav';
import { routing } from 'app/app.routing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AdvancedSearch } from 'app/accommodation/advancedSearch/accommodation.advanced.search';
import { SimpleSearch } from 'app/accommodation/simpleSearch/accommodation.simple.search';
import { AddDetails } from 'app/accommodation/shared/adDetails/accommodation.details.add';
import { UniversitiesService } from 'app/universities/universities.list.service';
import { Http, HttpModule, XHRBackend, RequestOptions } from '@angular/http';
import { HttpInterceptorService } from 'app/shared/Interceptor/HttpInterceptorService';
import { Dashboard } from 'app/dashboard/landing.dashboard';
import { DriverComponent } from 'app/airport/dashboards/driver/driver.dashboard';
import { OrganizerComponent } from 'app/airport/dashboards/organizer/organizer.dashboard';
import { RidesComponent } from 'app/airport/dashboards/rides/rides.component';
import { UpcomingComponent } from 'app/airport/dashboards/driver/upcoming/upcoming.rides';
import { CalendarComponent } from 'app/airport/dashboards/driver/calendar/calendar.rides';
import { LandingSearch } from 'app/dashboard/search/landing.search';
import { LandingSearchService } from 'app/dashboard/search/landing.search.service';
import { SimpleSearchService } from './accommodation/simpleSearch/accommodation.simple.search.service';
import { SimpleSearchAddsList } from './accommodation/simpleSearch/adsList/simple.search.adds.list';
import { SimpleSearchAddsFilters } from './accommodation/simpleSearch/filters/simple.search.filters';
import { SharedDataService } from './shared/data/shared.data.service';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatChipsModule, MatIconModule, MatSnackBarModule, MatSelectModule, MatInputModule, MatNativeDateModule, MatDialog, MatDialogModule, } from '@angular/material';

import 'hammerjs';
import { LandingFlashCards } from './dashboard/flashCards/landing.flash.cards';
import { LandingFlashCardsService } from './dashboard/flashCards/landing.flash.cards.service';
import { PostAccommodation } from './accommodation/post/accommodation.post';
import { UserPosts } from './accommodation/post/userPosts/user.posts';
import { MidSection } from './dashboard/midSection/landing.mid.section';
import { SimpleSearchFilterService } from './accommodation/simpleSearch/filters/simple.search.filters.service';
import { ChipsInputExample } from 'app/airport/airport.list';
import { NotificationSettingsModal } from './notifications/notifications.subscribe.modal';
import { Universities } from './universities/universities.list';
import { AdvancedSearchFilters } from './accommodation/advancedSearch/filters/advanced.search.filters';
import { AddsList } from './accommodation/shared/adsList/ads.list';
import { AdvanceSearchService } from './accommodation/advancedSearch/accommodation.advanced.search.service';
import { SubscribeNotificationsModal } from 'app/accommodation/shared/modals/subscribe.notifications.modal';
import { LoginModal } from 'app/shared/modals/login.modal';
import { httpFactory } from './shared/Interceptor/HttpInterceptorService';
import { UserService } from 'app/shared/userServices/user.service';

@NgModule({
  imports: [
    BrowserModule,
    FacebookModule.forRoot(),
    routing,
    HttpModule,
    FormsModule,
    BrowserAnimationsModule,
    MatChipsModule,
    MatIconModule,
    MatSnackBarModule,
    MatSelectModule,
    MatInputModule,
    ReactiveFormsModule,
    MatDialogModule,

  ],
  declarations: [
    AppComponent,
    TopHeader,
    LeftNav,
    SimpleSearch,
    AdvancedSearch,
    AddDetails,
    Dashboard,
    DriverComponent,
    OrganizerComponent,
    RidesComponent,
    UpcomingComponent,
    CalendarComponent,
    LandingSearch,
    LandingFlashCards,
    SimpleSearchAddsList,
    SimpleSearchAddsFilters,
    PostAccommodation,
    UserPosts,
    MidSection,
    ChipsInputExample,
    NotificationSettingsModal,
    Universities,
    AdvancedSearchFilters,
    AddsList,
    SubscribeNotificationsModal,
    LoginModal

  ],
  providers: [UniversitiesService,
    HttpInterceptorService,
    LandingSearchService,
    SimpleSearchService,
    SharedDataService,
    LandingFlashCardsService,
    SimpleSearchFilterService,
    AdvanceSearchService,
    MatDialog,
    UserService,
    {
      provide: Http,
      useFactory: httpFactory,
      deps: [XHRBackend, RequestOptions, FacebookService]
    }

  ],
  bootstrap: [AppComponent, TopHeader, LeftNav],
  entryComponents: [SubscribeNotificationsModal, LoginModal]
})
export class AppModule {

  constructor(private fb: FacebookService,
    sharedDataService: SharedDataService) {
    let initParams: InitParams = {
      appId: '931333680308184',
      xfbml: true,
      version: 'v2.8'
    };

    fb.init(initParams);
  }
}
