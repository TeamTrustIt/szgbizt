import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ListUserComponent} from './list-user.component';
import {ButtonModule} from "../../components/button/button.module";


@NgModule({
  declarations: [
    ListUserComponent
  ],
  imports: [
    CommonModule,
    ButtonModule
  ]
})
export class ListUserModule {
}
