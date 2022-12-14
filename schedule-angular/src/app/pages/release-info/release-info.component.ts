import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {ReleaseInfoService} from "./release-info.service";
import {ResponseEnum} from "../../response.enum";
import {NzMessageService} from "ng-zorro-antd/message";
import {RELEASE_CATEGORY, SALES_CATEGORY_L, SALES_CATEGORY_R} from "../../app.constant";
import {NzButtonSize} from "ng-zorro-antd/button";
import {FormBuilder, FormControl, FormGroup, Validators} from "@angular/forms";
import * as moment from "moment";

@Component({
  selector: 'app-release-info',
  templateUrl: './release-info.component.html',
  styleUrls: ['./release-info.component.scss']
})
export class ReleaseInfoComponent implements OnInit {

  validateForm!: FormGroup;
  dateFormat = 'yyyy/MM/dd';

  size: NzButtonSize = 'small';
  isVisible = true;
  sizeSpace = 8;

  makerList: any;
  carModelList: any;
  carModelGroupList: any;
  supportPeriodList: any;
  salesCategoryLList = SALES_CATEGORY_L;
  salesCategoryRList = SALES_CATEGORY_R;
  releaseCategoryList = RELEASE_CATEGORY;
  userList: any;

  releaseInfoId: any;
  finalChangeDate: any;
  deleteFlg: any;
  message: any;

  constructor(private http: HttpClient,
              private router: Router,
              private route: ActivatedRoute,
              private fb: FormBuilder,
              private nzMessage: NzMessageService,
              private releaseInfoService: ReleaseInfoService) {

  }

  /**
   * 画面初期化
   */
  ngOnInit(): void {
    this.message = null;
    this.validateForm = this.fb.group({
      releaseCategoryId: [null],
      releaseCategory: [null],
      nextConfirmDate: [null],
      announcementDate: [null, [Validators.required]],
      launchDate: [null, [Validators.required]],
      releaseUrl: [null],
      makerCd: [null, [Validators.required]],
      makerName: [null],
      carModelCd: [null, [Validators.required]],
      carModelName: [null],
      carModelGroupCd: [null, [Validators.required]],
      carModelGroupName: [null],
      tempChecked: [false],
      tempCarName: [null],
      tempCarGroupName: [null],
      salesCategoryIdL: [null, [this.salesCategoryLValidator]],
      salesCategoryL: [null],
      salesCategoryIdR: [null, [this.salesCategoryRValidator]],
      salesCategoryR: [null],
      salesCategoryNote: [null],
      supportPeriod: [null],
      picId: [null],
      userName: [null],
      note: [null, [Validators.maxLength(255)]],
    });
    this.releaseInfoId = this.route.snapshot.queryParams.release_info_id;
    if(!this.validateForm.value.tempChecked) {
      this.validateForm.controls.tempCarName.disable({onlySelf: true, emitEvent: true});
      this.validateForm.controls.tempCarGroupName.disable({onlySelf: true, emitEvent: true});
    }
    if (!this.releaseInfoId) {
      this.releaseInfoId = '';
    }
    this.query();
  }

  salesCategoryLValidator = (control: FormControl): { [s: string]: boolean } => {
    if (!control.value) {
      return;
    } else if (control.value == this.validateForm.controls.salesCategoryIdR.value) {
      return { error: true, same: true, required: false  };
    } else {
      this.validateForm.controls.salesCategoryIdR.setErrors(null);
    }
    return;
  };

  salesCategoryRValidator = (control: FormControl): { [s: string]: boolean } => {
    if (!control.value) {
      return;
    } else if (control.value == this.validateForm.controls.salesCategoryIdL.value) {
      return { error: true, same: true, required: false  };
    } else {
      this.validateForm.controls.salesCategoryIdL.setErrors(null);
    }
    return;
  };

  query() {
    this.releaseInfoService.findReleaseInfo(this.releaseInfoId).then(res => {
      this.makerList = res.result.makerList;
      this.supportPeriodList = res.result.supportPeriodList;
      this.userList = res.result.userList;
      this.finalChangeDate = res.result.finalChangeDate;
      if (this.finalChangeDate) {
        this.deleteFlg = res.result.releaseInfo.deleteFlg;
        let isChecked = false;
        if (res.result.releaseInfo.tempCarName || res.result.releaseInfo.tempCarGroupName) {
          isChecked = true;
        }
        let nextConfirmDate;
        if (res.result.releaseInfo.nextConfirmDate) {
          nextConfirmDate = moment(res.result.releaseInfo.nextConfirmDate).format('YYYY/MM/DD');
        }
        let supportPeriod;
        if (res.result.releaseInfo.supportPeriod) {
          supportPeriod = moment(res.result.releaseInfo.supportPeriod + '01').format('YYYY/MM');
        }
        let picId;
        if (res.result.releaseInfo.picId) {
          picId = res.result.releaseInfo.picId + '';
        }
        this.validateForm.patchValue({
          releaseCategoryId: res.result.releaseInfo.releaseCategory + '',
          nextConfirmDate: nextConfirmDate,
          announcementDate: moment(res.result.releaseInfo.announcementDate).format('YYYY/MM/DD'),
          launchDate: moment(res.result.releaseInfo.launchDate).format('YYYY/MM/DD'),
          releaseUrl: res.result.releaseInfo.releaseUrl,
          makerCd: res.result.releaseInfo.makerCd + '',
          carModelCd: res.result.releaseInfo.carModelCd + '',
          carModelGroupCd: res.result.releaseInfo.carModelGroupCd + '',
          tempChecked: isChecked,
          tempCarName: res.result.releaseInfo.tempCarName,
          tempCarGroupName: res.result.releaseInfo.tempCarGroupName,
          salesCategoryIdL: res.result.releaseInfo.salesCategoryL + '',
          salesCategoryIdR: res.result.releaseInfo.salesCategoryR + '',
          salesCategoryNote: res.result.releaseInfo.salesCategoryNote,
          supportPeriod: supportPeriod,
          picId: picId,
          note: res.result.releaseInfo.note,
        });
      }
    });
  }

