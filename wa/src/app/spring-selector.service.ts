import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { Scenario } from './scenario';
import { Spring } from './spring';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class SpringSelectorService {

  // TODO: Make this configurable by build script later
  private springSelectorWSURL = 'http://localhost:8080/springselector/runscenario';

  constructor(
    private http: HttpClient
  ) { }

  findSprings(scenario: Scenario, returnAllSprings: boolean): Observable<Spring[]> {
    return this.http.post<Spring[]>(this.springSelectorWSURL + "?returnAllSprings=" + returnAllSprings, scenario, httpOptions);
  }

}
