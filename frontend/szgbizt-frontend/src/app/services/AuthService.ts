import {Injectable} from "@angular/core";
import {environment} from "../../environments/environment";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Store} from "@ngrx/store";
import {AuthState} from "../interfaces/states/auth-state";
import {login, logout} from "../actions/auth.actions";
import {UserLoginDto} from "../interfaces/user-login-dto";
import {Router} from "@angular/router";
import {Observable, tap} from "rxjs";
import {NetworkLoginResponse} from "../interfaces/network-login-response";
import {UserRegisterDto} from "../interfaces/user-register-dto";

@Injectable({providedIn: 'root'})
export class AuthService {

  private baseUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient, private store: Store<{ auth: AuthState }>, private router: Router) {
  }

  public login(username: string, password: string): Observable<NetworkLoginResponse> {

    const usernamePassword = `${username}:${password}`
    const encoded = btoa(usernamePassword)
    const authHeader = `Basic ${encoded}`
    const headers = new HttpHeaders()
      .set('Authorization', authHeader)
    return this.http.post<NetworkLoginResponse>(`${this.baseUrl}/login`, null, {headers: headers}).pipe(tap(res => {
      const token = res.token
      const user = res.user
      this.setToken(token)
      this.store.dispatch(login({user: user}))
      this.router.navigateByUrl("/home")
    }))
  }

  public register(email: string, password: string, username: string): Observable<UserLoginDto> {
    const regUser: UserRegisterDto = {
      email: email,
      password: password,
      username: username
    }
    return this.http.post<UserLoginDto>(`${this.baseUrl}/registration`, regUser)
  }

  public logout() {
    this.http.post<UserLoginDto>(`${this.baseUrl}/logout`, null).subscribe() // todo
    this.removeToken()
    this.store.dispatch(logout())
    this.router.navigateByUrl("/login")

  }

  setToken(token: string) {
    localStorage.setItem('auth-token', token)
  }

  getToken(): string | null {
    return localStorage.getItem('auth-token')
  }

  removeToken() {
    localStorage.removeItem('auth-token')
  }

}
