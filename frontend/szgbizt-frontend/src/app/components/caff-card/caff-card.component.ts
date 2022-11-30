import {Component, EventEmitter, Input, Output} from '@angular/core';
import {CaffDataNoCommentDto} from "../../interfaces/caff-data-no-comment-dto";

@Component({
  selector: 'app-caff-card',
  templateUrl: './caff-card.component.html',
  styleUrls: ['./caff-card.component.css']
})
export class CaffCardComponent {

  @Input() classes: string = ''
  @Input() deleteEnabled: boolean = false
  @Input() caffData: CaffDataNoCommentDto = {
    username: "Author0",
    description: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut a velit ligula. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Cras felis sem, congue vitae aliquam eget, ultrices luctus justo. Nunc vitae libero at leo pellentesque volutpat ut id leo. Ut tincidunt lorem at tincidunt iaculis. Nulla at tempor nunc.",
    imageUrl: "assets/img/sample.jpg",
    id: "0",
    filename: "Caff0",
    price: 12,
    uploadDate: new Date(Date.now())
  }

  @Output() onDetailsClicked = new EventEmitter<string>()
  @Output() onDeleteClicked = new EventEmitter<string>()
}
