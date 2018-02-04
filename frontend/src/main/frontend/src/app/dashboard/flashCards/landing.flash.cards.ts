import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs/Subject';
import { FlashCardsModel } from '../models/flash.cards.model';
import { LandingFlashCardsService } from './landing.flash.cards.service';
import {Observable} from 'rxjs/Rx';
import { FlashCardsRequestModel } from '../models/flash.cards.request.model';
import { SharedDataService } from '../../shared/data/shared.data.service';
import { University } from '../../accommodation/shared/models/universities.list.model';

@Component({
    selector: 'landing-flash-cards',
    templateUrl: 'landing.flash.cards.html'
})
export class LandingFlashCards {

    flashCardsData: FlashCardsModel;

    constructor(private router: Router,
        private flashCardsService: LandingFlashCardsService,
        private sharedDataService: SharedDataService) {
            
    }

    ngOnInit() {
        this.getFlashCards();
        Observable.interval(10000).subscribe(x => {
           this.getFlashCards();
          });
    }

    searchResultCardClick() {
        this.router.navigate(['/simple-search/']);
    }

    getFlashCards() {
        let flashCardsRequestModel = new FlashCardsRequestModel();
        let selectedUniversities = this.sharedDataService.getUserSelectedUniversitiesList();
        flashCardsRequestModel.universityIDs = new Array(selectedUniversities.length);
        for(let i=0;i<selectedUniversities.length;i++){
            flashCardsRequestModel.universityIDs[i] = selectedUniversities[i].universityId;
        }
        flashCardsRequestModel.currentUniversityID = this.flashCardsData == null ? 0 : this.flashCardsData.currentUniversityID;
        this.flashCardsService.getFlashCards(flashCardsRequestModel)
            .subscribe(flashCards => { 
                this.flashCardsData = flashCards
            });
    }
}
