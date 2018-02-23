import { Component } from "@angular/core";
import { MatDialogRef } from "@angular/material";
import { MatDialog, MAT_DIALOG_DATA } from '@angular/material';
import { Inject } from "@angular/core";

@Component({
    selector: 'copy-link',
    templateUrl: 'copy.link.modal.html',
})
export class CopyLinkModal {

    constructor(public dialogRef: MatDialogRef<CopyLinkModal>,
        @Inject(MAT_DIALOG_DATA) public copyText: string) {
    }

    copy() {
        var copyFrom = document.createElement("textarea");
        copyFrom.textContent = this.copyText;
        var body = document.getElementsByTagName('body')[0];
        body.appendChild(copyFrom);
        copyFrom.select();
        document.execCommand('copy');
        body.removeChild(copyFrom);
    }
}