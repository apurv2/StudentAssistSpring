import { Component, Inject, NgZone, ViewChild, ElementRef } from '@angular/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA, ErrorStateMatcher } from '@angular/material';
import { FacebookService } from 'ngx-facebook/dist/esm/providers/facebook';
import { LoginResponse } from 'ngx-facebook/dist/esm/models/login-response';
import { Http, Response } from '@angular/http';
import 'rxjs/add/operator/map';
import { Observable } from 'rxjs/Observable';
import { MapsAPILoader } from '@agm/core';
import { } from '@types/googlemaps';
import { FormControl, Validators, FormGroupDirective, NgForm } from '@angular/forms';
import { Apartment } from '../../shared/models/apartment.names.model';


@Component({
    selector: 'new-aparment',
    templateUrl: 'new.apartment.modal.html',
    styles: [`
    agm-map {
      height: 300px;
    }
  `]
})

export class NewApartmentModal {

    @ViewChild("search")
    public searchElementRef: ElementRef;

    public latitude: number;
    public longitude: number;
    public searchControl: FormControl;
    public zoom: number;

    constructor(public dialogRef: MatDialogRef<NewApartmentModal>,
        private mapsApiLoader: MapsAPILoader,
        private ngZone: NgZone,
        @Inject(MAT_DIALOG_DATA) public apartment: Apartment) {
    }

    validFormControl = new FormControl('', [
        Validators.required
    ]);

    ngOnInit() {

        //set google maps defaults
        this.zoom = 4;
        this.latitude = 39.8282;
        this.longitude = -98.5795;

        //create search FormControl
        this.searchControl = new FormControl('', [
            Validators.required
        ]);
        this.setCurrentPosition();


        //load Places Autocomplete
        this.mapsApiLoader.load().then(() => {
            let autocomplete = new google.maps.places.Autocomplete(this.searchElementRef.nativeElement, {
                types: ["address"]
            });
            autocomplete.addListener("place_changed", () => {
                this.ngZone.run(() => {
                    //get the place result
                    let place: google.maps.places.PlaceResult = autocomplete.getPlace();

                    //verify result
                    if (place.geometry === undefined || place.geometry === null) {
                        return;
                    }

                    this.latitude = place.geometry.location.lat();
                    this.longitude = place.geometry.location.lng();
                    this.zoom = 12;

                    let componentForm = {
                        street_number: 'short_name',
                        route: 'long_name',
                        locality: 'long_name',
                        administrative_area_level_1: 'short_name',
                        postal_code: 'short_name'
                    };


                    let apartmentItem = {
                        street_number: 'street_num',
                        route: 'addr_line',
                        locality: 'city',
                        administrative_area_level_1: 'state',
                        postal_code: 'zip'
                    };
                    for (var i = 0; i < place.address_components.length; i++) {
                        var addressType = place.address_components[i].types[0];
                        if (componentForm[addressType]) {
                            var val = place.address_components[i][componentForm[addressType]];
                            this.apartment[apartmentItem[addressType]] = val;
                        }
                    }
                });
            });
        });
    }

    private setCurrentPosition() {
        if ("geolocation" in navigator) {
            navigator.geolocation.getCurrentPosition((position) => {
                this.latitude = position.coords.latitude;
                this.longitude = position.coords.longitude;
                this.zoom = 16;
            });
        }
    }

    closeDialog(): void {
        this.apartment.addr_line = this.apartment.street_num + " " + this.apartment.addr_line
        this.dialogRef.close(this.apartment);
    }
}
