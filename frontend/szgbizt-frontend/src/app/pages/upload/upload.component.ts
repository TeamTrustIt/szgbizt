import {Component, OnDestroy} from '@angular/core';
import {Subscription} from "rxjs";
import {UserService} from "../../services/UserService";
import {HotToastService} from "@ngneat/hot-toast";

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

  subscription?: Subscription

  constructor(private userService: UserService, private alertService: HotToastService) {
  }

  upload() {
    this.error = ""
    if (this.file && this.name !== "" && this.description !== "") {
      const formData = new FormData()
      formData.append("file", this.file)
      formData.append("description", this.description)
      formData.append("filename", this.name)
      console.log(this.file)
      this.subscription = this.userService.upload(formData).subscribe(res => {
        this.alertService.success(`${res.filename} was uploaded successfully`)
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

