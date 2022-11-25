import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {AdminService} from "../../services/AdminService";
import {UserLoginDto} from "../../interfaces/user-login-dto";
import {HotToastService} from "@ngneat/hot-toast";

@Component({
  selector: 'app-list-user',
  templateUrl: './list-user.component.html',
  styleUrls: ['./list-user.component.css']
})
export class ListUserComponent implements OnInit, OnDestroy {

  users: UserLoginDto[] = [];
  subscriptionGet?: Subscription
  subscriptionDelete?: Subscription
  deleting: boolean = false;

  constructor(private adminService: AdminService, private alertService: HotToastService) {
  }

  ngOnInit(): void {
    this.getUsers()
  }

  getUsers() {
    this.subscriptionGet = this.adminService.getUsers().subscribe(res => {
      this.users = res
    })
  }

  deleteUser(id: string, name: string) {
    const sure: boolean = window.confirm(`Are you sure to delete ${name}?`)
    if (sure) {
      this.deleting = true
      this.subscriptionDelete = this.adminService.deleteUserById(id).subscribe({
        next: (res) => {
          this.deleting = false
          this.alertService.success(name + " successfully deleted")
          this.getUsers()
        },
        complete: () => {
          this.deleting = false
        }
      })
    }
  }

  ngOnDestroy(): void {
    this.subscriptionGet?.unsubscribe()
    this.subscriptionDelete?.unsubscribe()
  }
}
