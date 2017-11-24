import { Component } from "@angular/core";
import { SharedDataService } from "../shared/data/shared.data.service";
import { transition } from "@angular/core/src/animation/dsl";

@Component({
    selector: 'dashboard',
    templateUrl: 'landing.dashboard.html'
})

export class Dashboard {

    constructor(private sharedDataService: SharedDataService) {
    }

    ngOnInit() {
        this.sharedDataService.setHomePageBackground(true);
    }

    ngOnDestroy() {
        this.sharedDataService.setHomePageBackground(false);
    }

}