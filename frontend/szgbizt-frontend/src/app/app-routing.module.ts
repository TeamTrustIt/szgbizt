import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from "./pages/login/login.component";
import {RegisterComponent} from "./pages/register/register.component";
import {ListUserComponent} from "./pages/list-user/list-user.component";
import {ListComponent} from "./pages/list/list.component";
import {DetailComponent} from "./pages/detail/detail.component";
import {ProfileComponent} from "./pages/profile/profile.component";
import {UploadComponent} from "./pages/upload/upload.component";
import {AuthGuard} from "./guards/auth.guard";
import {AdminGuard} from "./guards/admin.guard";

const routes: Routes = [
  {path: 'login', component: LoginComponent, canActivate: [AuthGuard, AdminGuard]},
  {path: 'register', component: RegisterComponent, canActivate: [AuthGuard, AdminGuard]},

  {path: 'admin-users', component: ListUserComponent, canActivate: [AuthGuard, AdminGuard]},

  {path: 'home', component: ListComponent, canActivate: [AuthGuard]},
  {path: 'detail/:id', component: DetailComponent, canActivate: [AuthGuard]},
  {path: 'profile', component: ProfileComponent, canActivate: [AuthGuard]},
  {path: 'upload', component: UploadComponent, canActivate: [AuthGuard]},

  {path: '**', redirectTo: '/login', pathMatch: 'full'},
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {
    scrollPositionRestoration: "disabled",
    onSameUrlNavigation: "ignore"
  })],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
