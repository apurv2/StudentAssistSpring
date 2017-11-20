import { Component } from '@angular/core';
import { UniversitiesService } from 'app/universities/universities.list.service';
import { University } from 'app/universities/universities.model';

@Component({
    selector: 'universities.list',
    templateUrl: 'universities.list.html'
})

export class Universities {

    universitiesList: University[];
    constructor(private universitiesService: UniversitiesService) { }

    getUniversitiesList() {

        this.universitiesService.getUniversitiesList().subscribe(res => {
            this.universitiesList = res;
            // TODO  toggle bottom bar
        });
    }
    showBottomBar() { }
    createNewUserWithUniversitiesSelection() { }
    toggleBottomBar() { }
}    