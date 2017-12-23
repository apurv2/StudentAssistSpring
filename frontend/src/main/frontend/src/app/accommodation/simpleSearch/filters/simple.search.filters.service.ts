import { Injectable } from "@angular/core";
import { Http } from '@angular/http';
import { AccommodationSearchModel } from "../../shared/models/accommodation.filter.model";
import { environment } from "../../../../environments/environment";

@Injectable()
export class SimpleSearchFilterService {

    constructor(private http: Http) { }

    getApartmentNames(filterData: AccommodationSearchModel) {
        console.log(environment.url + environment.getAllApartmentsWithType);
        console.log(JSON.stringify(filterData));
        return this.http.post(environment.url + environment.getAllApartmentsWithType, filterData).
            map(res => res.json());

    }

}