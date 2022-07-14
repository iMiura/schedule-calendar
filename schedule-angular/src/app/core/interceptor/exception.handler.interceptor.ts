import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Observable, throwError} from 'rxjs';
import {catchError, tap} from 'rxjs/operators';
import {NzNotificationService} from 'ng-zorro-antd/notification';
import {Injector} from '@angular/core';
import {Router} from '@angular/router';
import {PrincipalService} from '../auth/principal.service';
import {AuthJwtService} from '../auth/auth-jwt.service';

export class ExceptionHandlerInterceptor implements HttpInterceptor {

  ERR_RESPONSE = {
    '0': 'サーバからの応答がありません。しばらく時間を置いてから、再度操作していただけますようお願いいたします。',
    '500': '500：システムエラーが発生いたしました。システム管理者へご連絡してください。',
    '-999': '-999：システムエラーが発生いたしました。システム管理者へご連絡してください。'
  };

  constructor(private injector: Injector) {
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(req).pipe(
      tap(
        () => {
        },
        (err: any) => {
          if (err instanceof HttpErrorResponse) {
            if (err.status === 406) {
              return;
            }
            // 認証エラーの場合、トップページへ返す。
            if (this.is4xxClientError(err.status)) {
              this.handler4xxError(err);
            } else {
              this.notifyErrorToUser(err.status);
            }
          } else {
            this.notifyErrorToUser('-999');
          }
        }),
    );
  }

  private handler4xxError(err) {
    const authJwtService: AuthJwtService = this.injector.get(AuthJwtService);
    const principal: PrincipalService = this.injector.get(PrincipalService);
    // tokenのキャッシュクリア
    authJwtService.logout().subscribe();
    // 認証情報のクリア
    principal.logout();
    // ログイン画面へ画面遷移
    const router: Router = this.injector.get(Router);
    router.navigate(['/login'], {
      skipLocationChange: false,
    }).then(() => {
      if (err.status === 403) {
        const notification: NzNotificationService = this.injector.get(NzNotificationService);
        notification.create(
          'info',
          '',
          'ログインボタンでログインしてください。'
        );
      } else {
        // エラーメッセージ表示
        this.notifyErrorToUserWithContent(err.error.message);
      }
    });
  }

  private is4xxClientError(status): boolean {
    const seriesCode = Math.floor(status / 100);
    return seriesCode === 4;
  }

  private notifyErrorToUser(status) {
    let content = this.ERR_RESPONSE[status];
    if (!content) {
      content = '想定外のエラーが発生しました。エラーコード:' + status;
    }
    const notification: NzNotificationService = this.injector.get(NzNotificationService);
    notification.create(
      'error',
      'システムエラーが発生しました。',
      content
    );
  }

  private notifyErrorToUserWithContent(content) {
    const notification: NzNotificationService = this.injector.get(NzNotificationService);
    notification.create(
      'error',
      'システムエラーが発生しました。',
      content
    );
  }
}
