import { Component, ErrorHandler } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
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
import { University } from 'app/universities/universities.model';
import { UserService } from 'app/shared/userServices/user.service';
import { NewApartmentModal } from './newApartment/new.apartment.modal';
import { UserInfo } from '../../shared/models/user.info.model';
import { Universities } from '../../universities/universities.list';
import { AddDetailsService } from '../shared/adDetails/accommodation.details.add.service';


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
export class PostAccommodation implements ErrorHandler {


    deleteClicked: boolean = false;
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
    photos: any[] = [];
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
    saveOrUpdate: string = environment.savePost;
    isUpdate: boolean = false;
    editAccommodationAdd: AccommodationAdd;

    constructor(private sharedDataService: SharedDataService,
        private simpleSearchFilterService: SimpleSearchFilterService,
        private dialog: MatDialog,
        private userService: UserService,
        private postAccommodationService: PostAccommodationService,
        private snackBar: MatSnackBar,
        private router: Router,
        private route: ActivatedRoute,
        private addDetailService: AddDetailsService) { }

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
        this.universityNameSpinnerSelectedItem = new AccommodationDropdown();

        this.aptTypeSpinnerValues = environment.apartmentTypes;
        this.genderSpinnerValues = environment.GENDER_CODES;
        this.noOfRoomsSpinnerValues = environment.noOfRooms;
        this.vacanciesSpinnerValues = environment.vacancies;

        this.noOfRoomsSpinnerSelectedItem = Object.assign([], this.noOfRoomsSpinnerValues[0]);
        this.vacanciesSpinnerSelectedItem = Object.assign([], this.vacanciesSpinnerValues[0]);
        this.aptTypeSpinnerSelectedItem = Object.assign([], this.aptTypeSpinnerValues[0]);
        this.genderSpinnerSelectedItem = Object.assign([], this.genderSpinnerValues[0]);

        let editAddId: boolean = false;
        let editUnivId: number = 0;
        this.route.params.subscribe(
            params => {
                editAddId = (params[environment.edtAddId]);
                editUnivId = (params[environment.editUnivId]);
            });

