import { Injectable } from '@angular/core';
import { Http, RequestOptionsArgs, RequestOptions } from "@angular/http";

@Injectable()
export class PostAccommodationService {

    constructor(private http: Http) { }
    postImages(url, params) {

        return this.http.post(url, params)
            .map(res => res.json());
    }

    postAccommodation(url, params) {
        return this.http.put(url, params)
            .map(res => res.json());
    }

    addApartment(url, params) {
        return this.http.post(url, params)
            .map(res => res.text());
    }

}