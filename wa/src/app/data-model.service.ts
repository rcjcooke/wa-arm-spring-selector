import { Injectable } from '@angular/core';
import { BehaviorSubject, ReplaySubject } from 'rxjs';
import { Spring } from './spring';

@Injectable({
  providedIn: 'root'
})
export class DataModelService {

  private springsSource = new BehaviorSubject(<Spring[]>[]);
  springs$ = this.springsSource.asObservable();

  // Use ReplaySubject as we don't have (or want) an initial value
  private selectedSpringSource = new ReplaySubject<Spring>();
  selectedSpring$ = this.selectedSpringSource.asObservable();

  constructor() { }

  changeSprings(springs: Spring[]) {
    this.springsSource.next(springs);
  }

  changeSelectedSpring(spring: Spring) {
    this.selectedSpringSource.next(spring);
  }

  getSpring(manufacturer: string, orderNum: string): Spring {
    return this.springsSource.getValue().find(spring => spring.mManufacturer == manufacturer && spring.mOrderNum == orderNum);
  }

}
