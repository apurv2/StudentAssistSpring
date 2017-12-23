import { Component } from "@angular/core";
import { AccommodationDropdown } from "../../shared/models/accommodation.dropdown.model";
import { environment } from "../../../../environments/environment";
import { SharedDataService } from "../../../shared/data/shared.data.service";
import { ApartmentName } from "../../shared/models/apartment.names.model";
import { University } from "../../shared/models/universities.list.model";
import { SimpleSearchFilterService } from "../../simpleSearch/filters/simple.search.filters.service";
import { AccommodationSearchModel } from "../../shared/models/accommodation.filter.model";

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

    mapApartmentNames(apartments: ApartmentName[]) {
        for (let apartment of apartments) {
            this.aptNameSpinnerValues.push({
                'code': apartment.apartmentName,
                'description': apartment.apartmentName
            });
        }
        this.aptNameSpinnerSelectedItem = Object.assign([], this.aptNameSpinnerValues[0]);
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

    spinnerClick(event) {

    }
}