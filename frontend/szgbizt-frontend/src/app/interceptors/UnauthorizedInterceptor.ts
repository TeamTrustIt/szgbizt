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
import { Router } from '@angular/router';
import {HotToastService} from "@ngneat/hot-toast";
import {AuthService} from "../services/AuthService";

@Injectable()
export class UnauthorizedInterceptor implements HttpInterceptor {

  constructor(private router: Router, private toastService: HotToastService, private authService: AuthService) {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    //const token = this.tokenService.getToken()

    //if(!token) return next.handle(request)

    return next.handle(request).pipe(
      catchError((err: any) => {
        if(err instanceof HttpErrorResponse) {
          if(err.status === HttpStatusCode.Unauthorized) {
            this.toastService.error("Unauthorized request")
            this.authService.removeToken()
            //this.store.dispatch(logout())
            this.router.navigate(['/login'])
          }
        }
        return throwError(() => err);
      })
    )
  }
}
