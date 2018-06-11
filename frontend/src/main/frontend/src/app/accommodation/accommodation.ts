import { ViewChild } from "@angular/core";
import { SimpleSearchAddsList } from "./simpleSearch/adsList/simple.search.adds.list";
import { AccommodationAddsList } from "./accommodation.adds.list";
import { PlatformLocation } from "@angular/common";

export class Accommodation {
    @ViewChild('addsList') addsList: AccommodationAddsList;
    backPressed: boolean;
}