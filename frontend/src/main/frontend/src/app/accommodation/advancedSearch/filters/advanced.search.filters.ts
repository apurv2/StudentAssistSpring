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

        this.initializtApartmentNames();
        this.initializeUniversityNames();
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
            this.simpleSearchFilterService.getApartmentNames(filterData).
                subscribe(apartmentNames => this.mapApartmentNames(apartmentNames));
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

        let filterData: AccommodationSearchModel = new AccommodationSearchModel();

        filterData.apartmentName = this.aptNameSpinnerSelectedItem.code;
        filterData.apartmentType = this.aptTypeSpinnerSelectedItem.code;
        filterData.gender = this.genderSpinnerSelectedItem.code;
        filterData.selectedUniversityId = +this.universityNameSpinnerSelectedItem.code;

        this.sharedDataService.emitAccommomdationSearchFilters(filterData);

    }

    spinnerClick(clickedItem) {
        this.populateApartmentNameSpinner();
    }

    populateApartmentNameSpinner() {

        let universityId = this.universityNameSpinnerSelectedItem.code;
        let apartmentType = this.aptTypeSpinnerSelectedItem.code;
        this.aptNameSpinnerValues = new Array();
        for (let apartment of this.allApartments) {

            if (apartment.uinversityId == +universityId &&
                apartment.apartmentType == apartmentType) {

                this.aptNameSpinnerValues.push({
                    'code': apartment.apartmentName,
                    'description': apartment.apartmentName
                });
                this.aptNameSpinnerSelectedItem = Object.assign([], this.aptNameSpinnerValues[0]);
            }
        }
    }

    spinnerClick1(clickedItem) { }
}