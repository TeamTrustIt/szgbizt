import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CaffCardComponent} from './caff-card.component';
import {ButtonModule} from "../button/button.module";
import {UseHttpImgSrcModule} from "../../pipes/use-http-img-src/use-http-img-src.module";



@NgModule({
  declarations: [
    CaffCardComponent
  ],
  exports: [
    CaffCardComponent
  ],
  imports: [
    CommonModule,
    ButtonModule,
    UseHttpImgSrcModule
  ]
})
export class CaffCardModule {
}