        if (editAddId && editUnivId > 0) {
            this.saveOrUpdate = environment.updatePost;
            Observable.forkJoin(this.addDetailService.getAddDetailsFromAddId(editAddId),
                this.getAllUniversities())
                .subscribe(response => {
                    this.isUpdate = true;
                    let accommodationAdd: AccommodationAdd = response[0];
                    let universities: University[] = response[1];

                    let univIndex: number = universities.findIndex(univ => editUnivId == univ.universityId);
                    this.populateEditAdd(accommodationAdd, universities);
                    this.initializeUniversityNames(universities, univIndex);

                });
        }
        else {

            this.getAllUniversities()
                .flatMap(universities => {
                    this.initializeUniversityNames(universities, 0);
                    return this.initializtApartmentNames(universities)
                }).subscribe();
        }
    }

    initializeUniversityNames(universities: University[], position: number) {
        this.selectedUniversities = universities;
        this.mapUniversityNames(universities, position);
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

    mapUniversityNames(selectedUniversities: University[], index: number) {
        for (let university of selectedUniversities) {
            this.universityNameSpinnerValues.push({
                'code': university.universityId + '',
                'description': university.universityName
            });
        }
        this.universityNameSpinnerSelectedItem = Object.assign([], this.universityNameSpinnerValues[index]);
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
            .do(e => this.loading = false)
            .flatMap(e => this.sharedDataService.openSuccessFailureDialog(e, this.dialog, "Congratulations your listing has been successfully posted"))
            .subscribe(e => this.router.navigate(['/dashboard/']),
                err => {
                    this.loading = false;
                    console.log(err);
                    this.sharedDataService.openSuccessFailureDialog("failure", this.dialog).subscribe();;
                });
    }

    postAccommodation(userInfo?: UserInfo) {

        if (this.isUpdate) {
            if (this.deleteClicked && this.photos.length > 0) {
                return this.uploadImages()
            }
            else
                return this.deleteClicked ? this.postAccommodationAdd(null, userInfo) :
                    this.postAccommodationAdd(this.editAccommodationAdd.addPhotoIds, userInfo);
        }
        else
            return this.photos.length > 0 ? this.uploadImages() : this.postAccommodationAdd(null, userInfo);
    }

    openLoginDialog() {
        let dialogRef = this.dialog.open(LoginModal);
        return dialogRef.afterClosed();
    }

    addFile(files: any) {
        let allowedExtensions = ["jpg", "png", "jpeg"];
        if ((this.photos != null && this.photos.length == 3) || (files.files != null && files.files.length > 3)) {
            this.sharedDataService.openSnackBar(this.snackBar, "We can only upload upto 3 photos for you :)", "");
            return;
        }
        for (let photoFile of files.files) {

            let fileExtension: string = photoFile.name.split('.').pop();
            if (!this.isInArray(allowedExtensions, fileExtension)) {
                this.sharedDataService.openSnackBar(this.snackBar, "only  jpg, jpeg and png file extension are allowed", "");
                continue;

            }
            let reader = new FileReader();
            reader.onload = (e) => this.photos.push(e.target['result']);
            reader.readAsDataURL(photoFile);
        }
    }

    isInArray(array, word) {
        return array.indexOf(word.toLowerCase()) > -1;
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

    deleteAllPhotos() {
        this.deleteClicked = true;
        this.photos.length = 0;
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
        if (this.editAccommodationAdd) {
            url = environment.editAccommodationAdd;
            accommodationAdd.addId = this.editAccommodationAdd.addId;
        }
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
            .filter(apartmentId => parseInt(apartmentId) > 0)
            .switchMap(apartmentId => this.initializtApartmentNames(this.sharedDataService.getUserSelectedUniversitiesList(), +apartmentId))
            .subscribe(() => {
                this.showAddApartment = false
                this.apartmentTooltipText = environment.apartmentAlreadyAdded;
                this.sharedDataService.openSnackBar(this.snackBar, environment.apartmentSuccess, "Dismiss");
            },
                err => {
                    this.loading = false;
                    this.sharedDataService.openSuccessFailureDialog("failure", this.dialog).subscribe();
                });

    }

    checkAdminUser() {
        this.userService.getLoginStatus()
            .filter(resp => resp)
            .flatMap(e => this.getUserDetails())
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

    populateEditAdd(add: AccommodationAdd, universities: University[]) {

        this.initializtApartmentNames(universities).subscribe(e => {
            this.aptTypeSpinnerSelectedItem = Object.assign([], this.aptTypeSpinnerValues.find(apt => add.apartmentType == apt.description));
            this.populateApartmentNameSpinner(add.apartmentId);
            this.genderSpinnerSelectedItem = Object.assign([], this.genderSpinnerValues.find(apt => add.gender == apt.description));
            this.noOfRoomsSpinnerSelectedItem = Object.assign([], this.noOfRoomsSpinnerValues.find(apt => add.noOfRooms == apt.description.replace(/ /g, '')));
            this.vacanciesSpinnerSelectedItem = Object.assign([], this.vacanciesSpinnerValues.find(apt => add.vacancies == +apt.description));
            this.cost = add.cost;
            this.notes = add.notes;
            add.addPhotoIds.forEach(photo => this.photos.push(photo));
            let postedDate: Date = new Date(add.postedTill);
            this.dateAvailableTill = new FormControl(postedDate.toISOString());
            this.editAccommodationAdd = add;
        });
    }

    handleError(error) {
        // exception occured in some service class method.
        console.log('Error in MyErrorhandler - %s', error);
    }

    getAllUniversities() {
        return this.postAccommodationService.getUniversityDetails(environment.allUniversities)
    }
}
