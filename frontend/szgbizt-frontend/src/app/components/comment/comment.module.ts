import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CommentComponent } from './comment.component';
import {ButtonModule} from "../button/button.module";



@NgModule({
    declarations: [
        CommentComponent
    ],
    exports: [
        CommentComponent
    ],
    imports: [
        CommonModule,
        ButtonModule
    ]
})
export class CommentModule { }
