import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {LoginComponent} from './pages/login/login.component';
import {OperateComponent} from './pages/operate/operate.component';
import {NavbarComponent} from './pages/layout/navbar/navbar.component';
import {CredentialComponent} from "./pages/credential/credential.component";
import {ReleaseComponent} from "./pages/release/release.component";
import {ReleaseInfoComponent} from "./pages/release-info/release-info.component";

const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: '/home'
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    // TOP PAGE
    path: 'home',
    component: NavbarComponent,
    children: [
      {
        path: '',
        loadChildren: () => import('./pages/home/home.module').then(m => m.HomeModule),
      },
    ]
  },
  {
    path: 'operate',
    component: OperateComponent
  },
  {
    path: 'credential',
    component: CredentialComponent
  },
  {
    path: 'release',
    component: NavbarComponent,
    children: [
      {
        path: '',
        component: ReleaseComponent
      },
    ]
  },
  {
    path: 'release-info',
    component: ReleaseInfoComponent
  },
];

@NgModule({
  // ローカル実行時、コメントアウトを外す
  imports: [RouterModule.forRoot(routes)],
  // War作成時、コメントアウトを外す
  // imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
