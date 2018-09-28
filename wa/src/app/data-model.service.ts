import { Injectable } from '@angular/core';
import { BehaviorSubject, ReplaySubject } from 'rxjs';
import { Spring } from './spring';
import { Scenario } from './scenario';

@Injectable({
  providedIn: 'root'
})
export class DataModelService {

  private scenarioSource = new BehaviorSubject(new Scenario());
  scenario$ = this.scenarioSource.asObservable();

  private springsSource = new BehaviorSubject(<Spring[]>[]);
  springs$ = this.springsSource.asObservable();

  // Use ReplaySubject as we don't have (or want) an initial value
  private selectedSpringSource = new ReplaySubject<Spring>();
  selectedSpring$ = this.selectedSpringSource.asObservable();
  private selectedSpring: Spring;

  constructor() {
    this.selectedSpring$.subscribe(sp => {
      this.selectedSpring = sp;
    });
  }

  changeSprings(springs: Spring[], scenario: Scenario) {
    this.springsSource.next(springs);
    this.scenarioSource.next(scenario);
    if (this.selectedSpring) {
      // If there's a spring selected, refresh it if it's in the new spring set or remove it (push null) if it's not 
      var sp = springs.find(sp => sp.mOrderNum == this.selectedSpring.mOrderNum && sp.mManufacturer == this.selectedSpring.mManufacturer);
      this.selectedSpringSource.next(sp);
    }
  }

  changeSelectedSpring(spring: Spring) {
    console.log(spring);
    this.selectedSpringSource.next(spring);
  }

  getSpring(manufacturer: string, orderNum: string): Spring {
    return this.springsSource.getValue().find(spring => spring.mManufacturer == manufacturer && spring.mOrderNum == orderNum);
  }

}
