import {NgModule} from '@angular/core';
import {NZ_ICONS, NzIconModule} from 'ng-zorro-antd/icon';

import {
  MenuFoldOutline,
  MenuUnfoldOutline,
  FormOutline,
  DashboardOutline,
  ProjectOutline, UploadOutline, QuestionCircleOutline, ControlOutline,
  AppstoreTwoTone, RobotOutline, ApiOutline, AreaChartOutline
} from '@ant-design/icons-angular/icons';

const icons = [MenuFoldOutline, MenuUnfoldOutline, DashboardOutline, FormOutline, ProjectOutline, UploadOutline, QuestionCircleOutline, ControlOutline, ProjectOutline,
  AppstoreTwoTone, RobotOutline, ApiOutline, AreaChartOutline];

@NgModule({
  imports: [NzIconModule],
  exports: [NzIconModule],
  providers: [
    {provide: NZ_ICONS, useValue: icons}
  ]
})
export class IconsProviderModule {
}
