import { Input, Component, Injectable } from "@angular/core";

@Component({
    selector: 'css-carousel',
    templateUrl: 'carousel.component.html',
    styleUrls: ['carousel.styles.css']
})
export class CarouselComponent {

    @Input()
    images: string[];

    ngOnInit() { }

    constructor() { }

    ngOnChanges() { }

}

