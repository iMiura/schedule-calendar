import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {LoginComponent} from './pages/login/login.component';
import {OperateComponent} from "./pages/operate/operate.component";
import {NavbarComponent} from "./pages/layout/navbar/navbar.component";

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
    // 首页
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
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
