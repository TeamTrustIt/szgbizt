import {Component, OnDestroy, OnInit} from '@angular/core';
import {CaffData} from "../../interfaces/caff-data";
import {ActivatedRoute} from "@angular/router";
import {UserService} from "../../services/UserService";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.css']
})
export class DetailComponent implements OnInit, OnDestroy {

  caff?: CaffData
  subscription?: Subscription
  subscription2?: Subscription
  newCommentText: string = "";
  caffId?: number

  constructor(private route: ActivatedRoute, private userService: UserService) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(p => {
      this.caffId = p['id']
      this.getCaffData()
    })
  }

  getCaffData() {
    if (this.caffId) {
      this.subscription = this.userService.getCaffById(this.caffId).subscribe(c => {
        this.caff = c
      })
    }
  }

  sendComment() {
    if (this.newCommentText !== "" && this.caffId) {
      this.subscription2 = this.userService.sendComment(this.caffId, this.newCommentText).subscribe(res => {
        if(res.isSuccess){
          this.getCaffData()
        }
      })
    }
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe()
    this.subscription2?.unsubscribe()
  }
}
