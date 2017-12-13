import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { environment } from '../../../environments/environment';
import { Subject } from 'rxjs/Subject';
import { University } from '../../accommodation/shared/models/universities.list.model';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/distinctUntilChanged';
import 'rxjs/add/operator/switchMap';
import 'rxjs/add/observable/of';

@Injectable()
export class LandingSearchService {
    baseUrl: string = environment.url + environment.universityName;

    constructor(private http: Http) { }

    search(terms: Observable<string>) {
        return terms.debounceTime(400)
            .distinctUntilChanged()
            .switchMap(term => this.searchEntries(term));
    }
    searchEntries(term) {
        return term ?
            this.http.get(this.baseUrl + '/' + term)
                .map(res => res.json()) : Observable.of([])
    }

}