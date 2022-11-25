import {Component, OnDestroy, OnInit} from '@angular/core';
import {UserService} from "../../services/UserService";
import {UserEditDto} from "../../interfaces/user-edit-dto";
import {Router} from "@angular/router";
import {HotToastService} from "@ngneat/hot-toast";
import {Subscription} from "rxjs";
import {AuthService} from "../../services/AuthService";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit, OnDestroy {

  userO: UserEditDto = {caffData: [], email: "", id: "", username: "", roles: "ROLE_USER", balance: 0}
  currentPassword: string = "";
  newPassword: string = "";
  newPasswordConfirm: string = "";

  updatingProfile: boolean = false
  updatingPassword: boolean = false
  deletingUser: boolean = false

  subscriptionGetData?: Subscription
  subscriptionUpdateProfile?: Subscription
  subscriptionUpdatePassword?: Subscription
  subscriptionDeleteUser?: Subscription

  constructor(private userService: UserService, private router: Router, private alertService: HotToastService, private authService: AuthService) {
  }

  ngOnInit(): void {
    this.getUserData()
  }

  getUserData() {
    this.subscriptionGetData = this.userService.getUserDataById().subscribe((res: UserEditDto) => {
      this.userO = res
    })
  }

  updateProfile() {
    if (this.userO.email !== "" && this.userO.username !== "") {
      // todo
      this.updatingProfile = true
      /*this.subscriptionUpdateProfile = this.userService.updateProfile()*/
    } else {
      this.alertService.warning("Username and Email must not be empty")
    }
  }

  updatePassword() {
    if (this.currentPassword !== "" && this.newPassword !== "" && this.newPasswordConfirm !== "") {
      // todo
      this.updatingPassword = true
      /*this.subscriptionUpdatePassword = this.userService.updateProfile()*/
    } else {
      this.alertService.warning("Password fields must not be empty")
    }
  }

  deleteUser() {
    const sure: boolean = window.confirm("Are you sure?")
    if (sure) {
      this.deletingUser = true
      this.subscriptionDeleteUser = this.userService.deleteUser().subscribe({
        next: (res) => {
          this.alertService.success("User successfully deleted")
          this.deletingUser = false
          this.authService.logout()
        },
        complete: () => {
          this.deletingUser = false
        }
      })
    }
  }

  deleteCaff(id: string, filename: string) {
    const sure = window.confirm(`Are you sure to delete ${filename}?`)
    if (sure) {
      this.userService.deleteCaff(id).subscribe(res => {
        this.alertService.success(`${filename} successfully delete`)
        this.getUserData()
      })
    }
  }


  navigateToDetail(id: string) {
    this.router.navigateByUrl("/detail/" + id)
  }

  ngOnDestroy(): void {
    this.subscriptionUpdateProfile?.unsubscribe()
    this.subscriptionUpdatePassword?.unsubscribe()
    this.subscriptionDeleteUser?.unsubscribe()
    this.subscriptionGetData?.unsubscribe()
  }
}
