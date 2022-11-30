import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse,
  HttpStatusCode
} from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import {HotToastService} from "@ngneat/hot-toast";

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {

  constructor(private toastService: HotToastService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    return next.handle(request).pipe(
      catchError((err: any, caught: any) => {
        if(err instanceof HttpErrorResponse) {
          switch (err.status) {
            case HttpStatusCode.Forbidden: {
              this.toastService.error("Forbidden request")
              break;
            }
            case HttpStatusCode.BadRequest: {
              if(err.error.message) {
                this.toastService.error("Bad Request: " + err.error.message)
              }
              else {
                this.toastService.error("Bad Request")
              }
              break;
            }
            case HttpStatusCode.InternalServerError: {
              this.toastService.error("Internal Error")
              break;
            }
          }
        }
        return throwError(() => err)
      }),
    )
  }
}
