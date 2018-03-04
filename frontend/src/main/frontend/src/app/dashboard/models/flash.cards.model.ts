import { AccommodationAdd } from "../../accommodation/shared/models/accommodation.model";
import { AirportModel } from "../../airport/airport.model";

export class FlashCardsModel {
    accomodationCards: AccommodationAdd[];
    airportCards: AirportModel[];
    currentUniversityID: number;
    
}