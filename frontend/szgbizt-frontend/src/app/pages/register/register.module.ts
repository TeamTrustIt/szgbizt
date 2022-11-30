import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegisterComponent } from './register.component';
import {ButtonModule} from "../../components/button/button.module";
import {TextInputModule} from "../../components/text-input/text-input.module";
import {AppRoutingModule} from "../../app-routing.module";



@NgModule({
  declarations: [
    RegisterComponent
  ],
  imports: [
    CommonModule,
    ButtonModule,
    TextInputModule,
    AppRoutingModule
  ]
})
export class RegisterModule { }
