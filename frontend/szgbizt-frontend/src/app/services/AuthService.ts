import {Injectable} from "@angular/core";
import {environment} from "../../environments/environment";
import {HttpClient, HttpHeaders, HttpParamsOptions} from "@angular/common/http";
import {Store} from "@ngrx/store";
import {AuthState} from "../interfaces/states/auth-state";
import {login, logout } from "../actions/auth.actions";
import {UserLoginDto} from "../interfaces/user-login-dto";
import {Router} from "@angular/router";
import {NetworkResponse} from "../interfaces/network-response";

@Injectable({providedIn: 'root'})
export class AuthService {

  private baseUrl = environment.apiBaseUrl + "/auth";

  constructor(private http: HttpClient, private store: Store<{auth: AuthState}> , private router: Router) { }

  public adminLogin(email: string, password: string) /*: Observable<NetworkResponse>*/ {
    const user: UserLoginDto = {
      email: email, id: 0, name: "AdminTester", role: "admin"

    }
    this.setToken("mock-admin")
    this.store.dispatch(login({user: user}))
    this.router.navigateByUrl("/home")
    //return this.http.post<NetworkResponse>(`${this.baseUrl}/login`, {email: email, password: password})
  }

  public login(username: string, password: string) /*: Observable<NetworkResponse>*/ {
    const user: UserLoginDto = {
      email: "t@t.hu", id: 1, name: username, role: "user"
    }
    this.setToken("mock")
    this.store.dispatch(login({user: user}))
    this.router.navigateByUrl("/home")

    // implemented:
    const usernamePassword = `${username}:${password}`
    const encoded = btoa(usernamePassword)
    const authHeader = `Basic ${encoded}`
    const headers = new HttpHeaders()
      .set('Authorization', authHeader)
    //return this.http.post<NetworkResponse>(`${this.baseUrl}/login`,null, {headers: headers})
  }

  public register(email: string, password: string) /*Observable<unknown>*/ {
    /*return this.http.post<unknown>(`${this.baseUrl}/register`, {email: email, password: password})*/
  }

  public logout() {
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
