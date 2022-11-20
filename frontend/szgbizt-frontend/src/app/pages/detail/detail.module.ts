import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DetailComponent } from './detail.component';
import {CommentModule} from "../../components/comment/comment.module";
import {TextInputModule} from "../../components/text-input/text-input.module";
import {ButtonModule} from "../../components/button/button.module";



@NgModule({
  declarations: [
    DetailComponent
  ],
  imports: [
    CommonModule,
    CommentModule,
    TextInputModule,
    ButtonModule
  ]
})
export class DetailModule { }
