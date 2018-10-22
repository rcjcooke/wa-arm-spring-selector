import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms'; // <-- NgModel lives here
import { HttpClientModule }    from '@angular/common/http';

// Materials imports
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material';
import { MatTableModule } from '@angular/material/table';
import { MatSortModule } from '@angular/material';
import { MatCardModule } from '@angular/material/card';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSliderModule } from '@angular/material/slider';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonToggleModule } from '@angular/material/button-toggle';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';

// Component imports
import { AppComponent } from './app.component';
import { ScenarioSelectorComponent } from './scenario-selector/scenario-selector.component';
import { SpringListComponent } from './spring-list/spring-list.component';
import { SpringSelectionVisualiserComponent } from './spring-selection-visualiser/spring-selection-visualiser.component';
import { SpringDetailComponent } from './spring-detail/spring-detail.component';
import { SpringDetailStaticComponent } from './spring-detail-static/spring-detail-static.component';
import { SpringDetailScenarioComponent } from './spring-detail-scenario/spring-detail-scenario.component';

@NgModule({
  declarations: [
    AppComponent,
    ScenarioSelectorComponent,
    SpringListComponent,
    SpringSelectionVisualiserComponent,
    SpringDetailComponent,
    SpringDetailStaticComponent,
    SpringDetailScenarioComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatFormFieldModule,
    MatInputModule,
    MatTableModule,
    MatSortModule,
    MatCardModule,
    MatCheckboxModule,
    MatTooltipModule,
    MatSliderModule,
    MatButtonToggleModule,
    MatSelectModule,
    MatSlideToggleModule,
    MatButtonModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
