import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import * as moment from "moment";
import {OperateService} from "./operate.service";
import {ResponseEnum} from "../../response.enum";
import {NzMessageService} from "ng-zorro-antd/message";

@Component({
  selector: 'app-operate',
  templateUrl: './operate.component.html',
  styleUrls: ['./operate.component.scss']
})
export class OperateComponent implements OnInit {

  isVisible = true;
  sizeSpace = 8;

  // 年月度
  ym: any;
  // タスクID
  taskId: any;
  // タスク所在行数
  row: any;
  // タスク進捗状況の座標
  col0: any;
  // 実績開始日列の座標
  col1: any;
  // 実績終了日列の座標
  col2: any;
  // タスク担当者ID
  taskOwner: any;

  // 年月度
  ymShow: string;
  // 業務名
  workName: string;
  // タスク名
  taskName: string;
  // 作業時間
  workTimes: any;
  // 進捗状況
  workStatus: any;
  timeRecordId: any;
  finalChangeDate: any;
  message: any;

  constructor(private http: HttpClient,
              private router: Router,
              private route: ActivatedRoute,
              private nzMessage: NzMessageService,
              private operateService: OperateService) {

  }

  /**
   * 画面初期化
   */
  ngOnInit(): void {
    this.message = null;
    this.workTimes = '';
    this.ym = this.route.snapshot.queryParams.ym;
    this.taskId = this.route.snapshot.queryParams.taskID;
    this.row = this.route.snapshot.queryParams.row;
    this.col0 = this.route.snapshot.queryParams.col0;
    this.col1 = this.route.snapshot.queryParams.col1;
    this.col2 = this.route.snapshot.queryParams.col2;
    this.taskOwner = this.route.snapshot.queryParams.taskOwner;
    this.ymShow = moment(this.ym + '01').format('yyyy年MM月度');
    this.query();
  }

  query() {
    this.operateService.findWorkTime(this.ym, this.taskId, this.taskOwner).then(res => {
      this.workName = res.result.workName;
      this.taskName = res.result.taskName;
      this.workTimes = res.result.workTimes;
      this.workStatus = res.result.status;
      this.timeRecordId = res.result.timeRecordId;
      this.finalChangeDate = res.result.finalChangeDate;
      this.message = res.result.message_owner;
    });
  }

  updateStart(status) {
    let range = this.col0 + this.row + ':' + this.col2 + this.row;
    this.operateService.updateStart(range, this.ym, this.taskId, status, this.finalChangeDate).then(res => {
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

  updateTimeInfo(status) {
    let range = this.col1 + this.row + ':' + this.col2 + this.row;
    this.operateService.updateTimeInfo(range, this.ym, this.timeRecordId, status, this.finalChangeDate).then(res => {
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
