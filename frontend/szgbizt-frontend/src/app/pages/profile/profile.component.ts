import {Component, OnInit} from '@angular/core';
import {UserService} from "../../services/UserService";
import {UserEditDto} from "../../interfaces/user-edit-dto";
import {Router} from "@angular/router";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit{

  userO: UserEditDto = {caffs: [], email: "", id: 0, name: "", role: ""}
  currentPassword: string = "";
  newPassword: string = "";
  newPasswordConfirm: string = "";

  constructor(private userService: UserService, private router: Router) {
  }

  ngOnInit(): void {
    this.userService.getUserData().subscribe(res => {
      this.userO = res
    })
  }

  navigateToDetail(id: number) {
    this.router.navigateByUrl("/detail/" + id)
  }
}
