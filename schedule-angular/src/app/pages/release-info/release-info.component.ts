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
      releaseCategoryId: [null, [Validators.required]],
      releaseCategory: [null],
      nextConfirmDate: [null],
      announcementDate: [null, [Validators.required]],
      launchDate: [null, [Validators.required]],
      releaseUrl: [null, [Validators.maxLength(1000)]],
      makerCd: [null, [Validators.required]],
      makerName: [null],
      carModelCd: [null, [Validators.required]],
      carModelName: [null],
      carModelGroupCd: [null, [Validators.required]],
      carModelGroupName: [null],
      tempChecked: [false],
      tempCarName: [null, [Validators.required, Validators.maxLength(20)]],
      tempCarGroupName: [null, [Validators.required, Validators.maxLength(50)]],
      salesCategoryIdL: [null, [this.salesCategoryLValidator]],
      salesCategoryL: [null],
      salesCategoryIdR: [null, [this.salesCategoryRValidator]],
      salesCategoryR: [null],
      salesCategoryNote: [null, [Validators.maxLength(255)]],
      supportPeriod: [null, [Validators.required]],
      picId: [null],
      userName: [null],
      note: [null, [Validators.maxLength(255)]],
    });
    this.releaseInfoId = this.route.snapshot.queryParams.release_info_id;
    if (!this.releaseInfoId) {
      this.releaseInfoId = '';
    }
    this.query();
  }

  salesCategoryLValidator = (control: FormControl): { [s: string]: boolean } => {
    if (!control.value) {
      return;
    } else if (control.value == this.validateForm.value.salesCategoryIdR) {
      return { error: true, same: true, required: false  };
    } else {
      this.validateForm.controls.salesCategoryIdR.setErrors(null);
    }
    return;
  };

  salesCategoryRValidator = (control: FormControl): { [s: string]: boolean } => {
    if (!control.value) {
      return;
    } else if (control.value == this.validateForm.value.salesCategoryIdL) {
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
      this.formPatchValue(res.result.releaseInfo);
      this.disabledItem((res.result.releaseInfo))
    });
  }

  formPatchValue(releaseInfo) {
    if (this.finalChangeDate) {
      this.deleteFlg = releaseInfo.deleteFlg;
      let isChecked = false;
      if (releaseInfo.tempCarName || releaseInfo.tempCarGroupName) {
        isChecked = true;
      }
      let releaseCategoryId;
      if (releaseInfo.releaseCategory) {
        releaseCategoryId = releaseInfo.releaseCategory + '';
      }
      let nextConfirmDate;
      if (releaseInfo.nextConfirmDate) {
        nextConfirmDate = moment(releaseInfo.nextConfirmDate).format('YYYY/MM/DD');
      }
      let carModelCd;
      if (releaseInfo.carModelCd) {
        carModelCd = releaseInfo.carModelCd + '';
      }
      let carModelGroupCd;
      if (releaseInfo.carModelGroupCd) {
        carModelGroupCd = releaseInfo.carModelGroupCd + '';
      }
      let salesCategoryIdL;
      if (releaseInfo.salesCategoryL) {
        salesCategoryIdL = releaseInfo.salesCategoryL + '';
      }
      let salesCategoryIdR;
      if (releaseInfo.salesCategoryR) {
        salesCategoryIdR = releaseInfo.salesCategoryR + '';
      }
      let supportPeriod;
      if (releaseInfo.supportPeriod) {
        supportPeriod = moment(releaseInfo.supportPeriod + '01').format('YYYY/MM');
      }
      let picId;
      if (releaseInfo.picId) {
        picId = releaseInfo.picId + '';
      }
      this.validateForm.patchValue({
        releaseCategoryId: releaseCategoryId,
        nextConfirmDate: nextConfirmDate,
        announcementDate: moment(releaseInfo.announcementDate).format('YYYY/MM/DD'),
        launchDate: moment(releaseInfo.launchDate).format('YYYY/MM/DD'),
        releaseUrl: releaseInfo.releaseUrl,
        makerCd: releaseInfo.makerCd + '',
        carModelCd: carModelCd,
        carModelGroupCd: carModelGroupCd,
        tempChecked: isChecked,
        tempCarName: releaseInfo.tempCarName,
        tempCarGroupName: releaseInfo.tempCarGroupName,
        salesCategoryIdL: salesCategoryIdL,
        salesCategoryIdR: salesCategoryIdR,
        salesCategoryNote: releaseInfo.salesCategoryNote,
        supportPeriod: supportPeriod,
        picId: picId,
        note: releaseInfo.note,
      });
    }
  }

  disabledItem(releaseInfo) {
    if (this.finalChangeDate) {
      const salesCategoryIdL = releaseInfo.salesCategoryL;
      if (salesCategoryIdL == '0') {
        this.validateForm.get('tempCarName').disable();
        this.validateForm.get('carModelGroupCd').disable();
      } else if (salesCategoryIdL == '1') {
        this.validateForm.get('carModelCd').disable();
        this.validateForm.get('carModelGroupCd').disable();
      } else {
        this.validateForm.get('tempCarName').disable();
        this.validateForm.get('tempCarGroupName').disable();
      }
    } else {
      this.validateForm.get('carModelCd').disable();
      this.validateForm.get('carModelGroupCd').disable();
      this.validateForm.get('tempCarName').disable();
      this.validateForm.get('tempCarGroupName').disable();
    }
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
    this.validateForm.get('carModelCd').reset();
    this.validateForm.get('carModelGroupCd').reset();
    if (this.validateForm.value.makerCd) {
      this.releaseInfoService.getCarModelList(this.validateForm.value.makerCd).then(res => {
        this.carModelList = res.result.carModelList;
      });
      const salesCategoryIdL = this.validateForm.value.salesCategoryIdL;
      if (salesCategoryIdL != '1') {
        this.validateForm.get('carModelCd').enable();
      }
    } else {
      this.validateForm.get('carModelCd').disable();
      this.validateForm.get('carModelGroupCd').disable();
    }
  }

  changeCarModel() {
    this.validateForm.value.carModelGroupCd = null;
    this.validateForm.controls.carModelGroupCd.reset();
    if (this.validateForm.value.carModelCd) {
      this.releaseInfoService.getCarModelGroupList(this.validateForm.value.makerCd, this.validateForm.value.carModelCd).then(res => {
        this.carModelGroupList = res.result.carModelGroupList;
      });
      const salesCategoryIdL = this.validateForm.value.salesCategoryIdL;
      if (salesCategoryIdL != '0' && salesCategoryIdL != '1') {
        this.validateForm.get('carModelGroupCd').enable();
      } else {
        this.validateForm.get('carModelGroupCd').disable();
      }
    }
  }

  changeSalesCategory() {
    const salesCategoryIdL = this.validateForm.value.salesCategoryIdL;
    const makerCd = this.validateForm.value.makerCd;
    const carModelCd = this.validateForm.value.carModelCd;
    if (!salesCategoryIdL) {
      this.validateForm.value.salesCategoryIdR = null;
      this.validateForm.get('salesCategoryIdR').reset();
    }
    if (salesCategoryIdL == '0') {
      if (makerCd) {
        this.validateForm.get('carModelCd').enable();
      } else {
        this.validateForm.get('carModelCd').disable();
      }
      this.validateForm.get('carModelGroupCd').reset();
      this.validateForm.get('carModelGroupCd').disable();
      this.validateForm.get('tempCarName').reset();
      this.validateForm.get('tempCarName').disable();
      this.validateForm.get('tempCarGroupName').enable();
    } else if (salesCategoryIdL == '1') {
      this.validateForm.get('carModelCd').reset();
      this.validateForm.get('carModelCd').disable();
      this.validateForm.get('carModelGroupCd').reset();
      this.validateForm.get('carModelGroupCd').disable();
      this.validateForm.get('tempCarName').enable();
      this.validateForm.get('tempCarGroupName').enable();
    } else {
      if (makerCd) {
        this.validateForm.get('carModelCd').enable();
      } else {
        this.validateForm.get('carModelCd').disable();
      }
      if (carModelCd) {
        this.validateForm.get('carModelGroupCd').enable();
      } else {
        this.validateForm.get('carModelGroupCd').disable();
      }
      this.validateForm.get('tempCarName').reset();
      this.validateForm.get('tempCarName').disable();
      this.validateForm.get('tempCarGroupName').reset();
      this.validateForm.get('tempCarGroupName').disable();
    }
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
      if (this.carModelList) {
        this.carModelList.filter(function (item) {
          if (item.value == that.validateForm.value.carModelCd) {
            that.validateForm.value.carModelName = item.label;
          }
        });
      }
      if (this.carModelGroupList) {
        this.carModelGroupList.filter(function (item) {
          if (item.value == that.validateForm.value.carModelGroupCd) {
            that.validateForm.value.carModelGroupName = item.label;
          }
        });
      }
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
