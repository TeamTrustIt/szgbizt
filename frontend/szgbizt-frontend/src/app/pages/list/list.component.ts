import {Component, OnInit} from '@angular/core';
import {UserService} from "../../services/UserService";
import {CaffDataNoCommentDto} from "../../interfaces/caff-data-no-comment-dto";
import {map, Observable} from "rxjs";
import {Router} from "@angular/router";

@Component({
  selector: 'app-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.css']
})
export class ListComponent implements OnInit{

  caffs: CaffDataNoCommentDto[] = []
  caffsFiltered: CaffDataNoCommentDto[] = []
  searchText: string = ""

  constructor(private userService: UserService, private router: Router) {
  }

  ngOnInit(): void {
    this.userService.getCaffs().subscribe(res => {
      this.caffs = res
      this.caffsFiltered = this.caffs
    })
  }

  search() {
    this.caffsFiltered = this.caffs.filter(c => c.filename.toLowerCase().includes(this.searchText.toLowerCase()))
  }

  navigateToDetails(id: string) {
    this.router.navigateByUrl("/detail/" + id)
  }
}
