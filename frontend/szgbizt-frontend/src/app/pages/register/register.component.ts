import {Component, OnDestroy} from '@angular/core';
import {AuthService} from "../../services/AuthService";
import {Subscription} from "rxjs";
import {HotToastService} from "@ngneat/hot-toast";
import {Router} from "@angular/router";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnDestroy {

  email: string = ""
  username: string = ""
  password: string = ""
  passwordConfirm: string = ""

  subscription?: Subscription
  registering: boolean = false;

  constructor(private authService: AuthService, private alertService: HotToastService, private router: Router) {
  }

  register() {
    if (this.email !== "" && this.username !== "" && this.password !== "" && this.passwordConfirm !== "") {
      if (this.password === this.passwordConfirm) {
        this.registering = true
        this.subscription = this.authService.register(this.email, this.password, this.username).subscribe({
          next: (res) => {
            this.registering = false
            this.alertService.success("Successful Registration for " + res.username)
            this.router.navigateByUrl('/login')
          },
          error: _err => {
            this.registering = false
          }
        })
      } else {
        this.alertService.warning("Passwords don't match")
      }
    } else {
      this.alertService.warning("Fill every field")
    }
  }

  ngOnDestroy(): void {
  }
}
