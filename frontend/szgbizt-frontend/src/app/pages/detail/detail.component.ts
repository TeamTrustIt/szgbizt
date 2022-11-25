import {Component, OnDestroy, OnInit} from '@angular/core';
import {CaffData} from "../../interfaces/caff-data";
import {ActivatedRoute} from "@angular/router";
import {UserService} from "../../services/UserService";
import {Subscription} from "rxjs";
import {HotToastService} from "@ngneat/hot-toast";

@Component({
  selector: 'app-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.css']
})
export class DetailComponent implements OnInit, OnDestroy {

  caff?: CaffData
  caffId?: string
  newCommentText: string = "";

  commenting: boolean = false

  subscriptionGetCaff?: Subscription
  subscriptionComment?: Subscription

  constructor(private route: ActivatedRoute, private userService: UserService, private alertService: HotToastService) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(p => {
      this.caffId = p['id']
      this.getCaffData()
    })
  }

  getCaffData() {
    if (this.caffId) {
      this.subscriptionGetCaff = this.userService.getCaffById(this.caffId).subscribe(c => {
        this.caff = c
      })
    }
  }

  sendComment() {
    if (this.newCommentText !== "" && this.caffId) {
      this.commenting = true
      this.subscriptionComment = this.userService.sendComment(this.caffId, this.newCommentText).subscribe(res => {
        this.newCommentText = ""
        this.commenting = false
        this.getCaffData()
      })
    }
    else {
      this.alertService.warning("Comment text field must not be empty")
    }
  }

  ngOnDestroy(): void {
    this.subscriptionGetCaff?.unsubscribe()
    this.subscriptionComment?.unsubscribe()
  }
}
