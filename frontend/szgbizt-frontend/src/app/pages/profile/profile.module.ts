import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProfileComponent } from './profile.component';
import {TextInputModule} from "../../components/text-input/text-input.module";
import {CaffCardModule} from "../../components/caff-card/caff-card.module";
import {ButtonModule} from "../../components/button/button.module";
import {AppRoutingModule} from "../../app-routing.module";



@NgModule({
  declarations: [
    ProfileComponent
  ],
    imports: [
        CommonModule,
        TextInputModule,
        CaffCardModule,
        ButtonModule,
        AppRoutingModule
    ]
})
export class ProfileModule { }
