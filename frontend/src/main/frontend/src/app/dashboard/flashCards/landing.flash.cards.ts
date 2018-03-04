import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs/Subject';
import { FlashCardsModel } from '../models/flash.cards.model';
import { LandingFlashCardsService } from './landing.flash.cards.service';
import { Observable } from 'rxjs/Rx';
import { Subscription } from 'rxjs/Subscription';
import { FlashCardsRequestModel } from 'app/dashboard/models/flash.cards.request.model';
import { SharedDataService } from 'app/shared/data/shared.data.service';
import { AccommodationAdd } from '../../accommodation/shared/models/accommodation.model';
import { MatDialog } from '@angular/material';
import { AddDetailsModal } from '../../accommodation/shared/adDetails/accommodation.details.modal';

@Component({
    selector: 'landing-flash-cards',
    templateUrl: 'landing.flash.cards.html'
})
export class LandingFlashCards {
    flashCardIntervalObservable: Subscription;
    flashCardsData: FlashCardsModel;

    constructor(private router: Router,
        private flashCardsService: LandingFlashCardsService,
        private sharedDataService: SharedDataService,
        private dialog: MatDialog) {
    }

    ngOnInit() {
        this.getFlashCards();
        this.startFlashCardsTimer();
    }

    startFlashCardsTimer() {
        this.flashCardIntervalObservable = Observable
            .interval(10000)
            .subscribe(x => this.getFlashCards());
    }

    openAddDetails(accommodationAdd: AccommodationAdd) {
        this.unsubscribeFlashCards()
        this.dialog.open(AddDetailsModal, {
            data: accommodationAdd
        }).afterClosed().subscribe(result => {
            this.startFlashCardsTimer();
        });
    }

    getFlashCards() {
        let flashCardsRequestModel: FlashCardsRequestModel = new FlashCardsRequestModel();
        if (this.sharedDataService.getUserSelectedUniversitiesList() != null) {
            flashCardsRequestModel.universityIDs = this.sharedDataService
                .getUserSelectedUniversitiesList()
                .map(a => a.universityId);
        }
        flashCardsRequestModel.currentUniversityID = this.sharedDataService.flashCardUniversityID;
        this.flashCardsService.getFlashCards(flashCardsRequestModel)
            .subscribe((flashCards: FlashCardsModel) => {

                if (flashCards.accomodationCards.length > 0 || flashCards.airportCards.length > 0) {
                this.flashCardsData = flashCards;
                    this.sharedDataService.flashCardUniversityID = this.flashCardsData
                        .currentUniversityID;
                }
            });
    }

    unsubscribeFlashCards() {
        this.flashCardIntervalObservable.unsubscribe();
    }

    ngOnDestroy() {
        this.unsubscribeFlashCards();
    }
}
