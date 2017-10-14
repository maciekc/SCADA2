import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'SCADA';

  public changeCSS(event) {
    let childrens = document.getElementById('menuList').children;
    for (let i = 0; i < childrens.length; i++) {
      childrens[i].setAttribute('class', 'na-item');
    }
    let e = event.target;
    e.parentElement.setAttribute('class', 'nav-item active');
    console.log(e);
  }
}
