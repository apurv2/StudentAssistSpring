import { CanDeactivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Injectable } from '@angular/core';
import { RecentlyViewedService } from '../../accommodation/recentlyViewed/accommodation.recently.viewed.service';
import { SimpleSearch } from '../../accommodation/simpleSearch/accommodation.simple.search';
import { environment } from 'environments/environment';

@Injectable()
export class SimpleSearchGuard implements CanDeactivate<SimpleSearch> {

    canDeactivate(component: SimpleSearch,
        currentRoute: ActivatedRouteSnapshot,
        currentState: RouterStateSnapshot,
        nextState?: RouterStateSnapshot) {
        let backPressed: boolean = component.backPressed;
        component.backPressed = false;

        if (window.innerWidth < 767 && backPressed) {
            if (component.simpleSearchList.showingDetails) {
                component.simpleSearchList.showingDetails = false;
                component.simpleSearchList.showList();
                history.pushState({}, "", currentState.url);
                return false;
            }
            return true;
        }
        return true;
    }

}