import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { AccommodationSearchModel } from '../shared/models/accommodation.filter.model';

@Injectable()
export class AdvanceSearchService {

    constructor(public http: Http) {
    }

    getAdvancedSearchAdds(filters: AccommodationSearchModel) {
        console.log(JSON.stringify(filters));
        console.log(environment.url + environment.getAdvancedSearchAdds);

        return this.http.post(environment.url + environment.getAdvancedSearchAdds, filters)
            .map(res => res.json());
    }

}