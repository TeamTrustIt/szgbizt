import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ListComponent } from './list.component';
import {TextInputModule} from "../../components/text-input/text-input.module";
import {ButtonModule} from "../../components/button/button.module";
import {CaffCardModule} from "../../components/caff-card/caff-card.module";



@NgModule({
  declarations: [
    ListComponent
  ],
    imports: [
        CommonModule,
        TextInputModule,
        ButtonModule,
        CaffCardModule
    ]
})
export class ListModule { }
