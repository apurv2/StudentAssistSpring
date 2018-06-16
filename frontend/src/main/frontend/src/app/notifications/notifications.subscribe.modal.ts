import { Component, Inject } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material';
import { NotificationSettingsService } from 'app/notifications/notifications.subscribe.modal.service';
import { NotificationSettings } from 'app/notifications/models/notification.settings.model';
import { UniversityApartments } from 'app/accommodation/shared/models/university.apartments.model';
import { SharedDataService } from 'app/shared/data/shared.data.service';
import { CheckBoxModel } from 'app/shared/models/checkbox.model';
import { Apartment } from 'app/accommodation/shared/models/apartment.names.model';
import { checkAndUpdateBinding } from '@angular/core/src/view/util';
import { environment } from 'environments/environment';
import { University } from 'app/universities/universities.model';

@Component({
    selector: 'notification-settings',
    templateUrl: 'notifications.subscribe.modal.html',
})
export class SubscribeNotificationsModal {

    constructor(public dialogRef: MatDialogRef<SubscribeNotificationsModal>,
        private notificationService: NotificationSettingsService,
        private sharedDataService: SharedDataService) { }

    notificationSettings: NotificationSettings;
    selectedUniversityDetails: UniversityApartments;
    selectedUniversityId: number;
    allUniversities: University[] = [];
    aptNameCheckboxes: CheckBoxModel[] = [];
    aptTypeCheckboxes: {} = {};
    genderValues = [
        'Male',
        'Female',
    ];
    selectedGender: string;
    showUnivs: boolean;

    aptTypeVisibility = {
        on: false,
        off: false,
        dorms: false
    };
    backPrompt: boolean;
    isLoading: boolean;

    onNoClick(): void {
        this.dialogRef.close();
    }

    ngOnInit() {
        this.getNotificationSettings();

        let userUniversities: University[] = this.sharedDataService.
            getUserSelectedUniversitiesList();

    }

    getNotificationSettings() {
        this.isLoading = true;
        this.notificationService.getNotificationSettings().
            subscribe(notificationSettings => {
                this.notificationSettings = notificationSettings;
                this.selectedUniversityId = this.notificationSettings.universityId;

                this.setSelectedUnivAptNames();
                this.extractUniversitynames();

                if (notificationSettings.universityId === -1) {
                    this.showUnivs = true;
                }
                setTimeout(() => {
                    this.populateCheckBoxNgModel();
                    this.isLoading = false;
                }, 500);
            });

    }

    extractUniversitynames() {

        this.notificationSettings.apartmentNames.map(university => {
            return new University({
                universityName: university.universityName,
                universityId: university.universityId,
            });
        }).forEach(university => this.allUniversities.push(university));
    }

    setSelectedUnivAptNames() {

        this.selectedUniversityDetails = this
            .notificationSettings
            .apartmentNames
            .find(item => item.universityId == this.selectedUniversityId)

    }

    populateCheckBoxNgModel() {

        if (this.selectedUniversityDetails) {
            for (let aptName of this.selectedUniversityDetails.apartmentNames) {

                let value = this.notificationSettings.apartmentName.
                    indexOf(aptName.apartmentName) != -1 ? true : false;

                let checkbox: CheckBoxModel = new CheckBoxModel();
                checkbox.aptType = aptName.apartmentType;
                checkbox.value = value;
                this.aptNameCheckboxes[aptName.apartmentName] = checkbox;
            }

            for (let aptType of this.notificationSettings.apartmentType) {

                this.aptTypeVisibility[aptType] = true;
                let value = this.notificationSettings.apartmentType.
                    indexOf(aptType) != -1;
                this.aptTypeCheckboxes[aptType] = value;
            }
            this.selectedGender = this.notificationSettings.gender;
        }
    }

    subscribe() {

        let settings: NotificationSettings = new NotificationSettings();
        let aptNames: string[] = [];
        let aptTypes: string[] = [];

        for (let aptName in this.aptNameCheckboxes) {
            if (this.aptNameCheckboxes[aptName].value) {
                aptNames.push(aptName);
            }
        }
        for (let aptType in this.aptTypeCheckboxes) {
            if (this.aptTypeCheckboxes[aptType]) {
                aptTypes.push(aptType);
            }
        }

        settings.apartmentName = aptNames;
        settings.apartmentType = aptTypes;
        settings.gender = this.selectedGender;
        settings.universityId = this.selectedUniversityId;
        this.notificationService.
            subscribeForNotifications(settings).subscribe(status => this.dialogRef.close(status), err => this.dialogRef.close(err));
    }

    selectUniversity() {
        this.showUnivs = false;
        this.setSelectedUnivAptNames();
    }
    backClicked() {
        this.backPrompt = true;
    }

    backConfirm() {
        this.showUnivs = true;
        this.backPrompt = false;
        this.checkUnCheckBoxes(environment.all);
        this.aptTypeCheckboxes = {};
        this.selectedGender = null;
    }

    backPromptNo() {
        this.backPrompt = false;
    }

    close() {
        this.dialogRef.close();
    }

    checkUnCheckBoxes(aptType: string) {

        if (aptType === environment.all) {
            for (let aptName in this.aptNameCheckboxes) {
                this.aptNameCheckboxes[aptName].value = false;
            }
        }
        else {
            this.aptTypeVisibility[aptType] = !this.aptTypeVisibility[aptType];
            for (let aptName in this.aptNameCheckboxes) {
                if (this.aptNameCheckboxes[aptName].aptType === aptType) {
                    this.aptNameCheckboxes[aptName].value = false;
                }
            }
        }

    }

    isEmptyObject(obj) {
        let aptTypes: string[] = [];
        for (let aptType in this.aptTypeCheckboxes) {
            if (this.aptTypeCheckboxes[aptType]) {
                aptTypes.push(aptType);
            }
        }
        return aptTypes.length == 0;
    }
}





