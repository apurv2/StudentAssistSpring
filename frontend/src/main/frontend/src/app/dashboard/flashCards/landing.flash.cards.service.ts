import { Injectable } from "@angular/core";
import { Http } from "@angular/http";
import { environment } from "../../../environments/environment";
import { FlashCardsModel } from "../models/flash.cards.model";
import { FlashCardsRequestModel } from "../models/flash.cards.request.model";

@Injectable()
export class LandingFlashCardsService {

    constructor(public http: Http) { }

    getFlashCards(flashCardsRequestModel :FlashCardsRequestModel) {

        console.log(JSON.stringify(flashCardsRequestModel));
        return this.http.post(environment.getFlashCards, flashCardsRequestModel)
            .map(res => res.json());

    }
}