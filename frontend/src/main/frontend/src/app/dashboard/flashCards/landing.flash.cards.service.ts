import { Injectable } from "@angular/core";
import { Http } from "@angular/http";
import { environment } from "../../../environments/environment";
import { FlashCardsModel } from "../models/flash.cards.model";

@Injectable()
export class LandingFlashCardsService {

    constructor(public http: Http) { }

    getInitialFlashCards() {

        let flashCardModel = new FlashCardsModel();
        return this.http.post(environment.getFlashCards, flashCardModel)
            .map(res => res.json());

    }
}