import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { AccommodationSearchModel } from '../shared/models/accommodation.filter.model';

@Injectable()
export class SimpleSearchService {
    data: any;
    constructor(public http: Http) {
    }

    getSimpleSearchAdds(filters: AccommodationSearchModel) {

        return this.http.post(environment.getSimpleSearchAdds, filters)
            .map(res => this.data = res.json());
    }

    // getAllApartmentnames() {
    //     return this.http.get(environment.url).map((res: Response) => res.json())
    //         .catch((error: any) => Observable.throw(error.json().error || 'server error'));

    // }

}