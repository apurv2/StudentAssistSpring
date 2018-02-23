import { Injectable } from '@angular/core';
import { Http, RequestOptionsArgs, RequestOptions } from "@angular/http";
import { AccommodationAdd } from '../models/accommodation.model';
import { environment } from 'environments/environment';

@Injectable()
export class AddDetailsService {

    constructor(private http: Http) { }

    deleteAdd(url: string) {
        return this.http.delete(url)
            .map(res => res.json());
    }

    makeLink(url: string, body: any) {
        return this.http.post(environment.branchUrl,body)
            .map(res => res.json());
    }

}