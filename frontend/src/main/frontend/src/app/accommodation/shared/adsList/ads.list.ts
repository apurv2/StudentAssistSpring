import { Component, Input, Output, EventEmitter } from "@angular/core";
import { AccommodationAdd } from "../../shared/models/accommodation.model";
import { UniversityAccommodationAdds } from "../../shared/models/university.accommodation.adds.model";
import { SharedDataService } from "../../../shared/data/shared.data.service";
import { AdsListService } from "./ads.list.service";
import { AccommodationAddsList } from "../../accommodation.adds.list";

declare var $: any;

@Component({
    selector: 'adds-list',
    templateUrl: 'ads.list.html'
})

export class AddsList extends AccommodationAddsList {

    @Input()
    accommodationAdds: AccommodationAdd[];
    @Input()
    showVisited: boolean;

    selectedItemId: number;
    paginating: boolean = false;
    @Output() paginationEvent = new EventEmitter<number>();
    stopPagination: boolean;

    constructor(private sharedDataService: SharedDataService,
        private adsListService: AdsListService) {
        super();
    }
    ngOnInit() {
    }

    ngOnChanges() {
        if (this.accommodationAdds != null && this.accommodationAdds.length > 0) {
            if (window.innerWidth > 767) {
                this.addClick(this.accommodationAdds[0]);
            }
            this.paginating = false;
        }
    }

    addClick(accommodationAdd: AccommodationAdd) {

        if (this.showVisited && !accommodationAdd.userVisitedSw) {
            this.adsListService.setUserVisitedAdd(accommodationAdd)
                .subscribe(e => {
                    accommodationAdd.userVisitedSw = true;
                });
        }

        this.selectedItemId = accommodationAdd.addId;
        this.sharedDataService.emitAccommodationAdd(accommodationAdd);
        this.addClickMobile();
    }

    paginationClick() {
        this.paginating = true;
        let position: number = this.accommodationAdds.length + 1;
        this.paginationEvent.emit(position);
    }
}