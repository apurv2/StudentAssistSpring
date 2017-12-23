import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs/Subject';
import { FlashCardsModel } from '../models/flash.cards.model';
import { LandingFlashCardsService } from './landing.flash.cards.service';
import {Observable} from 'rxjs/Rx';

@Component({
    selector: 'landing-flash-cards',
    templateUrl: 'landing.flash.cards.html'
})
export class LandingFlashCards {

    flashCardsData: FlashCardsModel;

    constructor(private router: Router,
        private flashCardsService: LandingFlashCardsService) {
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
        this.flashCardsService.getFlashCards()
            .subscribe(flashCards => {
                this.flashCardsData = flashCards
            });
    }
}