  changeReleaseCategory() {
    if (this.validateForm.value.releaseCategoryId!=1) {
      this.validateForm.value.nextConfirmDate = null;
      this.validateForm.controls.nextConfirmDate.reset();
    }
  }

  changeMaker() {
    this.validateForm.value.carModelCd = null;
    this.validateForm.value.carModelGroupCd = null;
    this.validateForm.controls.carModelCd.reset();
    this.validateForm.controls.carModelGroupCd.reset();
    if (this.validateForm.value.makerCd) {
      this.releaseInfoService.getCarModelList(this.validateForm.value.makerCd).then(res => {
        this.carModelList = res.result.carModelList;
      });
    }
  }

  changeCarModel() {
    this.validateForm.value.carModelGroupCd = null;
    this.validateForm.controls.carModelGroupCd.reset();
    if (this.validateForm.value.carModelCd) {
      this.releaseInfoService.getCarModelGroupList(this.validateForm.value.makerCd, this.validateForm.value.carModelCd).then(res => {
        this.carModelGroupList = res.result.carModelGroupList;
      });
    }
  }

  changeSalesCategory() {
    this.validateForm.value.salesCategoryIdR = null;
    this.validateForm.controls.salesCategoryIdR.reset();
  }

  submitForm(): void {
    this.validateForm.controls.salesCategoryL.updateValueAndValidity({onlySelf: true});
    if (this.validateForm.valid) {
      const that = this;
      if (this.validateForm.value.releaseCategoryId) {
        this.validateForm.value.releaseCategory = this.releaseCategoryList[this.validateForm.value.releaseCategoryId].label;
      }
      this.makerList.filter(function (item) {
        if (item.value == that.validateForm.value.makerCd) {
          that.validateForm.value.makerName = item.label;
        }
      });
      this.carModelList.filter(function (item) {
        if (item.value == that.validateForm.value.carModelCd) {
          that.validateForm.value.carModelName = item.label;
        }
      });
      this.carModelGroupList.filter(function (item) {
        if (item.value == that.validateForm.value.carModelGroupCd) {
          that.validateForm.value.carModelGroupName = item.label;
        }
      });
      this.salesCategoryLList.filter(function (item) {
        if (item.value == that.validateForm.value.salesCategoryIdL) {
          that.validateForm.value.salesCategoryL = item.label;
        }
      });
      this.salesCategoryRList.filter(function (item) {
        if (item.value == that.validateForm.value.salesCategoryIdR) {
          that.validateForm.value.salesCategoryR = item.label;
        }
      });
      this.userList.filter(function (item) {
        if (item.value == that.validateForm.value.picId) {
          that.validateForm.value.userName = item.label;
        }
      });
      const body = JSON.parse(JSON.stringify(this.validateForm.value));
      if (this.validateForm.value.nextConfirmDate) {
        body.nextConfirmDate = moment(this.validateForm.value.nextConfirmDate).format('X');
      }
      if (this.validateForm.value.announcementDate) {
        body.announcementDate = moment(this.validateForm.value.announcementDate).format('X')
      }
      if (this.validateForm.value.launchDate) {
        body.launchDate = moment(this.validateForm.value.launchDate).format('X')
      }
      this.saveRelease(body);
    } else {
      Object.values(this.validateForm.controls).forEach(control => {
        if (control.invalid) {
          control.markAsDirty();
          control.updateValueAndValidity({ onlySelf: true });
        }
      });
    }
  }

  saveRelease(body) {
    this.releaseInfoService.saveRelease(body, this.releaseInfoId, this.finalChangeDate).then(res => {
      // 成功
      if (res.body.code === ResponseEnum.SUCCESS) {
        window.opener=null;
        window.open('','_self');
        window.close();
        return;
      }
      // 失敗
      if (res.body.code === ResponseEnum.FAILED) {
        this.nzMessage.error(res.body.msg);
        return;
      }
    });
  }

  delRelease() {
    this.releaseInfoService.delRelease(this.releaseInfoId, this.finalChangeDate).then(res => {
      // 成功
      if (res.body.code === ResponseEnum.SUCCESS) {
        window.opener=null;
        window.open('','_self');
        window.close();
        return;
      }
      // 失敗
      if (res.body.code === ResponseEnum.FAILED) {
        this.nzMessage.error(res.body.msg);
        return;
      }
    });
  }

  handleCancel(): void {
    window.opener=null;
    window.open('','_self');
    window.close();
  }

}
