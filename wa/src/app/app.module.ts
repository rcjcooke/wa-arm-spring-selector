import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

// Materials imports
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material';

// Component imports
import { AppComponent } from './app.component';
import { ScenarioSelectorComponent } from './scenario-selector/scenario-selector.component';
// import { SpringListComponent } from './spring-list/spring-list.component';
// import { SpringSelectionVisualiserComponent } from './spring-selection-visualiser/spring-selection-visualiser.component';

@NgModule({
  declarations: [
    AppComponent,
    ScenarioSelectorComponent
    // SpringListComponent,
    // SpringSelectionVisualiserComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
