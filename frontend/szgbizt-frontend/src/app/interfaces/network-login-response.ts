import {UserLoginDto} from "./user-login-dto";

export interface NetworkLoginResponse {
  token: string,
  user: UserLoginDto
}
