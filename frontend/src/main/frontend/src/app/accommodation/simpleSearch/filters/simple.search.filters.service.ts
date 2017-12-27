import { Injectable } from "@angular/core";
import { Http } from '@angular/http';
import { AccommodationSearchModel } from "../../shared/models/accommodation.filter.model";
import { environment } from "../../../../environments/environment";

@Injectable()
export class SimpleSearchFilterService {

    constructor(private http: Http) { }

    getApartmentNames(filterData: AccommodationSearchModel) {
        return this.http.post(environment.getAllApartmentsWithType, filterData).
            map(res => res.json());

    }

}