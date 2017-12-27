import { Component, Inject } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material';


@Component({
    selector: 'dialog-overview-example-dialog',
    templateUrl: 'subscribe.notifications.modal.html',
})
export class SubscribeNotificationsModal {

    constructor(
        public dialogRef: MatDialogRef<SubscribeNotificationsModal>) { }

    onNoClick(): void {
        this.dialogRef.close();
    }
}