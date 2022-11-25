import {Component, OnDestroy} from '@angular/core';
import {Subscription} from "rxjs";
import {UserService} from "../../services/UserService";
import {HotToastService} from "@ngneat/hot-toast";
import {Router} from "@angular/router";

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css']
})
export class UploadComponent implements OnDestroy {

  file?: File
  name: string = ""
  description: string = ""
  error: string = ""

  uploading: boolean = false
  subscription?: Subscription

  constructor(private userService: UserService, private alertService: HotToastService, private router: Router) {
  }

  upload() {
    this.error = ""
    if (this.file && this.name !== "" && this.description !== "") {
      this.uploading = true
      const formData = new FormData()
      formData.append("file", this.file)
      formData.append("description", this.description)
      formData.append("filename", this.name)
      this.subscription = this.userService.upload(formData).subscribe({
        next: (res) => {
          this.alertService.success(`${res.filename} was uploaded successfully`)
          this.router.navigateByUrl("/home")
        },
        complete: () => {
          this.uploading = false
        }
      })
    } else {
      this.error = "All fields are mandatory "
    }

  }

  setFile(event: any) {
    this.file = event.target.files[0]
  }

  ngOnDestroy(): void {
    this.subscription?.unsubscribe()
  }
}

