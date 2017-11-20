import { Component } from "@angular/core";
import { NotificationSettingsService } from "app/notifications/notifications.subscribe.modal.service";

@Component({
    selector: 'notification-settings',
    templateUrl: 'notifications.subscribe.modal.html'
})

export class NotificationSettingsModal {

    constructor(private notificationSettingsService: NotificationSettingsService) {

    }

    getNotificationSettings() {
      //  this.notificationSettingsService().subscribe()

    }


}