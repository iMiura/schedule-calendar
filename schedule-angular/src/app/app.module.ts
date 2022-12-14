import {BrowserModule} from '@angular/platform-browser';
import {Injector, NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {LoginComponent} from "./pages/login/login.component";
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {OperateComponent} from './pages/operate/operate.component';
import {NzModalModule, NzModalService} from 'ng-zorro-antd/modal';
import {OverlayModule} from '@angular/cdk/overlay';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {NzLayoutModule} from 'ng-zorro-antd/layout';
import {NzGridModule} from "ng-zorro-antd/grid";
import {NzFormModule} from "ng-zorro-antd/form";
import {NzInputModule} from "ng-zorro-antd/input";
import {NzButtonModule} from "ng-zorro-antd/button";
import {NzTypographyModule} from "ng-zorro-antd/typography";
import {NzResultModule} from "ng-zorro-antd/result";
import {NzCheckboxModule} from "ng-zorro-antd/checkbox";
import {NzStepsModule} from "ng-zorro-antd/steps";
import {NzSelectModule} from "ng-zorro-antd/select";
import {NzDropDownModule} from "ng-zorro-antd/dropdown";
import {IconsProviderModule} from './icons-provider.module';
import {NavbarComponent} from "./pages/layout/navbar/navbar.component";
import {NzRadioModule} from "ng-zorro-antd/radio";
import {NzSpaceModule} from "ng-zorro-antd/space";
import {NzMessageService} from "ng-zorro-antd/message";
import {AuthInterceptor} from "./core/interceptor/auth.interceptor";
import {LocalStorageService, NgxWebstorageModule, SessionStorageService} from "ngx-webstorage";
import {ExceptionHandlerInterceptor} from "./core/interceptor/exception.handler.interceptor";
import {NzNotificationService} from 'ng-zorro-antd/notification';
import {CredentialComponent} from "./pages/credential/credential.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {NzTableModule} from "ng-zorro-antd/table";
import {ReleaseComponent} from "./pages/release/release.component";
import {ReleaseInfoComponent} from "./pages/release-info/release-info.component";
import {NzDatePickerModule} from "ng-zorro-antd/date-picker";
import {registerLocaleData} from "@angular/common";
import ja from '@angular/common/locales/ja';
registerLocaleData(ja);
import {ja_JP, NZ_I18N} from "ng-zorro-antd/i18n";

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    NavbarComponent,
    OperateComponent,
    CredentialComponent,
    ReleaseComponent,
    ReleaseInfoComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    OverlayModule,
    BrowserAnimationsModule,
    NzGridModule,
    NzFormModule,
    NzInputModule,
    NzButtonModule,
    NzRadioModule,
    NzTypographyModule,
    NzStepsModule,
    NzResultModule,
    NzCheckboxModule,
    NzSelectModule,
    NzDropDownModule,
    NzLayoutModule,
    IconsProviderModule,
    NzModalModule,
    NzSpaceModule,
    NgxWebstorageModule.forRoot(),
    FormsModule,
    NzTableModule,
    NzDatePickerModule,
    ReactiveFormsModule,
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: AuthInterceptor,
      multi: true,
      deps: [LocalStorageService, SessionStorageService]
    },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ExceptionHandlerInterceptor,
      multi: true,
      deps: [Injector]
    },
    NzModalService,
    NzMessageService,
    NzNotificationService,
    {
      provide: NZ_I18N, useValue: ja_JP
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
