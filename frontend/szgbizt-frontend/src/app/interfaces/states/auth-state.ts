import {UserLoginDto} from "../user-login-dto";

export interface AuthState {
  isLoggedIn: boolean,
  user: UserLoginDto | null
}
