import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {LoginComponent} from './login.component';
import {ButtonModule} from "../../components/button/button.module";
import {TextInputModule} from "../../components/text-input/text-input.module";
import {CaffCardModule} from "../../components/caff-card/caff-card.module";
import {CommentModule} from "../../components/comment/comment.module";
import {AppRoutingModule} from "../../app-routing.module";


@NgModule({
  declarations: [
    LoginComponent
  ],
  imports: [
    CommonModule,
    ButtonModule,
    TextInputModule,
    CaffCardModule,
    CommentModule,
    AppRoutingModule
  ]
})
export class LoginModule {
}
