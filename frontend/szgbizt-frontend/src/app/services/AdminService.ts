import {Injectable} from "@angular/core";
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {UserLoginDto} from "../interfaces/user-login-dto";
import {UserEditDto} from "../interfaces/user-edit-dto";
import {Store} from "@ngrx/store";
import {AuthState} from "../interfaces/states/auth-state";

@Injectable({providedIn: 'root'})
export class AdminService {

  private baseUrl = environment.apiBaseUrl + "/admin";

  private userId: string = ""

  constructor(private http: HttpClient, private store: Store<{ auth: AuthState }>) {
    this.store.subscribe(state => {
      this.userId = state.auth.user?.id ? state.auth.user.id : ''
    })
  }

  getUsers(): Observable<UserLoginDto[]> {
    return this.http.get<UserLoginDto[]>(`${this.baseUrl}/users`)
  }

  getUserDataById(id: string = this.userId): Observable<UserEditDto> {
    return this.http.get<UserEditDto>(`${this.baseUrl}/users/${id}`)
  }

  deleteUserById(id: string): Observable<unknown> {
    return this.http.delete<unknown>(`${this.baseUrl}/users/${id}`) // 204 on success
  }

  deleteCaff(id: string): Observable<unknown> {
    return this.http.delete<unknown>(`${this.baseUrl}/caff-data/${id}`) // 204 on success
  }

  deleteComment(id: string): Observable<unknown> {
    return this.http.delete<unknown>(`${this.baseUrl}/comments/${id}`) // 204 on success
  }

}
