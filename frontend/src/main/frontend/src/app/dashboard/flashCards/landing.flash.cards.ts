import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs/Subject';
import { FlashCardsModel } from '../models/flash.cards.model';
import { LandingFlashCardsService } from './landing.flash.cards.service';

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
        this.getInitialFlashCards();
    }

    searchResultCardClick() {
        this.router.navigate(['/simple-search/']);
    }

    getInitialFlashCards() {
        this.flashCardsService.getInitialFlashCards()
            .subscribe(flashCards => {
                this.flashCardsData = flashCards
            });
    }
}
