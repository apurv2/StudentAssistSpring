import { ErrorHandler, Injectable } from '@angular/core';
import { ErrorLogService } from './error.log.service';

@Injectable()
export class UIErrorHandler extends ErrorHandler {
    constructor(private errorLogService: ErrorLogService) {
        super();
    }
    handleError(error) {
        super.handleError(error);
        this.errorLogService.logError(error);
    }
}
