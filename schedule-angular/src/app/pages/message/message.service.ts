import {Observable, Subject} from 'rxjs';
import {EventEmitter, Injectable} from '@angular/core';

export interface IMsg {
  msg: string[];
  code: number;
  eventKey: string;
}

export class Msg implements IMsg {
  code: number;
  msg: string[];
  eventKey: string;

  constructor(code?: number, msg?: string[], eventKey?: string) {
    this.code = code;
    this.msg = msg;
    this.eventKey = eventKey;
  }
}

@Injectable({providedIn: 'root'})
export class MessageService {
  private subject = new EventEmitter<Msg>(true);

  send(message: Msg) {
    this.subject.next(message);
  }

  get(): Observable<Msg> {
    return this.subject.asObservable();
  }
}
