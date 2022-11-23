import {Injectable} from "@angular/core";
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable, of} from "rxjs";
import {User} from "../interfaces/user";
import {NetworkResponse} from "../interfaces/network-response";

@Injectable({providedIn: 'root'})
export class AdminService {

  private baseUrl = environment.apiBaseUrl + "/admin";

  constructor(private http: HttpClient) { }


  public getUsers(): Observable<User[]> {
    const users = []
    for (let i of [1,2,3,4]){
      const user: User = {
        email: "tester1@tester.hu",
        role: "user",
        id: i,
        username: "tester" + i
      }
      users.push(user)
    }
    return of(users)

    //return this.http.get<User[]>(`${this.baseUrl}/users`)
  }

  deleteUser(id: number): Observable<NetworkResponse> {
    const res: NetworkResponse= {
      isSuccess: true,
      errorMessage: ""
    }
    return of(res)
    //return this.http.delete<NetworkResponse>(`${this.baseUrl}/users/${id}`)
  }
}
