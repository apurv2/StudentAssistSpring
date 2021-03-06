import { Component, Inject } from '@angular/core';
import { MatDialog, MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';

@Component({
    selector: 'success-failure',
    templateUrl: 'success.or.failure.html',
})
export class SuccessOrFailureModal {

    constructor(@Inject(MAT_DIALOG_DATA) public data: any,
    public dialogRef: MatDialogRef<SuccessOrFailureModal>) { }

    closeDialog(success: boolean): void {
        this.dialogRef.close(success);
    }
}