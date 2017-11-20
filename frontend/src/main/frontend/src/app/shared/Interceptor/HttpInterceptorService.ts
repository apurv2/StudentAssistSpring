import { Injectable } from '@angular/core';
import { Http, Headers } from '@angular/http';
import { FacebookService } from 'ngx-facebook';
import { environment } from 'environments/environment';

@Injectable()
export class HttpInterceptorService {

  constructor(private http: Http,
    private fb: FacebookService) { }

  createAuthorizationHeader(headers: Headers) {
    let accessToken = this.fb.getAuthResponse()['accessToken'];
    headers.append(environment.accessToken, accessToken);
  }

  get(url) {
    let headers = new Headers();
    this.createAuthorizationHeader(headers);
    return this.http.get(url, {
      headers: headers
    });
  }

  post(url, data) {
    let headers = new Headers();
    this.createAuthorizationHeader(headers);
    return this.http.post(url, data, {
      headers: headers
    });
  }
}