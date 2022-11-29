import {Component, OnDestroy, OnInit} from '@angular/core';
import {CaffData} from "../../interfaces/caff-data";
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../../services/UserService";
import {Subscription} from "rxjs";
import {HotToastService} from "@ngneat/hot-toast";
import {AuthState} from "../../interfaces/states/auth-state";
import {Store} from "@ngrx/store";
import {AdminService} from "../../services/AdminService";

@Component({
  selector: 'app-detail',
  templateUrl: './detail.component.html',
  styleUrls: ['./detail.component.css']
})
export class DetailComponent implements OnInit, OnDestroy {

  isAdmin: boolean = false;
  username: string = ""

  caff!: CaffData
  caffId?: string

  newCommentText: string = "";
  commenting: boolean = false
  buyingCaff: boolean = false
  deletingCaff: boolean = false

  subscriptionGetCaff?: Subscription
  subscriptionComment?: Subscription
  subscriptionBuyCaff?: Subscription
  subscriptionDeleteCaff?: Subscription

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService,
    private adminService: AdminService,
    private alertService: HotToastService,
    private store: Store<{ auth: AuthState }>) {
  }

  ngOnInit(): void {
    this.route.params.subscribe(p => {
      this.caffId = p['id']
      this.getCaffData()
    })
    this.store.subscribe(state => {
      this.isAdmin = state.auth.user?.roles === "ROLE_ADMIN"
      this.username = state.auth.user?.username ?? ""
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
    } else {
      this.alertService.warning("Comment text field must not be empty")
    }
  }

  buyCaff(id?: string) {
    // todo
    if (id) {
      this.buyingCaff = true
      this.subscriptionBuyCaff = this.userService.buyCaff(id).subscribe({
        next: (res) => {
          this.buyingCaff = false
          console.log(res)
        },
        complete: () => {
          this.buyingCaff = false
        }
      })
    }
  }

  deleteCaff(id: string | undefined, filename: string | undefined) {
    if (id && filename) {
      const sure = window.confirm(`Are you sure to delete ${filename}`)
      if (sure) {
        this.deletingCaff = true
        this.subscriptionDeleteCaff = this.adminService.deleteCaff(id).subscribe({
          next: (res) => {
            this.deletingCaff = false
            this.alertService.success(`${filename} successfully deleted`)
            this.router.navigateByUrl("/home")
          },
          complete: () => {
            this.deletingCaff = false
          }
        })
      }
    }
  }

  ngOnDestroy(): void {
    this.subscriptionGetCaff?.unsubscribe()
    this.subscriptionComment?.unsubscribe()
    this.subscriptionBuyCaff?.unsubscribe()
  }
}
