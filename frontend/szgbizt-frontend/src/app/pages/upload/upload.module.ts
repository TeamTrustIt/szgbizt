import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {UploadComponent} from './upload.component';
import {TextInputModule} from "../../components/text-input/text-input.module";
import {ButtonModule} from "../../components/button/button.module";
import {FormsModule} from "@angular/forms";


@NgModule({
  declarations: [
    UploadComponent
  ],
  imports: [
    CommonModule,
    TextInputModule,
    ButtonModule,
    FormsModule
  ]
})
export class UploadModule {
}
