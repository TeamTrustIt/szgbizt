import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginAdminComponent } from './login-admin.component';
import {TextInputModule} from "../../components/text-input/text-input.module";
import {ButtonModule} from "../../components/button/button.module";
import {AppRoutingModule} from "../../app-routing.module";



@NgModule({
  declarations: [
    LoginAdminComponent
  ],
  imports: [
    CommonModule,
    TextInputModule,
    ButtonModule,
    AppRoutingModule
  ]
})
export class LoginAdminModule { }
