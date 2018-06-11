import { CanDeactivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { Injectable } from '@angular/core';
import { SimpleSearch } from '../../../accommodation/simpleSearch/accommodation.simple.search';
import { Accommodation } from '../../../accommodation/accommodation';

@Injectable()
export class AccommodationGuard implements CanDeactivate<Accommodation> {

    canDeactivate(component: Accommodation,
        currentRoute: ActivatedRouteSnapshot,
        currentState: RouterStateSnapshot,
        nextState?: RouterStateSnapshot) {
        let backPressed: boolean = component.backPressed;
        component.backPressed = false;

        if (window.innerWidth < 767 && backPressed) {
            if (component.addsList.showingDetails) {
                component.addsList.showingDetails = false;
                component.addsList.showList();
                history.pushState({}, "", currentState.url);
                return false;
            }
            return true;
        }
        return true;
    }

}