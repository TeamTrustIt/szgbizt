import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {UseHttpImageSourcePipe} from "./use-http-img-src.pipe";



@NgModule({
  declarations: [
    UseHttpImageSourcePipe
  ],
  imports: [
    CommonModule
  ],
  exports: [
    UseHttpImageSourcePipe
  ]
})
export class UseHttpImgSrcModule { }
