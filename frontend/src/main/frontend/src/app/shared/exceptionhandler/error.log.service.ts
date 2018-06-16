import { ErrorHandler, Injectable } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { Http } from "@angular/http";
import { environment } from 'environments/environment';
import { ErrorInfo } from './error.model';



// Our service to handle errors (ideally in its own file)
@Injectable()
export class ErrorLogService {

    constructor(private http: Http) { }

    logError(error: ErrorInfo) {
        return this.http.post(environment.errors, error)
            .map(res => res.json());
    }
}