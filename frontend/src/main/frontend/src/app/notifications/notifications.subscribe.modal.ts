import { Component, Inject } from '@angular/core';
import { MatDialogRef } from '@angular/material';
import { NotificationSettingsService } from 'app/notifications/notifications.subscribe.modal.service';
import { NotificationSettings } from 'app/notifications/models/notification.settings.model';
import { SharedDataService } from 'app/shared/data/shared.data.service';
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
    selectedUniversityDetails: University;
    selectedUniversityId: number;
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
    }

    getNotificationSettings() {
        this.isLoading = true;
        this.notificationService.getNotificationSettings().
            subscribe(notificationSettings => {
                this.notificationSettings = notificationSettings;
                this.selectedUniversityId = this.notificationSettings.universityId;
                this.showUnivs = notificationSettings.universityId == -1;

                this.setDBselectedUniv();
                setTimeout(() => {
                    this.prePopulateNotificationSettings();
                    this.isLoading = false;
                }, 500);
            });

    }


    setDBselectedUniv() {
        this.selectedUniversityDetails = this
            .notificationSettings
            .allUnivDetails
            .find(item => item.universityId == this.selectedUniversityId);
    }

    prePopulateNotificationSettings() {

        if (this.selectedUniversityDetails) {

            this
                .selectedUniversityDetails
                .apartments
                .filter(apartment => this.notificationSettings.apartmentName.indexOf(apartment.apartmentName) != -1)
                .forEach(apartment => apartment.selected = true);

            for (let aptType of this.notificationSettings.apartmentType) {
                this.aptTypeVisibility[aptType] = true;
                this.aptTypeCheckboxes[aptType] = this.notificationSettings.apartmentType.
                    indexOf(aptType) != -1;
            }
            this.selectedGender = this.notificationSettings.gender;
        }
    }

    subscribe() {

        let settings: NotificationSettings = new NotificationSettings();
        let aptNames: string[] = [];
        let aptTypes: string[] = [];

        aptNames = this.selectedUniversityDetails
            .apartments
            .filter(apartment => apartment.selected)
            .map(apartment => apartment.apartmentName);

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
        this.setDBselectedUniv();
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
        this.aptTypeVisibility = {
            on: false,
            off: false,
            dorms: false
        };
    }

    backPromptNo() {
        this.backPrompt = false;
    }

    close() {
        this.dialogRef.close();
    }

    checkUnCheckBoxes(aptType: string) {

        if (aptType === environment.all) {
            this.selectedUniversityDetails
                .apartments
                .forEach(apartment => apartment.selected = false);

        }
        else {
            this.aptTypeVisibility[aptType] = !this.aptTypeVisibility[aptType];
            this.selectedUniversityDetails
                .apartments
                .filter(apartment => apartment.apartmentType == aptType)
                .forEach(apartment => apartment.selected = false);
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





