import { Injectable } from "@angular/core";
import { Http } from '@angular/http';
import { AccommodationSearchModel } from "../../shared/models/accommodation.filter.model";

@Injectable()
export class SimpleSearchFilterService {
    constructor(private http: Http) { }

    getApartmentNames(filterData: AccommodationSearchModel) {
        return this.http.get("").
            map(res => res.json());

    }

}