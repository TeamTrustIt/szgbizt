import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CaffCardComponent } from './caff-card.component';
import {ButtonModule} from "../button/button.module";



@NgModule({
    declarations: [
        CaffCardComponent
    ],
    exports: [
        CaffCardComponent
    ],
    imports: [
        CommonModule,
        ButtonModule
    ]
})
export class CaffCardModule { }
