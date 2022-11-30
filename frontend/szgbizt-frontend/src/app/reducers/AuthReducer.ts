import {createReducer, on} from '@ngrx/store';
import * as AuthActions from '../actions/auth.actions'
import {AuthState} from "../interfaces/states/auth-state";


export const initialState: AuthState = {
  isLoggedIn: false,
  user: null,
}

export const AuthReducer = createReducer(
  initialState,

  on(AuthActions.login, (state, { user }) => (
    { ...state,
      isLoggedIn: true,
      user: user
    }
  )),

  on(AuthActions.logout, state => (
    { ...state,
      isLoggedIn: false,
      user: null
    }
  )),

)
