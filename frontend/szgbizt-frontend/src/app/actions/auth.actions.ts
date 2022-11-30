import { createAction, props } from '@ngrx/store'
import {UserLoginDto} from "../interfaces/user-login-dto";

export const login = createAction(
    '[Auth] Login',
    props<{ user: UserLoginDto }>()
)

export const logout = createAction(
    '[Auth] Logout'
)
