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
  loggingIn: boolean = false;

  subscription?: Subscription

  constructor(private authService: AuthService, private alertService: HotToastService) {
  }

  login() {
    if (this.username !== "" && this.password !== "") {
      this.loggingIn = true
      this.subscription = this.authService.login(this.username, this.password).subscribe({
        next: (res) => {
          this.alertService.success(`Successfully logged in as ${res.user.username}`)
          this.loggingIn = false
        },
        complete: () => {
          console.log("complete")
          this.loggingIn = false
        },
        error: err => {
          console.log("err")
          // todo error mint mutlkor mindenhol

          this.loggingIn = false
        }
      })
    }
    else {
      this.alertService.warning("Fill every field")
    }
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe()
  }
}
