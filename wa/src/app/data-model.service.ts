import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Spring } from './spring';

@Injectable({
  providedIn: 'root'
})
export class DataModelService {

  private springsSource = new BehaviorSubject(<Spring[]>[]);
  springs = this.springsSource.asObservable();

  constructor() { }

  changeSprings(springs: Spring[]) {
    this.springsSource.next(springs);
  }
}
