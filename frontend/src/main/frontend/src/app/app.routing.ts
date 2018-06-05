import { Routes, RouterModule } from '@angular/router';
import { SimpleSearch } from 'app/accommodation/simpleSearch/accommodation.simple.search';
import { Dashboard } from 'app/dashboard/landing.dashboard';
import { PostAccommodation } from './accommodation/post/accommodation.post';
import { AdvancedSearch } from './accommodation/advancedSearch/accommodation.advanced.search';
import { AddDetails } from './accommodation/shared/adDetails/accommodation.details.add';
import { AccommodationNotifications } from './notifications/accommodation/notifications.accommodation';
import { RecentlyViewedAccommodations } from './accommodation/recentlyViewed/accommodation.recently.viewed';
import { SimpleSearchGuard } from './shared/utilities/simple.search.guard';

const appRoutes: Routes = [
    {
        path: 'dashboard',
        component: Dashboard
    },
    {
        path: 'simple-search',
        component: SimpleSearch,
        canDeactivate: [SimpleSearchGuard]
    }, {
        path: 'post',
        component: PostAccommodation
    },
    {
        path: 'advanced-search',
        component: AdvancedSearch
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
        component: AccommodationNotifications
    },
    {
        path: 'recentlyViewed',
        component: RecentlyViewedAccommodations
    },
    { path: '**', redirectTo: 'dashboard' }

];

export const routing = RouterModule.forRoot(appRoutes, { useHash: true });
