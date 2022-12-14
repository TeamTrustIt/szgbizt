import {Injectable} from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse,
  HttpStatusCode
} from '@angular/common/http';
import {catchError, Observable, throwError} from 'rxjs';
import {Router} from '@angular/router';
import {HotToastService} from "@ngneat/hot-toast";
import {AuthService} from "../services/AuthService";
import {logout} from "../actions/auth.actions";
import {Store} from "@ngrx/store";
import {AuthState} from "../interfaces/states/auth-state";

@Injectable()
export class UnauthorizedInterceptor implements HttpInterceptor {

  constructor(private router: Router, private toastService: HotToastService, private authService: AuthService, private store: Store<{ auth: AuthState }>) {
  }

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {

    return next.handle(request).pipe(
      catchError((err: any) => {
        if (err instanceof HttpErrorResponse) {
          if (err.status === HttpStatusCode.Unauthorized) {
            this.toastService.error("Unauthorized request")
            this.authService.removeToken()
            this.store.dispatch(logout())
            this.router.navigate(['/login'])
          }
        }
        return throwError(() => err);
      })
    )
  }
}
