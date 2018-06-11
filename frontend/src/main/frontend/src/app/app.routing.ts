import { Routes, RouterModule } from '@angular/router';
import { SimpleSearch } from 'app/accommodation/simpleSearch/accommodation.simple.search';
import { Dashboard } from 'app/dashboard/landing.dashboard';
import { PostAccommodation } from './accommodation/post/accommodation.post';
import { AdvancedSearch } from './accommodation/advancedSearch/accommodation.advanced.search';
import { AddDetails } from './accommodation/shared/adDetails/accommodation.details.add';
import { AccommodationNotifications } from './notifications/accommodation/notifications.accommodation';
import { RecentlyViewedAccommodations } from './accommodation/recentlyViewed/accommodation.recently.viewed';
import { AccommodationGuard } from './shared/utilities/guards/accommodation.guard';

const appRoutes: Routes = [
    {
        path: 'dashboard',
        component: Dashboard
    },
    {
        path: 'simple-search',
        component: SimpleSearch,
        canDeactivate: [AccommodationGuard]
    }, {
        path: 'post',
        component: PostAccommodation,
        canDeactivate: [AccommodationGuard]
    },
    {
        path: 'advanced-search',
        component: AdvancedSearch,
        canDeactivate: [AccommodationGuard]
    },
    {
        path: 'adDetails',
        component: AddDetails,
        data: {
            sharedLink: true
        }
    },
    {
        path: 'accommodationNotifications',
        component: AccommodationNotifications,
        canDeactivate: [AccommodationGuard]
    },
    {
        path: 'recentlyViewed',
        component: RecentlyViewedAccommodations,
        canDeactivate: [AccommodationGuard]
    },
    { path: '**', redirectTo: 'dashboard' }

];

export const routing = RouterModule.forRoot(appRoutes, { useHash: true });
