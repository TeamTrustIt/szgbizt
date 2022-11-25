import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import { Observable } from 'rxjs';
import {Store} from "@ngrx/store";
import {AuthState} from "../interfaces/states/auth-state";

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {

  readonly mustBeAdmin = [
    '/admin-users'
  ]

  readonly mustNotBeAdmin = [
    '/login',
    '/register'
  ]

  private isAdmin!: boolean

  public constructor(
    private store: Store<{ auth: AuthState }>,
    private router: Router
  ) {
    this.store.subscribe((state: { auth: AuthState }) => {
      this.isAdmin = state.auth.user?.roles === 'ROLE_ADMIN'
    })
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    const url = state.url

    return this.isAdmin ?
      this.mustNotBeAdmin.indexOf(url) === -1 ? true : this.router.navigate(['/home']) :
      this.mustBeAdmin.indexOf(url) === -1 ? true : this.router.navigate(['/admin-login'])
  }

  /*canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return true;
  }*/

}
