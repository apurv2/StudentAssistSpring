import { Http, Response } from '@angular/http';
import { environment } from 'environments/environment';
import { Injectable } from '@angular/core';

@Injectable()
export class AccommodationNotificationService {
    constructor(public http: Http) { }

    getAccommodationNotifications(position: number) {
        return this.http.get(environment.getAccommodationNotifications + "?position=" + position)
            .map(res => res.json());
    }

}