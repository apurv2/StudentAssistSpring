import { environment } from "environments/environment";
import { SimpleSearchService } from "app/accommodation/simpleSearch/accommodation.simple.search.service";
import { AccommodationAdd } from "app/accommodation/shared/models/accommodation.model";
import { Component } from "@angular/core";
import { FacebookService } from "ngx-facebook";
import { Aparment } from 'app/accommodation/shared/models/accommodation.apartments';

@Component({
    selector: 'simple-search',
    templateUrl: 'accommodation.simple.search.html'
})@Component({
    selector: 'simple-search',
    templateUrl: 'accommodation.simple.search.html'
})

export class SimpleSearch {
    leftSpinnerValues: any = environment.leftSpinnerValues;
    rightSpinnerValues: any;
    leftSpinner: string;
    rightSpinner: string;
    selectedAccommodationAdd: AccommodationAdd;
    apartmentNames: Aparment[];

    constructor(private simpleSearchService: SimpleSearchService,
        private facebookService: FacebookService) {

    }
    ngOnInit() {
        this.facebookService.getLoginStatus().then(
            function (response) {
                if (response.status === 'connected') {
                    this.leftSpinnerClick(environment.leftSpinnerValues[0].code);
                } else {
                    //  $state.go('leftNav.Login');
                }
            });
    }

    leftSpinnerClick($event) {
        if ($event == environment.APARTMENT_TYPE) {

            this.rightSpinnerValues = environment.apartmentTypes;
            this.getSimpleSearchAdds($event, this.rightSpinner);
        }
        else if ($event == environment.APARTMENT_NAME) {

            this.simpleSearchService.getAllApartmentnames()
                .subscribe(res => this.apartmentNames = res.json());

        }
        else if ($event == environment.GENDER) {

            this.rightSpinnerValues = environment.GENDER_CODES;
            this.getSimpleSearchAdds($event, this.rightSpinner);
        }

    }
    getSimpleSearchAdds(leftSpinner, rightSpinner) {
        rightSpinner = encodeURIComponent(rightSpinner);
        this.simpleSearchService.getSimpleSearchAdds();
    }

    makeLink() {
        var copyFrom = document.createElement("textarea");
        //  copyFrom.textContent = $scope.link;
        var body = document.getElementsByTagName('body')[0];
        body.appendChild(copyFrom);
        copyFrom.select();
        document.execCommand('copy');
        body.removeChild(copyFrom);
        //  Materialize.toast('Copied to clipboard', 4000);
    }
}