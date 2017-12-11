import { Component } from "@angular/core";
import { Router } from '@angular/router';

@Component({
    selector: 'top-header',
    templateUrl: 'top.header.html'
})

export class TopHeader {

    showSideNav: boolean;

    constructor(private router: Router) { }

    ngOnInit() {

    }
    openNav() {
        document.getElementById("mySidenav").style.width = "250px";
        // document.getElementById("nav-overlay").style.display = "block";

        this.showSideNav = true;
    }

    closeNav() {
        document.getElementById("mySidenav").style.width = "0px";
        // document.getElementById("nav-overlay").style.display = "none";

        this.showSideNav = false;
    }

    openPostAccommodation() {
        this.router.navigate(['/post/']);
    }

}    