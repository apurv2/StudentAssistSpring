import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs/Subject';
import { FlashCardsModel } from '../models/flash.cards.model';
import { LandingFlashCardsService } from './landing.flash.cards.service';
import { Observable } from 'rxjs/Rx';
import { Subscription } from 'rxjs/Subscription';
import { FlashCardsRequestModel } from 'app/dashboard/models/flash.cards.request.model';
import { SharedDataService } from 'app/shared/data/shared.data.service';

@Component({
    selector: 'landing-flash-cards',
    templateUrl: 'landing.flash.cards.html'
})
export class LandingFlashCards {
    flashCardIntervalObservable: Subscription;
    flashCardsData: FlashCardsModel;

    constructor(private router: Router,
        private flashCardsService: LandingFlashCardsService,
        private sharedDataService: SharedDataService) {
    }

    ngOnInit() {
        this.getFlashCards();
        this.flashCardIntervalObservable = Observable.interval(10000).subscribe(x => {
            this.getFlashCards();
        });
    }

    searchResultCardClick() {
        this.router.navigate(['/simple-search/']);
    }

    getFlashCards() {
        let flashCardsRequestModel: FlashCardsRequestModel = new FlashCardsRequestModel();
        if(this.sharedDataService.getUserSelectedUniversitiesList()!=null){
            flashCardsRequestModel.universityIDs = this.sharedDataService.getUserSelectedUniversitiesList().map(a=>a.universityId);
        }        
        flashCardsRequestModel.currentUniversityID = this.sharedDataService.flashCardUniversityID;        
        this.flashCardsService.getFlashCards(flashCardsRequestModel)
            .subscribe(flashCards => {
                this.flashCardsData = flashCards;
                this.sharedDataService.flashCardUniversityID = this.flashCardsData.currentUniversityID;
            });
    }

    ngOnDestroy() {
        this.flashCardIntervalObservable.unsubscribe();
    }
}
