import {Component, OnDestroy} from '@angular/core';
import {AuthService} from "../../services/AuthService";
import {Subscription} from "rxjs";
import {HotToastService} from "@ngneat/hot-toast";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnDestroy {

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
      // todo remove
      this.subscription = this.authService.login('ttest1', '1234').subscribe()
    }
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe()
  }
}
