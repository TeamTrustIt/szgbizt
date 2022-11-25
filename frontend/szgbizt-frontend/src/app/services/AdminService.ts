import {Injectable} from "@angular/core";
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {UserLoginDto} from "../interfaces/user-login-dto";

@Injectable({providedIn: 'root'})
export class AdminService {

  private baseUrl = environment.apiBaseUrl + "/admin";

  constructor(private http: HttpClient) { }

  getUsers(): Observable<UserLoginDto[]> {
    return this.http.get<UserLoginDto[]>(`${this.baseUrl}/users`)
  }

  deleteUserById(id: string): Observable<unknown> {
    return this.http.delete<unknown>(`${this.baseUrl}/users/${id}`) //204 ha sikeres
  }

  deleteCaff(id: string): Observable<unknown> {
    return this.http.delete<unknown>(`${this.baseUrl}/caff-data/${id}`) //204 ha sikeres
  }

  deleteComment(id: string): Observable<unknown> {
    return this.http.delete<unknown>(`${this.baseUrl}/comments/${id}`) //204 ha sikeres
  }

}
