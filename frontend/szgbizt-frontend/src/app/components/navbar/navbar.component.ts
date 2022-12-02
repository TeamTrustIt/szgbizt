import {Component, OnInit} from '@angular/core';
import {AuthService} from "../../services/AuthService";
import {Store} from "@ngrx/store";
import {AuthState} from "../../interfaces/states/auth-state";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  loggedIn: boolean = false
  isAdmin: boolean = false
  title: string = 'SECUSHOP'

  constructor(private store: Store<{ auth: AuthState }>, private authService: AuthService) {
  }

  ngOnInit(): void {
    this.store.subscribe((state: { auth: AuthState }) => {
      this.loggedIn = state.auth.isLoggedIn
      this.isAdmin = state.auth.user?.roles === 'ROLE_ADMIN'
      this.title = this.isAdmin ? 'SECUSHOP ADMIN' : 'SECUSHOP';
    })
  }

  logout() {
    this.authService.logout()
  }
}
