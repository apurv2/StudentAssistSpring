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
import { ErrorStateMatcher, MatDialog, MatSnackBar } from '@angular/material';
import { LoginModal } from 'app/shared/modals/login.modal';
import { CloudinaryUploader } from 'ng2-cloudinary/dist/esm/src/cloudinary-uploader.service';
import { CloudinaryOptions } from 'ng2-cloudinary/dist/esm/src/cloudinary-options.class';
import { PostAccommodationService } from 'app/accommodation/post/accommodation.post.service';
import { Observable } from 'rxjs/Observable';
import { AccommodationAdd } from 'app/accommodation/shared/models/accommodation.model';
import { SuccessOrFailureModal } from 'app/shared/modals/success.or.failure';
import { University } from 'app/universities/universities.model';
import { UserService } from 'app/shared/userServices/user.service';
import { NewApartmentModal } from './newApartment/new.apartment.modal';
import { UserInfo } from '../../shared/models/user.info.model';


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
    toolTipPosition: string = "right";
    loading: boolean;
    adminUser: boolean;
    name: string;

    minDate: Date = new Date();
    maxDate: Date = new Date();

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
    dateAvailableTill: FormControl;
    cloudinaryUrls: string[] = [];
    showAddApartment: boolean = true;
    apartmentTooltipText: string;
    fbId: string;

    constructor(private sharedDataService: SharedDataService,
        private simpleSearchFilterService: SimpleSearchFilterService,
        private dialog: MatDialog,
        private userService: UserService,
        private postAccommodationService: PostAccommodationService,
        private snackBar: MatSnackBar,
        private router: Router) { }

    ngOnInit() {

        this.initializeSpinners();
        this.maxDate.setMonth(new Date().getMonth() + 1);
        this.apartmentTooltipText = environment.apartmentTooltipText;

        let future30DaysDate: Date = new Date();
        future30DaysDate.setMonth(new Date().getMonth() + 1);
        this.dateAvailableTill = new FormControl(future30DaysDate.toISOString());

        this.checkAdminUser();

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

        let universities: University[] = this.sharedDataService.getUserSelectedUniversitiesList();
        if (universities) {
            this.initializtApartmentNames(universities).subscribe();
        }
        this.initializeUniversityNames();
    }

    initializeUniversityNames() {

        this.selectedUniversities = this.sharedDataService.getUserSelectedUniversitiesList() != null ?
            this.sharedDataService.getUserSelectedUniversitiesList() : new Array<University>();
        this.mapUniversityNames(this.selectedUniversities);
    }
    initializtApartmentNames(universities: University[], apartmentId?: number) {
        let filterData: AccommodationSearchModel = new AccommodationSearchModel();
        filterData.universityIds = universities.map(university => university.universityId);
        return this.simpleSearchFilterService.getApartmentNames(filterData)
            .map(apartmentNames => this.mapApartmentNames(apartmentNames, apartmentId))

    }
    mapApartmentNames(apartments: Apartment[], apartmentId?: number) {
        this.allApartments = apartments;
        this.populateApartmentNameSpinner(apartmentId);
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

    populateApartmentNameSpinner(apartmentId?: number) {

        let universityId = this.universityNameSpinnerSelectedItem.code;
        let apartmentType = this.aptTypeSpinnerSelectedItem.code;
        this.aptNameSpinnerValues = new Array();
        if (this.allApartments != null) {
            for (let apartment of this.allApartments) {

                if (apartment.universityId == +universityId &&
                    apartment.apartmentType == apartmentType) {

                    this.aptNameSpinnerValues.push({
                        'code': apartment.apartmentId + "",
                        'description': apartment.apartmentName
                    });
                    this.aptNameSpinnerSelectedItem = apartmentId > 0 ? Object.assign([], this.aptNameSpinnerValues.filter(apt => +apt.code == apartmentId)[0]) :
                        this.aptNameSpinnerSelectedItem = Object.assign([], this.aptNameSpinnerValues[0]);
                }
            }
        }
    }

    submit() {
        this.loading = true;
        this.userService.getLoginStatus()
            .flatMap(status => status ? this.getUserDetails(this.fbId) : this.openLoginDialog())
            .map(resp => {
                if (resp == null) {
                    throw new Error('Invalid user id');
                }
                else return resp;
            })
            .flatMap((userInfo: UserInfo) => this.postAccommodation(userInfo))
            .subscribe(e => {
                this.sharedDataService.openSuccessFailureDialog(e, this.dialog);
                this.loading = false;
            }, err => {
                this.loading = false;
                console.log(err);
                this.sharedDataService.openSuccessFailureDialog("failure", this.dialog);
            });
    }

    postAccommodation(userInfo?: UserInfo) {
        return this.photos.length > 0 ? this.uploadImages() : this.postAccommodationAdd(null, userInfo);
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

    postAccommodationAdd(photoUrls?: string[], userInfo?: UserInfo): Observable<any> {
        let accommodationAdd: AccommodationAdd = this.preparePostAccommodationParams(userInfo);
        if (photoUrls != null) { accommodationAdd.addPhotoIds = photoUrls }

        let url = this.adminUser ? environment.createAccommodationAddFromFacebook : environment.createAccommodationAdd;
        return this.postAccommodationService
            .postAccommodation(url, accommodationAdd);
    }

    private preparePostAccommodationParams(userInfo?: UserInfo): AccommodationAdd {

        let accommodationAdd: AccommodationAdd = new AccommodationAdd();
        accommodationAdd.gender = this.genderSpinnerSelectedItem.code;
        accommodationAdd.vacancies = +this.vacanciesSpinnerSelectedItem.code;
        accommodationAdd.cost = this.cost;
        accommodationAdd.notes = this.notes;
        accommodationAdd.universityId = +this.universityNameSpinnerSelectedItem.code;
        accommodationAdd.emailId = this.email;
        accommodationAdd.noOfRooms = this.noOfRoomsSpinnerSelectedItem.code;
        accommodationAdd.postedTill = this.dateAvailableTill.value;

        if (userInfo != null) {
            accommodationAdd.firstName = userInfo.firstName;
            accommodationAdd.lastName = userInfo.lastName;
            accommodationAdd.userId = userInfo.userId;
        }

        accommodationAdd.apartmentId = +this.aptNameSpinnerSelectedItem.code;
        return accommodationAdd;
    }

    handlePostAccommodationResponse(response) {

    }

    apartmentNameClick() {

        let apartmentInfo: Apartment = new Apartment();
        apartmentInfo.universityId = +this.universityNameSpinnerSelectedItem.code;
        apartmentInfo.universityName = this.universityNameSpinnerSelectedItem.description;
        apartmentInfo.apartmentTypeDescription = this.aptTypeSpinnerSelectedItem.description;
        apartmentInfo.apartmentType = this.aptTypeSpinnerSelectedItem.code;

        this.dialog.open(NewApartmentModal, { data: apartmentInfo })
            .afterClosed()
            .filter((apartmentInfo: Apartment) => apartmentInfo != null)
            .flatMap((apartmentInfo: Apartment) => this.postAccommodationService.addApartment(environment.addNewApartment, apartmentInfo))
            .filter(response => parseInt(response) > 0)
            .switchMap(apartmentId => this.initializtApartmentNames(this.sharedDataService.getUserSelectedUniversitiesList(), +apartmentId))
            .subscribe(() => {
                this.showAddApartment = false
                this.apartmentTooltipText = environment.apartmentAlreadyAdded;
                this.sharedDataService.openSnackBar(this.snackBar, environment.apartmentSuccess, "Dismiss");
            },
                err => {
                    this.loading = false;
                    this.sharedDataService.openSuccessFailureDialog("failure", this.dialog);
                });

    }

    checkAdminUser() {

        this.getUserDetails()
            .flatMap(userInfo => this.userService.checkAdmin())
            .subscribe(data => this.adminUser = data, err => console.log("not looged in"));

    }

    getUserDetails(userId?: string) {
        return this.userService.getUserInfoFromFb(userId);
    }

    onFbIdChange() {

        if (+this.fbId > 0) {
            this.getUserDetails(this.fbId)
                .subscribe((userInfo: UserInfo) => {
                    this.name = userInfo.fullName;

                })

        }
    }
}
