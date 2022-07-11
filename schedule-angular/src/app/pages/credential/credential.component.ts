import {Component, NgZone, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {CredentialService} from "./credential.service";

@Component({
  selector: 'app-operate',
  templateUrl: './credential.component.html',
})
export class CredentialComponent implements OnInit {

  code: any;

  constructor(private http: HttpClient,
              private router: Router,
              private route: ActivatedRoute,
              private ngZone: NgZone,
              private credentialService: CredentialService) {

  }

  /**
   * 画面初期化
   */
  ngOnInit(): void {
    this.code = this.route.snapshot.queryParams.code;
    this.query();
  }

  query() {
    this.credentialService.getCredential(this.code).then(res => {
      this.navigate(['/home']);
    });
  }

  public navigate(commands: any[]): void {
    this.ngZone.run(() => this.router.navigate(commands)).then();
  }

}
