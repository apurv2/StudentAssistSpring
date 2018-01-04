import { Observable } from "rxjs/Observable";
import { NotificationSettings } from "app/notifications/models/notification.settings.model";
import { environment } from "environments/environment";
import { Injectable } from "@angular/core";
import { Http } from '@angular/http';

@Injectable()
export class NotificationSettingsService {

    constructor(private http: Http) { }

    getNotificationSettings() {
        return this.http.get(environment.getNotificationSettings).
            map(res => res.json());
    }

    subscribeForNotifications(settings: NotificationSettings) {
        return this.http.put(environment.subscribeNotifications, settings).
            map(res => res.json());
    }

}