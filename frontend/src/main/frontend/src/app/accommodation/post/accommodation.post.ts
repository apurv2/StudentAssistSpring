import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs/Subject';
import { AccommodationDropdown } from '../shared/models/accommodation.dropdown.model';
import { Apartment } from '../shared/models/apartment.names.model';
import { SharedDataService } from '../../shared/data/shared.data.service';
import { SimpleSearchFilterService } from '../simpleSearch/filters/simple.search.filters.service';
import { environment } from '../../../environments/environment';
import { AccommodationSearchModel } from '../shared/models/accommodation.filter.model';
import { FormControl, FormGroupDirective, NgForm, Validators } from '@angular/forms';
import { ErrorStateMatcher, MatDialog } from '@angular/material';
import { LoginModal } from 'app/shared/modals/login.modal';
import { CloudinaryUploader } from 'ng2-cloudinary/dist/esm/src/cloudinary-uploader.service';
import { CloudinaryOptions } from 'ng2-cloudinary/dist/esm/src/cloudinary-options.class';
import { PostAccommodationService } from 'app/accommodation/post/accommodation.post.service';
import { Observable } from 'rxjs/Observable';
import { AccommodationAdd } from 'app/accommodation/shared/models/accommodation.model';
import { SuccessOrFailureModal } from 'app/shared/modals/success.or.failure';
import { University } from 'app/universities/universities.model';
import { UserService } from 'app/shared/userServices/user.service';


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
    allApartments: Apartment[];
    matcher = new MyErrorStateMatcher();
    photos: File[] = [];
    cost: number;
    notes: string;
    email: string;

    emailFormControl = new FormControl('', [
        Validators.required,
        Validators.email,
    ]);

    validFormControl = new FormControl('', [
        Validators.required
    ]);

    numberFormControl = new FormControl('', [
        Validators.required,
        Validators.min(0),
        Validators.max(2000),
    ]);


    cloudinaryUrls: string[] = [];

    constructor(private sharedDataService: SharedDataService,
        private simpleSearchFilterService: SimpleSearchFilterService,
        private dialog: MatDialog,
        private userService: UserService,
        private postAccommodationService: PostAccommodationService) { }

    ngOnInit() {

        this.initializeSpinners();

        this.numberFormControl.hasError
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
        if (this.allApartments != null) {
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
    }

    submit() {
        this.userService.getLoginStatus()
            .flatMap(status => status ? this.postAccommodation() : this.openLoginDialog())
            .filter(response => response.response)
            .subscribe(e => this.handlePostAccommodationResponse(e));
    }

    postAccommodation() {
        return this.photos.length > 0 ? this.uploadImages() : this.postAccommodationAdd(null);
    }

    openLoginDialog() {
        let dialogRef = this.dialog.open(LoginModal);
        return dialogRef.afterClosed();
    }

    addFile(files: any) {
        for (let photoFile of files.files) {
            let reader = new FileReader();
            reader.onload = (e) => this.photos.push(e.target['result']);
            reader.readAsDataURL(photoFile);
        }
    }

    uploadImages() {
        let obs: Observable<any>[] = [];
        for (let photo of this.photos) {
            const params = this.createUploadParams(photo);
            obs.push(this.postAccommodationService.postImages(environment.cloudinaryURL, params));
        }

        return Observable.merge(obs)
            .flatMap(obs => obs)
            .map(cloudinaryResponse => this.cloudinaryUrls.push(cloudinaryResponse.url))
            .filter(e => this.cloudinaryUrls.length == this.photos.length)
            .switchMap(e => this.postAccommodationAdd(this.cloudinaryUrls))

    }

    private createUploadParams(photo) {
        let formData: FormData = new FormData();
        formData.append(environment.upload_preset, environment.CLOUDINARY_PRESET_VALUE);
        formData.append(environment.file, photo);
        return formData;
    }

    postAccommodationAdd(photoUrls: string[]): Observable<any> {
        let accommodationAdd: AccommodationAdd = this.preparePostAccommodationParams();
        if (photoUrls != null) { accommodationAdd.addPhotoIds = photoUrls }
        return this.postAccommodationService
            .postAccommodation(environment.createAccommodationAdd, accommodationAdd);
    }

    private preparePostAccommodationParams(): AccommodationAdd {

        let accommodationAdd: AccommodationAdd = new AccommodationAdd();
        accommodationAdd.apartmentName = this.aptNameSpinnerSelectedItem.code;
        accommodationAdd.gender = this.genderSpinnerSelectedItem.code;
        accommodationAdd.vacancies = +this.vacanciesSpinnerSelectedItem.code;
        accommodationAdd.cost = this.cost;
        accommodationAdd.notes = this.notes;
        accommodationAdd.universityId = +this.universityNameSpinnerSelectedItem.code;
        accommodationAdd.emailId = this.email;
        accommodationAdd.noOfRooms = this.noOfRoomsSpinnerSelectedItem.code;
        return accommodationAdd;
    }

    handlePostAccommodationResponse(response) {
        let data: any = {};
        data.message = response.response === environment.success ? "success" : "failure";
        data.response = response.response;

        this.dialog.open(SuccessOrFailureModal, {
            data: data
        }).
            afterClosed().subscribe(result => {
                console.log('The dialog was closed');
            });


    }


}
