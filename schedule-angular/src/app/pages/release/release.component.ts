import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
import {NzButtonSize} from "ng-zorro-antd/button";
import {DomSanitizer} from "@angular/platform-browser";
import {ReleaseService} from "./release.service";
import {NzMessageService} from "ng-zorro-antd/message";
import {NzModalService} from 'ng-zorro-antd/modal';
import {LocalStorageService} from "ngx-webstorage";
import {MessageService, Msg} from "../message/message.service";
import {SALES_CATEGORY_L, SALES_CATEGORY_R, RELEASE_CATEGORY} from "../../app.constant";
import * as moment from "moment";

@Component({
  selector: 'app-release',
  templateUrl: './release.component.html',
  styleUrls: ['./release.component.scss']
})
export class ReleaseComponent implements OnInit {

  radioValue = 5;
  size: NzButtonSize = 'small';
  monthFormat = 'yyyy年MM月';

  makerList: any;
  carModelList: any;
  carModelGroupList: any;
  salesCategoryLList = SALES_CATEGORY_L;
  salesCategoryRList = SALES_CATEGORY_R;
  releaseCategoryList = RELEASE_CATEGORY;
  userList: any;
  masterScheduleList: any;

  makerCd: any;
  makerName: any;
  carModelCd: any;
  carModelName: any;
  carModelGroupCd: any;
  carModelGroupName: any;
  supportPeriod: any;
  salesCategoryL: any;
  salesCategoryR: any;
  releaseCategory: any;
  picId: any;
  checked = false;

  google_sheets_src: any;
  isFilter: any;

  message: any;

  constructor(private http: HttpClient,
              private router: Router,
              private modal: NzModalService,
              private nzMessage: NzMessageService,
              private messageService: MessageService,
              private releaseService: ReleaseService,
              private localStorage: LocalStorageService,
              private sanitizer: DomSanitizer) {

  }

  ngOnInit(): void {
    this.message = null;
    this.messageService.send(new Msg(this.radioValue));

    this.releaseService.searchInit().then(res => {
      this.makerList = res.result.makerList;
      this.userList = res.result.userList;
      this.masterScheduleList = res.result.masterScheduleList;
      this.releaseService.searchRelease(
        this.makerName,
        this.carModelName,
        this.carModelGroupName,
        this.supportPeriod,
        this.salesCategoryL,
        this.salesCategoryR,
        this.releaseCategory,
        this.picId,
        this.checked
      ).then(res => {
        if (res.result.listUrl.indexOf('fvid') > -1) {
          this.isFilter = true;
        } else {
          this.isFilter = false;
        }
        this.google_sheets_src = this.sanitizer.bypassSecurityTrustResourceUrl(res.result.listUrl);
      });
    });
  }

  changeMaker() {
    this.carModelCd = null;
    this.carModelGroupCd = null;
    if (this.makerCd) {
      this.releaseService.getCarModelList(this.makerCd).then(res => {
        this.carModelList = res.result.carModelList;
      });
    }
  }

  changeCarModel() {
    this.carModelGroupCd = null;
    if (this.carModelCd) {
      this.releaseService.getCarModelGroupList(this.makerCd, this.carModelCd).then(res => {
        this.carModelGroupList = res.result.carModelGroupList;
      });
    }
  }

  clear() {
    this.makerCd = null;
    this.carModelCd = null;
    this.carModelGroupCd = null;
    this.makerName = null;
    this.carModelName = null;
    this.carModelGroupName = null;
    this.supportPeriod = null;
    this.salesCategoryL = null;
    this.salesCategoryR = null;
    this.releaseCategory = null;
    this.picId = null;
    this.checked = false;
  }

  search() {
    const that = this;
    this.makerList.filter(function (item) {
      if (item.value == that.makerCd) {
        that.makerName = item.label;
      }
    });
    if (this.carModelList) {
      this.carModelList.filter(function (item) {
        if (item.value == that.carModelCd) {
          that.carModelName = item.label;
        }
      });
    }
    if (this.carModelGroupList) {
      this.carModelGroupList.filter(function (item) {
        if (item.value == that.carModelGroupCd) {
          that.carModelGroupName = item.label;
        }
      });
    }
    let supportPeriod;
    if (this.supportPeriod) {
      supportPeriod = moment(this.supportPeriod).format('YYYY/MM');
    }
    this.releaseService.searchRelease(
      this.makerName,
      this.carModelName,
      this.carModelGroupName,
      supportPeriod,
      this.salesCategoryL,
      this.salesCategoryR,
      this.releaseCategory,
      this.picId,
      this.checked
    ).then(res => {
      if (res.result.listUrl.indexOf('fvid') > -1) {
        this.isFilter = true;
      } else {
        this.isFilter = false;
      }
      this.google_sheets_src = this.sanitizer.bypassSecurityTrustResourceUrl(res.result.listUrl);
    });
  }

}
