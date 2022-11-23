import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {User} from "../../interfaces/user";
import {AdminService} from "../../services/AdminService";

@Component({
  selector: 'app-list-user',
  templateUrl: './list-user.component.html',
  styleUrls: ['./list-user.component.css']
})
export class ListUserComponent implements OnInit, OnDestroy{

  users: User[] = [];
  subscriptionGet?: Subscription
  subscriptionDelete?: Subscription

  constructor(private adminService: AdminService) {
  }

  ngOnInit(): void {
    this.getUsers()
  }

  getUsers() {
    this.subscriptionGet = this.adminService.getUsers().subscribe(res => {
      this.users = res
    })
  }

  deleteUser(id: number) {
    this.subscriptionDelete = this.adminService.deleteUser(id).subscribe(res => {
      if(res.isSuccess){
        this.getUsers()
      }
    })
  }

  ngOnDestroy(): void {
    this.subscriptionGet?.unsubscribe()
    this.subscriptionDelete?.unsubscribe()
  }
}
