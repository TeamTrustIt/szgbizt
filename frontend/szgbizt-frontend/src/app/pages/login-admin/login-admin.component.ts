import {Component, OnDestroy} from '@angular/core';
import {AuthService} from "../../services/AuthService";
import {Subscription} from "rxjs";
import {HotToastService} from "@ngneat/hot-toast";

@Component({
  selector: 'app-login-admin',
  templateUrl: './login-admin.component.html',
  styleUrls: ['./login-admin.component.css']
})
export class LoginAdminComponent implements OnDestroy {

  username: string = ""
  password: string = ""
  subscription?: Subscription

  constructor(private authService: AuthService, private alertService: HotToastService) {
  }

  login() {
    if (this.username !== "" && this.password !== "") {
      this.subscription = this.authService.login(this.username, this.password).subscribe()
    }
    else {
      this.alertService.warning("Fill every field")
    }
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe()
  }
}
