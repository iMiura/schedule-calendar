import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';

import {HomeRoutingModule} from './home-routing.module';
import {HomeComponent} from './home.component';
import {NzGridModule} from 'ng-zorro-antd/grid';
import {NzAvatarModule} from 'ng-zorro-antd/avatar';
import {NzBreadCrumbModule} from 'ng-zorro-antd/breadcrumb';
import {NzPageHeaderModule} from 'ng-zorro-antd/page-header';
import {IconsProviderModule} from '../../icons-provider.module';
import {NzToolTipModule} from 'ng-zorro-antd/tooltip';
import {NzCardModule} from 'ng-zorro-antd/card';
import {NzListModule} from 'ng-zorro-antd/list';
import {NzButtonModule} from "ng-zorro-antd/button";
import {NzRadioModule} from "ng-zorro-antd/radio";
import {NzFormModule} from 'ng-zorro-antd/form';
import {NzSpaceModule} from "ng-zorro-antd/space";
import {FormsModule} from "@angular/forms";
import {NzSelectModule} from "ng-zorro-antd/select";

@NgModule({
  declarations: [HomeComponent],
  imports: [
    CommonModule,
    HomeRoutingModule,
    IconsProviderModule,
    NzFormModule,
    FormsModule,
    NzButtonModule,
    NzRadioModule,
    NzSpaceModule,
    NzGridModule,
    NzAvatarModule,
    NzBreadCrumbModule,
    NzPageHeaderModule,
    NzToolTipModule,
    NzCardModule,
    NzListModule,
    NzSelectModule
  ]
})
export class HomeModule {
}
