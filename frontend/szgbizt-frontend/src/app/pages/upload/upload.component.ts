import { Component } from '@angular/core';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css']
})
export class UploadComponent {

  file?: File
  name: string = ""
  description: string = ""
  error: string = ""

  upload() {
    this.error = ""
    if(this.file && this.name !== "" && this.description !== "") {
      const formData = new FormData()
      formData.append("caff", this.file)
      console.log(this.file)
      /*const upload$ = this.http.post("/api/thumbnail-upload", formData);
      upload$.subscribe();*/
    }
    else {
      this.error = "All fields are mandatory "
    }

  }

  setFile(event: any) {
    this.file = event.target.files[0]
  }
}
