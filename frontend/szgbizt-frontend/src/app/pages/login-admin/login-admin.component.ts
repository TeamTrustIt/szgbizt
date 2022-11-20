import { Component } from '@angular/core';
import {AuthService} from "../../services/AuthService";

@Component({
  selector: 'app-login-admin',
  templateUrl: './login-admin.component.html',
  styleUrls: ['./login-admin.component.css']
})
export class LoginAdminComponent {

  constructor(private authService: AuthService) {
  }

  login() {
    this.authService.adminLogin("test@test.hu", "")
  }
}
