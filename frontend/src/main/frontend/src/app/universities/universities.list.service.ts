import { Http, Response } from '@angular/http';
import { environment } from 'environments/environment';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/catch';
import { Injectable } from '@angular/core';
import { HttpInterceptorService } from 'app/shared/Interceptor/HttpInterceptorService';

@Injectable()
export class UniversitiesService {
    constructor(public http: HttpInterceptorService) { }

    
    getUniversityDetailsForUser() {
        var unitInformationUrl = `${environment.url}${environment.getUniversityDetailsForUser}`;
        console.log(unitInformationUrl);
        return this.http.get(unitInformationUrl).map((res: Response) => res.json());
    }


    getUniversitiesList() {
        return this.http.get(environment.url).map((res: Response) => res.json());
    }


}