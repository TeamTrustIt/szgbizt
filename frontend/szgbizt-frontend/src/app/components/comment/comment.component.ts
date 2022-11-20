import {Component, Input} from '@angular/core';
import {CaffComment} from "../../interfaces/caffComment";

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.css']
})
export class CommentComponent {

  @Input() comment: CaffComment = {authorName: "Test Author", caffId: 0, id: 0, message: "Test Message", uploadDate: new Date(Date.now())}

}
