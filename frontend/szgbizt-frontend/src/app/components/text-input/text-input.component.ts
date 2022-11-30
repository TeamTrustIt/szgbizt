import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'app-text-input',
  templateUrl: './text-input.component.html',
  styleUrls: ['./text-input.component.css']
})
export class TextInputComponent {

  @Input() classes: string = ''
  @Input() inputType: 'text' | 'password' | 'tel' | 'textArea' = 'text'
  @Input() value: string = ''
  @Input() placeholder: string = ''

  @Output() valueChange = new EventEmitter<string>()

}
