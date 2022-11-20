import {Component, Input} from '@angular/core';
import {Comment} from "../../interfaces/comment";

@Component({
  selector: 'app-comment',
  templateUrl: './comment.component.html',
  styleUrls: ['./comment.component.css']
})
export class CommentComponent {

  @Input() comment: Comment = {authorName: "Test Author", caffId: 0, id: 0, message: "Test Message", uploadDate: new Date(Date.now())}

}
