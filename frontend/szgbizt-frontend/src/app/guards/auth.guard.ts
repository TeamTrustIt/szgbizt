import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Store} from '@ngrx/store';
import {Observable} from 'rxjs';
import {AuthState} from '../interfaces/states/auth-state';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  readonly mustBeLoggedIn = [
    '/home',
    '/detail/:id',
    '/profile',
    '/upload'
  ]

  readonly mustBeLoggedOut = [
    '/login',
    '/register'
  ]

  private loggedIn!: boolean

  public constructor(
    private store: Store<{ auth: AuthState }>,
    private router: Router
  ) {
    this.store.subscribe((state: { auth: AuthState }) => {
      this.loggedIn = state.auth.isLoggedIn
    })
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const url = state.url

    return this.loggedIn ?
      this.mustBeLoggedOut.indexOf(url) === -1 ? true : this.router.navigate(['/home']) :
      this.mustBeLoggedIn.indexOf(url) === -1 ? true : this.router.navigate(['/login'])
  }
}
