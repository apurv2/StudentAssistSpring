import { Component } from "@angular/core";
import { SharedDataService } from "../../../shared/data/shared.data.service";
import { environment } from "../../../../environments/environment";
import { AccommodationSearchModel } from "../../shared/models/accommodation.filter.model";
import { University } from "../../shared/models/universities.list.model";
import { SimpleSearchFilterService } from "./simple.search.filters.service";
import { ApartmentName } from "../../shared/models/apartment.names.model";
import { AccommodationDropdown } from "../../shared/models/accommodation.dropdown.model";
import { MatSnackBar } from "@angular/material";


@Component({
    selector: 'simple-search-filters',
    templateUrl: 'simple.search.filters.html'
})

export class SimpleSearchAddsFilters {

    leftSpinnerValues: AccommodationDropdown[] = []
    rightSpinnerValues: AccommodationDropdown[] = []
    leftSpinnerSelectedItem: AccommodationDropdown;
    rightSpinnerSelectedItem: AccommodationDropdown;

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
            this.rightSpinnerSelectedItem = environment.apartmentTypes[0];
            this.emitSpinnerClick(false);

        }
        else if (clickedItem == environment.APARTMENT_NAME) {
            this.emitSpinnerClick(true);
        }
        else if (clickedItem == environment.GENDER) {

            this.rightSpinnerValues = environment.GENDER_CODES;
            this.rightSpinnerSelectedItem = environment.GENDER_CODES[0];
            this.emitSpinnerClick(false);
        }
    }

    rightSpinnerClick(clickedItem: string) {
        this.emitSpinnerClick(false);
    }

    emitSpinnerClick(apartmentName: boolean) {

        let filterData: AccommodationSearchModel = new AccommodationSearchModel();
        filterData.leftSpinner = this.leftSpinnerSelectedItem.code;
        filterData.rightSpinner = this.rightSpinnerSelectedItem.code;

        let universities: University[] = this.sharedDataService.getUserSelectedUniversitiesList();
        let universityIds: number[] = new Array<number>();
        if (universities != null) {
            for (let university of universities) {
                universityIds.push(university.universityId);
            }

            filterData.universityIds = universityIds;
            if (apartmentName) {
                this.simpleSearchFilterService.getApartmentNames(filterData).
                    map(apartmentNames => this.mapApartmentNames(apartmentNames),
                    filterData.rightSpinner = this.rightSpinnerSelectedItem.code).
                    map(data => this.sharedDataService.emitAccommomdationSearchFilters(filterData));
            }
            else {
                this.sharedDataService.emitAccommomdationSearchFilters(filterData);
            }
        }
        else {
            this.sharedDataService.openSnackBar(this.snackBar, "No Universities selected", "Dismiss");

        }
    }

    mapApartmentNames(apartments: ApartmentName[]) {

        for (let apartment of apartments) {
            this.rightSpinnerValues.push({
                'code': apartment.apartmentName,
                'description': apartment.apartmentName
            });
        }

        this.rightSpinnerSelectedItem = this.rightSpinnerValues[0];
    }

}