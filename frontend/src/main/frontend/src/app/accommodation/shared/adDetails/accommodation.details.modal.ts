import { Component} from "@angular/core";
import { MatDialogRef } from "@angular/material";
import { AccommodationAdd } from "app/accommodation/shared/models/accommodation.model";
import { MatDialog, MAT_DIALOG_DATA } from '@angular/material';
import { Inject } from "@angular/core";

@Component({
    selector: 'add-details-popup',
    templateUrl: 'accommodation.details.modal.html',
})
export class AddDetailsModal {

    constructor(public dialogRef: MatDialogRef<AddDetailsModal>,
        @Inject(MAT_DIALOG_DATA) public selectedAccommodationAdd: AccommodationAdd) { }
}