import { Component } from "@angular/core";
import { SharedDataService } from "../../../shared/data/shared.data.service";
import { environment } from "../../../../environments/environment";
import { AccommodationSearchModel } from "../../shared/models/accommodation.filter.model";
import { SimpleSearchFilterService } from "./simple.search.filters.service";
import { AccommodationDropdown } from "../../shared/models/accommodation.dropdown.model";
import { MatSnackBar } from "@angular/material";
import { Apartment } from "app/accommodation/shared/models/apartment.names.model";
import { University } from "app/universities/universities.model";


@Component({
    selector: 'simple-search-filters',
    templateUrl: 'simple.search.filters.html'
})

export class SimpleSearchAddsFilters {

    leftSpinnerValues: AccommodationDropdown[] = []
    rightSpinnerValues: AccommodationDropdown[] = []
    leftSpinnerSelectedItem: AccommodationDropdown;
    rightSpinnerSelectedItem: AccommodationDropdown;
    loading: boolean;

    ngOnInit() {
        this.initializeSpinners();
        this.emitSpinnerClick(false);

    }

    constructor(private sharedDataService: SharedDataService,
        private simpleSearchFilterService: SimpleSearchFilterService,
        private snackBar: MatSnackBar) {
    }

    initializeSpinners() {
        this.leftSpinnerValues = environment.leftSpinnerValues;
        this.rightSpinnerValues = environment.apartmentTypes;
        this.leftSpinnerSelectedItem = Object.assign([], this.leftSpinnerValues[0]);
        this.rightSpinnerSelectedItem = Object.assign([], this.rightSpinnerValues[0]);
    }

    leftSpinnerClick(clickedItem) {
        if (clickedItem == environment.APARTMENT_TYPE) {
            this.rightSpinnerValues = environment.apartmentTypes;
            this.rightSpinnerSelectedItem = Object.assign([], environment.apartmentTypes[0]);
            this.emitSpinnerClick(false);

        }
        else if (clickedItem == environment.APARTMENT_NAME) {
            this.emitSpinnerClick(true);
        }
        else if (clickedItem == environment.GENDER) {

            this.rightSpinnerValues = environment.GENDER_CODES;
            this.rightSpinnerSelectedItem = Object.assign([], environment.GENDER_CODES[0]);
            this.emitSpinnerClick(false);
        }
    }

    rightSpinnerClick(clickedItem: string) {
        this.emitSpinnerClick(false);
    }

    emitSpinnerClick(apartmentName: boolean) {

        this.loading = true;
        let filterData: AccommodationSearchModel = new AccommodationSearchModel();
        filterData.leftSpinner = this.leftSpinnerSelectedItem.code;
        filterData.rightSpinner = this.rightSpinnerSelectedItem.code;

        let universities: University[] = this.sharedDataService.getUserSelectedUniversitiesList();
        let universityIds: number[] = new Array<number>();
        if (universities != null) {

            universityIds = universities.map(university => university.universityId);
            filterData.universityIds = universityIds;
            if (apartmentName) {
                this.simpleSearchFilterService.getApartmentNames(filterData).
                    map(apartmentNames => {
                        this.mapApartmentNames(apartmentNames);
                        filterData.rightSpinner = this.rightSpinnerSelectedItem.code;
                    }).
                    subscribe(data => this.sharedDataService.emitAccommomdationSearchFilters(filterData));
            }
            else {
                this.sharedDataService.emitAccommomdationSearchFilters(filterData);
            }
        }
        else {
            this.sharedDataService.openSnackBar(this.snackBar, "No Universities selected", "Dismiss");
        }
    }

    mapApartmentNames(apartments: Apartment[]) {
        this.rightSpinnerValues = new Array();
        this.rightSpinnerValues.length = 0;
        for (let apartment of apartments) {
            this.rightSpinnerValues.push({
                'code': apartment.apartmentName,
                'description': apartment.apartmentName
            });
        }
        this.rightSpinnerSelectedItem = Object.assign([], this.rightSpinnerValues[0]);
    }

    getSpinners(): AccommodationSearchModel {
        let filterData: AccommodationSearchModel = new AccommodationSearchModel();
        filterData.leftSpinner = this.leftSpinnerSelectedItem.code;
        filterData.rightSpinner = this.rightSpinnerSelectedItem.code;
        return filterData;
    }

    stopLoding() {
        this.loading = false;
    }

}