import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs/Subject';
import { AccommodationDropdown } from '../shared/models/accommodation.dropdown.model';
import { University } from '../shared/models/universities.list.model';
import { ApartmentName } from '../shared/models/apartment.names.model';
import { SharedDataService } from '../../shared/data/shared.data.service';
import { SimpleSearchFilterService } from '../simpleSearch/filters/simple.search.filters.service';
import { environment } from '../../../environments/environment';
import { AccommodationSearchModel } from '../shared/models/accommodation.filter.model';
import { FormControl, FormGroupDirective, NgForm, Validators } from '@angular/forms';
import { ErrorStateMatcher, MatDialog } from '@angular/material';
import { LoginModal } from 'app/shared/modals/login.modal';


/** Error when invalid control is dirty, touched, or submitted. */
export class MyErrorStateMatcher implements ErrorStateMatcher {
    isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
        const isSubmitted = form && form.submitted;
        return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
    }
}

@Component({
    selector: 'accommodation-post',
    templateUrl: 'accommodation.post.html'
})
export class PostAccommodation {

    aptTypeSpinnerValues: AccommodationDropdown[] = []
    aptNameSpinnerValues: AccommodationDropdown[] = []
    universityNameSpinnerValues: AccommodationDropdown[] = []
    genderSpinnerValues: AccommodationDropdown[] = []
    noOfRoomsSpinnerValues: AccommodationDropdown[] = []
    vacanciesSpinnerValues: AccommodationDropdown[] = []

    noOfRoomsSpinnerSelectedItem: AccommodationDropdown;
    aptTypeSpinnerSelectedItem: AccommodationDropdown;
    vacanciesSpinnerSelectedItem: AccommodationDropdown;
    aptNameSpinnerSelectedItem: AccommodationDropdown;
    universityNameSpinnerSelectedItem: AccommodationDropdown;
    genderSpinnerSelectedItem: AccommodationDropdown;
    selectedUniversities: University[];
    allApartments: ApartmentName[];
    matcher = new MyErrorStateMatcher();


    emailFormControl = new FormControl('', [
        Validators.required,
        Validators.email,
    ]);

    constructor(private sharedDataService: SharedDataService,
        private simpleSearchFilterService: SimpleSearchFilterService,
        private dialog: MatDialog) { }

    ngOnInit() {

        this.initializeSpinners();
    }

    initializeSpinners() {

        this.aptNameSpinnerSelectedItem = new AccommodationDropdown();

        this.aptTypeSpinnerValues = environment.apartmentTypes;
        this.genderSpinnerValues = environment.GENDER_CODES;
        this.noOfRoomsSpinnerValues = environment.noOfRooms;
        this.vacanciesSpinnerValues = environment.vacancies;

        this.noOfRoomsSpinnerSelectedItem = Object.assign([], this.noOfRoomsSpinnerValues[0]);
        this.vacanciesSpinnerSelectedItem = Object.assign([], this.vacanciesSpinnerValues[0]);
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

    spinnerClick1(clickedItem) {
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

    postAccommodation() {
        console.log("clicked here");
        this.openDialog();
    }

    openDialog(): void {
        let dialogRef = this.dialog.open(LoginModal);

        dialogRef.afterClosed().subscribe(result => {
            console.log('The dialog was closed');
        });
    }

}
