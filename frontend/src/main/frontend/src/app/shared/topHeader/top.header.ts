import { Component } from "@angular/core";

@Component({
    selector: 'top-header',
    templateUrl: 'top.header.html'
})

export class TopHeader {

    showSideNav: boolean;

    ngOnInit() {

    }
    openNav() {
        document.getElementById("mySidenav").style.width = "250px";
        document.getElementById("nav-overlay").style.display = "block";
        
        this.showSideNav = true;
    }



    closeNav() {
        document.getElementById("mySidenav").style.width = "0px";
        document.getElementById("nav-overlay").style.display = "none";
        
        this.showSideNav = false;
    }



}    