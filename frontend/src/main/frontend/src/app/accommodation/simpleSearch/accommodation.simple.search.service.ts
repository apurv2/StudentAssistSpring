import { Http, Response } from '@angular/http';
import { environment } from 'environments/environment';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';

export class SimpleSearchService {
    data: any;
    constructor(public http: Http) {

    }

    getSimpleSearchAdds() {

        this.http.get(environment.url)
            .subscribe(res => this.data = res.json());

    }

    getAllApartmentnames() {
        return this.http.get(environment.url).map((res: Response) => res.json())
            .catch((error: any) => Observable.throw(error.json().error || 'server error'));

    }

}