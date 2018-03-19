import { Injectable } from '@angular/core';
import { Http, RequestOptionsArgs, RequestOptions } from "@angular/http";
import { AccommodationAdd } from '../shared/models/accommodation.model';

@Injectable()
export class PostAccommodationService {

    constructor(private http: Http) { }
    postImages(url, params) {

        return this.http.post(url, params)
            .map(res => res.json());
    }

    postAccommodation(url, accommodationAdd: AccommodationAdd ) {
        console.log(accommodationAdd.postedTill);
        return this.http.put(url, accommodationAdd)
            .map(res => res.json());
    }

    addApartment(url, params) {
        return this.http.post(url, params)
            .map(res => res.text());
    }

}