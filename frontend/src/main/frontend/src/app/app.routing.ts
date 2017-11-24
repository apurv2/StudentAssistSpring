import { Routes, RouterModule } from '@angular/router';
import { SimpleSearch } from 'app/accommodation/simpleSearch/accommodation.simple.search';
import { AdvancedSearch } from 'app/accommodation/advancedSearch/accommodation.advanced.search';
import { Login } from 'app/shared/login/login';
import { Dashboard } from 'app/dashboard/landing.dashboard';

const appRoutes: Routes = [
    {
        path: 'login',
        component: Login
    }, {
        path: 'dashboard',
        component: Dashboard
    },
    {
        path: 'simple-search',
        component: SimpleSearch
    },
    { path: '**', redirectTo: 'dashboard' }

];

export const routing = RouterModule.forRoot(appRoutes, { useHash: true });