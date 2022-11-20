import { Component, OnInit } from '@angular/core';
import {AuthService} from "../../services/AuthService";
import {Store} from "@ngrx/store";
import {AuthState} from "../../interfaces/states/auth-state";
import { logout } from 'src/app/actions/auth.actions';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  loggedIn: boolean = false

  constructor(private store: Store<{ auth: AuthState }>, private authService: AuthService) { }

  ngOnInit(): void {
    this.store.subscribe((state: { auth: AuthState }) => {
      this.loggedIn = state.auth.isLoggedIn
    })
  }

  logout() {
    this.authService.logout()
  }
}
