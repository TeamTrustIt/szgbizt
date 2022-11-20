import {Injectable} from "@angular/core";
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {CaffDataNoCommentDto} from "../interfaces/caff-data-no-comment-dto";
import {Observable, of} from "rxjs";
import {UserEditDto} from "../interfaces/user-edit-dto";

@Injectable({providedIn: 'root'})
export class UserService {

  private baseUrl = environment.apiBaseUrl + "/user";

  constructor(private http: HttpClient) { }

  getCaffs(): Observable<CaffDataNoCommentDto[]> {
    const list: CaffDataNoCommentDto[] = []
    list.push({authorName: "Tester", description: "This is a test caff data", file: "assets/img/sample.jpg", id: 0, name: "TestCaff", price: 0.0, uploadDate: new Date(Date.now())})
    for(let c of [1,2,3,4,5,6,7,8,9,10]){
      list.push({authorName: "Tester", description: "This is a test caff data", file: "assets/img/samples.jpg", id: 0, name: "TestCaff"+c, price: 0.0, uploadDate: new Date(Date.now())})
    }
    return of(list)
  }

  getUserData(): Observable<UserEditDto> {
    const list: CaffDataNoCommentDto[] = []
    list.push({authorName: "Tester", description: "This is a test caff data", file: "assets/img/sample.jpg", id: 0, name: "TestCaff", price: 0.0, uploadDate: new Date(Date.now())})
    for(let c of [1,2,3]){
      list.push({authorName: "Tester", description: "This is a test caff data", file: "assets/img/samples.jpg", id: 0, name: "TestCaff"+c, price: 0.0, uploadDate: new Date(Date.now())})
    }
    const user: UserEditDto = {
      caffs: list, email: "bela@test.hu", id: 1, name: "Tester Béla", role: "user"
    }
    return of(user)
  }
// todo ng ondestroy unsubscribes
}
