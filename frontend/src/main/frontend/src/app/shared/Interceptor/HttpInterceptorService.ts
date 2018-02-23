import { Injectable } from '@angular/core';
import { Http, Headers } from '@angular/http';
import { FacebookService } from 'ngx-facebook';
import { ConnectionBackend, RequestOptions, Request, RequestOptionsArgs, Response } from "@angular/http";
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs/Observable';
import { XHRBackend } from '@angular/http/src/backends/xhr_backend';
import { FacebookModule } from 'ngx-facebook/dist/esm/facebook.module';

export function httpFactory(xhrBackend: XHRBackend,
  requestOptions: RequestOptions,
  fb: FacebookService): Http {
  return new HttpInterceptorService(xhrBackend, requestOptions, fb);
}

@Injectable()
export class HttpInterceptorService extends Http {

  constructor(backend: ConnectionBackend,
    defaultOptions: RequestOptions,
    private fb: FacebookService) {
    super(backend, defaultOptions);
  }

  createAuthorizationHeader(headers: Headers) {
    let accessToken = this.fb.getAuthResponse() == null ? '0' : this.fb.getAuthResponse()['accessToken'];
    headers.append(environment.accessToken, accessToken);
  }

  request(url: string | Request, options?: RequestOptionsArgs): Observable<Response> {
    return super.request(url, options);
  }

  get(url: string, options?: RequestOptionsArgs): Observable<Response> {
    url = this.updateUrl(url);
    return super.get(url, this.getRequestOptionArgs(options));
  }

  post(url: string, body: string, options?: RequestOptionsArgs): Observable<Response> {
    url = this.updateUrl(url);
    return url.indexOf("cloudinary") == -1 ? super.post(url, body,
      this.getRequestOptionArgs(options, url)) : super.post(url, body);
  }

  put(url: string, body: string, options?: RequestOptionsArgs): Observable<Response> {
    url = this.updateUrl(url);
    return super.put(url, body, this.getRequestOptionArgs(options));
  }

  delete(url: string, options?: RequestOptionsArgs): Observable<Response> {
    url = this.updateUrl(url);
    return super.delete(url, this.getRequestOptionArgs(options));
  }

  private updateUrl(req: string) {

    let finalUrl: string = req.indexOf("branch") == -1 ? environment.url + req : req;
    console.log(finalUrl);
    return finalUrl;

  }

  private getRequestOptionArgs(options?: RequestOptionsArgs, url?: string): RequestOptionsArgs {
    if (options == null) {
      options = new RequestOptions();
    }
    if (options.headers == null) {
      options.headers = new Headers();
    }

    this.createAuthorizationHeader(options.headers);
    options.headers.append('Content-Type', 'application/json');

    return options;
  }
}