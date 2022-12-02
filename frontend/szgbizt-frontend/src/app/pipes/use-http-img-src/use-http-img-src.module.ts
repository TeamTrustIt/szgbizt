import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {UseHttpImgSourcePipe} from "./use-http-img-src.pipe";

@NgModule({
  declarations: [
    UseHttpImgSourcePipe
  ],
  imports: [
    CommonModule
  ],
  exports: [
    UseHttpImgSourcePipe
  ]
})
export class UseHttpImgSrcModule {
}
