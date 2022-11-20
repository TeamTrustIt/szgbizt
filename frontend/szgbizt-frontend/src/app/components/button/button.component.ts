import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'app-button',
  templateUrl: './button.component.html',
  styleUrls: ['./button.component.css']
})
export class ButtonComponent {

  @Input() text: string = 'Button'
  @Input() type: 'default' | 'nav' | 'icon' | 'buy' = 'default'
  @Input() icon: 'delete' | 'add' = 'delete'
  @Input() classes: string = ''
  @Input() disabled: boolean = false

  @Output() onClick = new EventEmitter<unknown>()

  onClicked() {
    if(!this.disabled) {
      this.onClick.emit()
    }
  }
}
