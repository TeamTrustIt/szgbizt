import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {CaffComment} from "../../interfaces/caffComment";
import {UserService} from "../../services/UserService";
import {AdminService} from "../../services/AdminService";
import {Subscription} from "rxjs";
import {HotToastService} from "@ngneat/hot-toast";

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.css']
})
export class CommentComponent implements OnInit, OnDestroy{

  @Input() comment: CaffComment = {username: "tester", caffDataId: "0", id: "0", message: "Test Message", uploadDate: new Date(Date.now())}
  @Input() isAdmin: boolean = false
  @Input() username: string = "tester"

  @Output() onDeleted: EventEmitter<unknown> = new EventEmitter<unknown>()

  subscriptionDelete?: Subscription
  deleting: boolean = false

  constructor(private userService: UserService, private adminService: AdminService, private alert: HotToastService) {
  }

  ngOnInit(): void {
  }

  deleteComment(id: string) {
    const sure = window.confirm("Are you sure to delete this comment?")
    if(sure){
      this.deleting = true
      if(this.isAdmin){
        this.subscriptionDelete = this.adminService.deleteComment(id).subscribe({
          next: value => {
            this.alert.success("Comment deleted")
            this.deleting = false
            this.onDeleted.emit()
          },
          error: err => {
            this.deleting = false
          }
        })
      }
      else {
        this.subscriptionDelete = this.userService.deleteComment(id).subscribe({
          next: value => {
            this.alert.success("Comment deleted")
            this.deleting = false
            this.onDeleted.emit()
          },
          error: err => {
            this.deleting = false
          }
        })
      }
    }
  }

  ngOnDestroy(): void {
    this.subscriptionDelete?.unsubscribe()
  }

}
