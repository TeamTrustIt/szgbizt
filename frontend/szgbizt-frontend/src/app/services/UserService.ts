import {Injectable} from "@angular/core";
import {environment} from "../../environments/environment";
import {HttpClient} from "@angular/common/http";
import {CaffDataNoCommentDto} from "../interfaces/caff-data-no-comment-dto";
import {Observable} from "rxjs";
import {UserEditDto} from "../interfaces/user-edit-dto";
import {CaffData} from "../interfaces/caff-data";
import {CaffComment} from "../interfaces/caffComment";
import {AuthState} from "../interfaces/states/auth-state";
import {Store} from "@ngrx/store";

@Injectable({providedIn: 'root'})
export class UserService {

  private baseUrl = environment.apiBaseUrl;

  userId: string = ""

  constructor(private http: HttpClient, private store: Store<{ auth: AuthState }>) {
    this.store.subscribe(state => {
      this.userId = state.auth.user?.id ? state.auth.user.id : ''
    })
  }

  getCaffs(): Observable<CaffDataNoCommentDto[]> {
    return this.http.get<CaffDataNoCommentDto[]>(`${this.baseUrl}/caff-data`)
  }

  getUserDataById(id: string = this.userId): Observable<UserEditDto> {
    return this.http.get<UserEditDto>(`${this.baseUrl}/users/${id}`)
  }

  getCaffById(id: string): Observable<CaffData> {
    return this.http.get<CaffData>(`${this.baseUrl}/caff-data/${id}`)
  }

  sendComment(caffId: string, newCommentText: string): Observable<CaffComment> {
    const comment: { message: string } = {
      message: newCommentText
    }
    return this.http.post<CaffComment>(`${this.baseUrl}/caff-data/${caffId}/comments`, comment)
  }

  upload(data: FormData): Observable<CaffData> {
    return this.http.post<CaffData>(`${this.baseUrl}/caff-data`, data)
  }

  deleteUser(id: string = this.userId): Observable<unknown> {
    return this.http.delete<unknown>(`${this.baseUrl}/users/${id}`) //204 ha sikeres
  }

  deleteCaff(id: string): Observable<unknown> {
    return this.http.delete<unknown>(`${this.baseUrl}/caff-data/${id}`) //204 ha sikeres
  }

  deleteComment(id: string): Observable<unknown> {
    return this.http.delete<unknown>(`${this.baseUrl}/comments/${id}`) //204 ha sikeres
  }

  updatePassword(id: string, currentPassword: string, newPassword: string): Observable<unknown> {
    const data: { currentPassword: string, newPassword: string } = {
      currentPassword: currentPassword,
      newPassword: newPassword
    }
    return this.http.patch<unknown>(`${this.baseUrl}/users/${id}/password`, data) //200 ha sikeres
  }

  updateProfile(id: string, email: string, username: string): Observable<unknown> {
    const data: { email: string, username: string } = {
      email: email,
      username: username
    }
    return this.http.patch<unknown>(`${this.baseUrl}/users/${id}/profile`, data) //200 ha sikeres
  }

  buyCaff(id: string) {
    return this.http.get<string>(`${this.baseUrl}/caff-data/${id}/caff`)
  }
}
