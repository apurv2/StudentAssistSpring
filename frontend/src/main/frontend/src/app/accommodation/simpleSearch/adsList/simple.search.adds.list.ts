import { Component, Input, Host, Output, EventEmitter, HostListener } from "@angular/core";
import { AccommodationAdd } from "../../shared/models/accommodation.model";
import { UniversityAccommodationAdds } from "../../shared/models/university.accommodation.adds.model";
import { SharedDataService } from "../../../shared/data/shared.data.service";
import { SimpleSearchFilterService } from "../filters/simple.search.filters.service";
import { environment } from "environments/environment";
import { AccommodationSearchModel } from "../../shared/models/accommodation.filter.model";
import { SimpleSearch } from "../accommodation.simple.search";
import { SimpleSearchAddsFilters } from "../filters/simple.search.filters";
import { Observable } from 'rxjs/Rx';
import { Subscription } from 'rxjs/Subscription';
import { AdsListService } from "../../shared/adsList/ads.list.service";
import { UserService } from "../../../shared/userServices/user.service";


declare var $: any;

@Component({
    selector: 'simple-search-list',
    templateUrl: 'simple.search.ads.list.html'
})

export class SimpleSearchAddsList {

    @Input()
    accommodationSearchResults: UniversityAccommodationAdds[];
    @Input()
    filters: SimpleSearchAddsFilters;
    selectedItemId: number;
    timerObservable: Subscription;
    showingDetails: boolean = false;


    constructor(private sharedDataService: SharedDataService,
        private simpleSearchService: SimpleSearchFilterService,
        private adsListService: AdsListService) { }

    ngOnInit() {
        $('.collapsible').collapsible();
    }

    ngOnChanges() {
        if (this.accommodationSearchResults != null && this.accommodationSearchResults.length > 0) {

            if (window.innerWidth > 767) {
                this.addClick(this.accommodationSearchResults[0].accommodationAdds[0]);
            }

            this.timerObservable = Observable
                .interval(300)
                .first()
                .subscribe(x => {
                    $('.collapsible').collapsible('open', 0);
                    this.filters.stopLoding();
                });
        }
    }

    universityClick() {
        this.timerObservable.unsubscribe();
    }


    addClick(accommodationAdd: AccommodationAdd) {
        if (!accommodationAdd.userVisitedSw) {
            this.adsListService.setUserVisitedAdd(accommodationAdd)
                .subscribe(e => {
                    accommodationAdd.userVisitedSw = true;
                });
        }
        this.selectedItemId = accommodationAdd.addId;
        this.sharedDataService.emitAccommodationAdd(accommodationAdd);

        if (window.innerWidth < 767) {
            this.showingDetails = true;
            document.getElementById("detailCard").style.display = 'block';
            document.getElementById("simpleSearchList").style.display = 'none';
        }
    }

    paginationClick(selectedUniversity: UniversityAccommodationAdds) {
        selectedUniversity.paginating = true;

        let search: AccommodationSearchModel = this.filters.getSpinners();
        search.selectedUniversityId = selectedUniversity.universityId;
        search.paginationPosition = selectedUniversity.accommodationAdds.length + 1;

        this.simpleSearchService
            .getSimpleSearchAddsForPagination(environment.getSimpleSearchAddsPagination, search)
            .subscribe((data: AccommodationAdd[]) => {

                data
                    .map(add => this.accommodationSearchResults
                        .find(university => university.universityId == selectedUniversity.universityId)
                        .accommodationAdds
                        .push(add));
                selectedUniversity.paginating = false;
                selectedUniversity.stopPagination = data.length < 10;

            });
    }

    showList() {

        if (window.innerWidth < 767) {
            this.showingDetails = false;
            document.getElementById("detailCard").style.display = 'none';
            document.getElementById("simpleSearchList").style.display = 'block';
        }
    }

    ngOnDestroy() {
        if (this.timerObservable != null)
            this.timerObservable.unsubscribe();
    }

}