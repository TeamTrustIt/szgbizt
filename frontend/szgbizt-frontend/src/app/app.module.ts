import {APP_INITIALIZER, NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HotToastModule} from '@ngneat/hot-toast';
import {StoreModule} from '@ngrx/store';
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";
import {AuthInterceptor} from "./interceptors/AuthInterceptor";
import {ErrorInterceptor} from "./interceptors/ErrorInterceptor";
import {UnauthorizedInterceptor} from "./interceptors/UnauthorizedInterceptor";
import {LoginModule} from "./pages/login/login.module";
import {NavbarModule} from "./components/navbar/navbar.module";
import {LoginAdminModule} from "./pages/login-admin/login-admin.module";
import {RegisterModule} from "./pages/register/register.module";
import {DetailModule} from "./pages/detail/detail.module";
import {ListModule} from "./pages/list/list.module";
import {ListUserModule} from "./pages/list-user/list-user.module";
import {UploadModule} from "./pages/upload/upload.module";
import {AuthReducer} from "./reducers/AuthReducer";
import {AuthService} from "./services/AuthService";
import {ProfileModule} from "./pages/profile/profile.module";
import {UseHttpImgSrcModule} from "./pipes/use-http-img-src/use-http-img-src.module";

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    HotToastModule.forRoot(),
    StoreModule.forRoot({
      auth: AuthReducer
    }, {}),
    LoginModule,
    NavbarModule,
    LoginAdminModule,
    RegisterModule,
    DetailModule,
    ListModule,
    ListUserModule,
    UploadModule,
    ProfileModule,
    UseHttpImgSrcModule
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true},
    {provide: HTTP_INTERCEPTORS, useClass: UnauthorizedInterceptor, multi: true},
    {provide: APP_INITIALIZER, useFactory: initializeAuth, deps: [AuthService], multi: true}
  ],
  exports: [
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}

function initializeAuth(authService: AuthService): Function {
  return () => new Promise<void>((resolve) => {
    const token = authService.getToken()
    if(token) {
      localStorage.clear()
      resolve()
    } else {
      resolve()
    }
  })
}
