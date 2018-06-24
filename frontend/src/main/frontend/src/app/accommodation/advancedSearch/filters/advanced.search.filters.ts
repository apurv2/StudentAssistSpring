import { Component } from "@angular/core";
import { AccommodationDropdown } from "../../shared/models/accommodation.dropdown.model";
import { environment } from "../../../../environments/environment";
import { SharedDataService } from "../../../shared/data/shared.data.service";
import { Apartment } from "../../shared/models/apartment.names.model";
import { SimpleSearchFilterService } from "../../simpleSearch/filters/simple.search.filters.service";
import { AccommodationSearchModel } from "../../shared/models/accommodation.filter.model";
import { University } from "app/universities/universities.model";

@Component({
    selector: 'advanced-search-filters',
    templateUrl: 'advanced.search.filters.html'
})

export class AdvancedSearchFilters {

    aptTypeSpinnerValues: AccommodationDropdown[] = []
    aptNameSpinnerValues: AccommodationDropdown[] = []
    universityNameSpinnerValues: AccommodationDropdown[] = []
    genderSpinnerValues: AccommodationDropdown[] = []

    aptTypeSpinnerSelectedItem: AccommodationDropdown;
    aptNameSpinnerSelectedItem: AccommodationDropdown;
    universityNameSpinnerSelectedItem: AccommodationDropdown;
    genderSpinnerSelectedItem: AccommodationDropdown;
    selectedUniversities: University[];
    allApartments: Apartment[];
    loading: boolean;

    constructor(private sharedDataService: SharedDataService,
        private simpleSearchFilterService: SimpleSearchFilterService) { }

    ngOnInit() {

        this.initializeSpinners();
    }

    initializeSpinners() {
        this.aptTypeSpinnerValues = environment.apartmentTypes;
        this.genderSpinnerValues = environment.GENDER_CODES;
        this.aptNameSpinnerSelectedItem = new AccommodationDropdown();

        this.aptTypeSpinnerSelectedItem = Object.assign([], this.aptTypeSpinnerValues[0]);
        this.genderSpinnerSelectedItem = Object.assign([], this.genderSpinnerValues[0]);

        this.initializeUniversityNames();
        this.initializtApartmentNames().subscribe(e => this.searchClicked());

    }

    initializeUniversityNames() {

        this.selectedUniversities = this.sharedDataService.getUserSelectedUniversitiesList() != null ?
            this.sharedDataService.getUserSelectedUniversitiesList() : new Array<University>();

        this.mapUniversityNames(this.selectedUniversities);
    }
    initializtApartmentNames() {

        let universities: University[] = this.sharedDataService.getUserSelectedUniversitiesList();
        let filterData: AccommodationSearchModel = new AccommodationSearchModel();
        let universityIds: number[] = new Array<number>();

        if (universities != null) {
            for (let university of universities) {
                universityIds.push(university.universityId);
            }

            filterData.universityIds = universityIds;
            return this.simpleSearchFilterService.getApartmentNames(filterData).
                map(allUnivDetails => this.mapApartmentNames(allUnivDetails));
        }
    }

    mapApartmentNames(apartments: Apartment[]) {
        this.allApartments = apartments;
        this.populateApartmentNameSpinner();
    }

    mapUniversityNames(selectedUniversities: University[]) {
        for (let university of selectedUniversities) {
            this.universityNameSpinnerValues.push({
                'code': university.universityId + '',
                'description': university.universityName
            });
        }
        this.universityNameSpinnerSelectedItem = Object.assign([], this.universityNameSpinnerValues[0]);
    }

    searchClicked() {
        this.sharedDataService.emitAccommomdationSearchFilters(this.getCurrentFilters());
    }
    getCurrentFilters(): AccommodationSearchModel {
        let filterData: AccommodationSearchModel = new AccommodationSearchModel();

        filterData.apartmentName = this.aptNameSpinnerSelectedItem.description;
        filterData.apartmentType = this.aptTypeSpinnerSelectedItem.code;
        filterData.gender = this.genderSpinnerSelectedItem.code;
        filterData.selectedUniversityId = +this.universityNameSpinnerSelectedItem.code;
        return filterData;
    }
    spinnerClick(clickedItem) {
        this.populateApartmentNameSpinner();
    }

    populateApartmentNameSpinner() {

        let universityId = this.universityNameSpinnerSelectedItem.code;
        let apartmentType = this.aptTypeSpinnerSelectedItem.code;
        this.aptNameSpinnerValues = new Array();
        for (let apartment of this.allApartments) {

            if (apartment.universityId == +universityId &&
                apartment.apartmentType == apartmentType) {

                this.aptNameSpinnerValues.push({
                    'code': apartment.apartmentId + "",
                    'description': apartment.apartmentName
                });
                this.aptNameSpinnerSelectedItem = Object.assign([], this.aptNameSpinnerValues[0]);
            }
        }
    }
}
