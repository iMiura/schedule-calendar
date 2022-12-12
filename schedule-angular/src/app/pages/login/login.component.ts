import {Component, NgZone, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Router} from '@angular/router';
import {ResponseEnum} from "../../response.enum";
import {NzMessageService} from 'ng-zorro-antd/message';
import {LoginService} from "../../core/auth/login.service";
import {SERVER_API_URL} from "../../app.constant";

declare let google: any;
declare let returnCitySN: any;

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  constructor(private http: HttpClient,
              private router: Router,
              private message: NzMessageService,
              private loginService: LoginService,
              private ngZone: NgZone) {

  }

  ngOnInit(): void {
    const that = this;
    this.getClientId().then(res => {
      const CLIENT_ID = res.result.CLIENT_ID;
      google.accounts.id.initialize({
        client_id: CLIENT_ID,
        callback: function (response) {
          return that.login(response, that)
        }
      });
      google.accounts.id.renderButton(
        document.getElementById("buttonDiv"),
        {theme: "outline", size: "large"}
      );
      google.accounts.id.prompt();
    });
  }

  login(response, that) {
    // that.loginService.login(response.credential, returnCitySN["cip"], that.getBroswer()).then(data => {

    that.loginService.login(response.credential, '', that.getBroswer()).then(data => {
      // 認証失敗
      if (data.body.code === ResponseEnum.FAILED) {
        this.message.error(data.body.msg);
        return;
      }

      if (data.body.result.URL) {
        window.location.href=data.body.result.URL;
      } else {
        that.navigate(['/home'], {
          skipLocationChange: false,
        });

      }
    })
  }

  public navigate(commands: any[]): void {
    this.ngZone.run(() => this.router.navigate(commands)).then();
  }

  getClientId(): Promise<any> {
    return this.http.get(SERVER_API_URL + '/client', {params: {}}).toPromise();
  }

  private getBroswer(): string {
    let browser='';
    const ua = window.navigator.userAgent;
    if(ua.indexOf('Edge') != -1 || ua.indexOf('Edg') != -1) {
      browser = 'Microsoft Edge';
    } else if(ua.indexOf('Trident') != -1 || ua.indexOf('MSIE') != -1) {
      browser = 'Microsoft Internet Explorer';
    } else if(ua.indexOf('OPR') != -1 || ua.indexOf('Opera') != -1) {
      browser = 'Opera';
    } else if(ua.indexOf('Chrome') != -1) {
      browser = 'Google Chrome';
    } else if(ua.indexOf('Firefox') != -1) {
      browser = 'FireFox';
    } else if(ua.indexOf('Safari') != -1) {
      browser = 'Safari';
    } else {
      browser = 'Unknown';
    }

    return browser;
  }
}
