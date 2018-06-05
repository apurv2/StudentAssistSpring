import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { FacebookModule, FacebookService, InitParams } from 'ngx-facebook';
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
import { MatChipsModule, MatIconModule, MatSnackBarModule, MatSelectModule, MatInputModule, MatNativeDateModule, MatDialog, MatDialogModule, MatCheckbox, MatCheckboxModule, MatRadioModule, MatSidenavModule, MatListModule, MatDatepicker, MatDatepickerModule, MatTooltipModule, MatProgressBarModule, MatProgressSpinnerModule, } from '@angular/material';

import 'hammerjs';
import { LandingFlashCards } from './dashboard/flashCards/landing.flash.cards';
import { LandingFlashCardsService } from './dashboard/flashCards/landing.flash.cards.service';
import { PostAccommodation } from './accommodation/post/accommodation.post';
import { UserPosts } from './accommodation/post/userPosts/user.posts';
import { MidSection } from './dashboard/midSection/landing.mid.section';
import { SimpleSearchFilterService } from './accommodation/simpleSearch/filters/simple.search.filters.service';
import { Universities } from './universities/universities.list';
import { AdvancedSearchFilters } from './accommodation/advancedSearch/filters/advanced.search.filters';
import { AddsList } from './accommodation/shared/adsList/ads.list';
import { AdvanceSearchService } from './accommodation/advancedSearch/accommodation.advanced.search.service';
import { LoginModal } from 'app/shared/modals/login.modal';
import { httpFactory } from './shared/Interceptor/HttpInterceptorService';
import { UserService } from 'app/shared/userServices/user.service';
import { environment } from 'environments/environment';
import { SubscribeNotificationsModal } from 'app/notifications/notifications.subscribe.modal';
import { NotificationSettingsService } from 'app/notifications/notifications.subscribe.modal.service';
import { NotificationSettingsFilter } from 'app/notifications/subscribe.notifications.filter';
import { Ng2CloudinaryModule } from 'ng2-cloudinary';
import { PostAccommodationService } from 'app/accommodation/post/accommodation.post.service';
import { SuccessOrFailureModal } from 'app/shared/modals/success.or.failure';
import { TopHeader } from 'app/shared/topHeader/top.header';
import { Ng2CarouselamosModule } from 'ng2-carouselamos';
import { UserPostsService } from 'app/accommodation/post/userPosts/user.posts.service';
import { AddDetailsModal } from 'app/accommodation/shared/adDetails/accommodation.details.modal';
import { AddDetailsService } from 'app/accommodation/shared/adDetails/accommodation.details.add.service';
import { CopyLinkModal } from './accommodation/shared/modals/copy.link.modal';
import { NewApartmentModal } from './accommodation/post/newApartment/new.apartment.modal';
import { AgmCoreModule } from '@agm/core';
import { CarouselComponent } from './shared/carousel/carousel.component';
import { AdsListService } from './accommodation/shared/adsList/ads.list.service';
import { AccommodationNotificationService } from './notifications/accommodation/notifications.accommodation.service';
import { AccommodationNotifications } from './notifications/accommodation/notifications.accommodation';
import { RecentlyViewedAccommodations } from './accommodation/recentlyViewed/accommodation.recently.viewed';
import { RecentlyViewedService } from './accommodation/recentlyViewed/accommodation.recently.viewed.service';
import { SimpleSearchGuard } from './shared/utilities/simple.search.guard';

@NgModule({
  imports: [AgmCoreModule.forRoot({
    apiKey: "AIzaSyAVW7umN-Y1GC69fOsqQlCfjus6hW1Ux84",
    libraries: ["places"]
  }),
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
    MatCheckboxModule,
    MatRadioModule,
    Ng2CarouselamosModule,
    MatSidenavModule,
    MatListModule,
    MatNativeDateModule,
    MatDatepickerModule,
    MatTooltipModule,
    MatProgressBarModule,
    MatProgressSpinnerModule
  ],
  declarations: [
    AppComponent,
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
    Universities,
    AdvancedSearchFilters,
    AddsList,
    SubscribeNotificationsModal,
    LoginModal,
    NotificationSettingsFilter,
    SuccessOrFailureModal,
    AddDetailsModal,
    TopHeader,
    CopyLinkModal,
    NewApartmentModal,
    CarouselComponent,
    AccommodationNotifications,
    RecentlyViewedAccommodations
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
    NotificationSettingsService,
    PostAccommodationService,
    UserPostsService,
    AddDetailsService,
    AdsListService,
    AccommodationNotificationService,
    RecentlyViewedService,
    SimpleSearchGuard,
    {
      provide: Http,
      useFactory: httpFactory,
      deps: [XHRBackend, RequestOptions, FacebookService]
    }

  ],
  bootstrap: [AppComponent, TopHeader],
  entryComponents: [SubscribeNotificationsModal, LoginModal, SuccessOrFailureModal, AddDetailsModal, CopyLinkModal, NewApartmentModal]
})
export class AppModule {

  constructor(private fb: FacebookService,
    sharedDataService: SharedDataService) {
    let initParams: InitParams = {
      appId: environment.appId,
      xfbml: true,
      version: 'v2.12'
    };

    fb.init(initParams);
  }
}

