import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {NavbarComponent} from './navbar.component';
import {ButtonModule} from "../button/button.module";
import {AppRoutingModule} from "../../app-routing.module";


@NgModule({
  declarations: [
    NavbarComponent
  ],
  exports: [
    NavbarComponent
  ],
  imports: [
    CommonModule,
    ButtonModule,
    AppRoutingModule
  ]
})
export class NavbarModule {
}
