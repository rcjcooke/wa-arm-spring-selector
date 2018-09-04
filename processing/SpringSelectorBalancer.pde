// ***** Spring Selector ***** NRL ----  Frank Beinersdorf 2013/14 *****

import peasy.*; //PeasyCam
import processing.pdf.*;
import java.util.Vector;
import java.awt.Frame; //needed for external ControlFrame
import java.awt.BorderLayout; //needed for external ControlFrame
import controlP5.*; //GUI and controller library

// ***** Begin Evil globals *****

//parameter of the system to be balanced
float gravity = 0.00980665; //graviational accelleration [km/(s*s)]
float mass = 1000; //system mass + payload mass [g]
float R1 = 1000; //Lever => distance CoM to Pivot [mm]

//constrains for the balancing system
float[] AllowedRangeR2 = {100,200}; //distance spring connection on lever to Pivot [mm]
float[] AllowedRangeA = {100,200}; //distance spring connection on fixxed y-Axis to Pivot [mm]
int NumberOfParallelSprings=1;      //Number of equivalent parallel springs
int MaxNumberOfParallelSprings=20;
float MechanicalAdvantageZaehler =1; //
float MaxMechanicalAdvantageZaehler=10;
float MechanicalAdvantageNenner =1; //
float MaxMechanicalAdvantageNenner =10; //
float MechanicalAdvantage =1;      //Mechanical advantage = MechanicalAdvantageZaehler/MechanicalAdvantageNenner

//Scaled mass and scaled Allowed Ranges R2 and A
float mass_sc = 0;
float[] AllowedRangeR2_sc ={0,0};
float[] AllowedRangeA_sc ={0,0};

//
float MassPotentialEnergy = 0; //Maximum Potential Energy of the mass in the above System // calculation in draw()
float[] AllowedRangeLr = {0,0};
float[] AllowedRangeFn = {0,0};

float[] TheoR2 = {0,0};
float[] TheoA = {0,0};

float[] FinalR2 = {0,0};
float[] FinalA = {0,0};

//***********************
String DataSet = "Databases/Federndatenbank2.csv";
//***********************

int WorkContext =1;

PImage System;

PeasyCam cam; //PeasyCam
PrintWriter GnuPlotOutput; //Writer for GnuPlotComandFileCreation
Table TableSpringParameters; //Table to hold SpringParamters imported from *.csv-file
Table TableSpringsBalancingSelection; //Table to hold SpringParamters imported from *.csv-file

ArrayList<SpringParameter> SpringParameterList; //dynamic list of SpringParameters

ControlFrame cf; //Control Frame
ControlP5 cp5; //ControlP5 System

float MinimumMass = 1000000000;
int CounterX =0;
int NumberOfSelectedSprings=0;
int SelectionCounter=0;

ArrayList<ScatterPlot> All2DPlots;
ArrayList<ScatterPlot> All3DPlots;

SpringCharacteristics TestCharacteristicsPlot;

boolean PlotMinimumMass = false;
boolean FindAllPossibleSprings =false;
boolean ExportVariations = false;

// ***** End Evil globals *****
//Begin setup *******************************************************************************************************************************************************************************

void setup() {
  size(1000,800,P3D); smooth();
  System = loadImage("System.png");
  cam = new PeasyCam(this, width/2,-height/2+150,0,width); //Camaera Object PeasyCam
  cp5 = new ControlP5(this); //Create ControlP5 System
  
  SpringParameterList = new ArrayList<SpringParameter>();  // Create empty ArrayList for SpringParameters
  TableSpringParameters = loadTable(DataSet, "header");//GutekunstFull.csv //Load Springparameters from *.csv to Table Object TableSpringParameters
  TableSpringsBalancingSelection = new Table(); //Create a table that holds the springs, that comply with the balancing condition for the current system
  TableSpringsBalancingSelection.addColumn("Bestellnummer");
  TableSpringsBalancingSelection.addColumn("SelectedMechanicalAdvantageZaehler");
  TableSpringsBalancingSelection.addColumn("SelectedMechanicalAdvantageNenner");
  TableSpringsBalancingSelection.addColumn("SelectedMechanicalAdvantage");
  TableSpringsBalancingSelection.addColumn("SelectedNumberOfParallelSprings");
  TableSpringsBalancingSelection.addColumn("R2min");
  TableSpringsBalancingSelection.addColumn("R2max");
  TableSpringsBalancingSelection.addColumn("Amin");
  TableSpringsBalancingSelection.addColumn("Amax");
  TableSpringsBalancingSelection.addColumn("A*R2");
  TableSpringsBalancingSelection.addColumn("OverallMass");
  TableSpringsBalancingSelection.addColumn("SpringLength");
  
  All2DPlots = new ArrayList<ScatterPlot>();
  All3DPlots = new ArrayList<ScatterPlot>();

  //get all Parameternames(types) in TableSpringParameters and collect them in SpringParameterList
  for (int i = 0;i<TableSpringParameters.getColumnCount();i++) {
    SpringParameterList.add(new SpringParameter(TableSpringParameters.getColumnTitle(i)));  // Start by adding one element
  }
  //set ColumnType (eg. 0 Table.STRING, 1 Table.INT, 2 Table.LONG, 3 Table.FLOAT,4  Table.DOUBLE) for each column based on values in first row
  for (int i = 0;i < SpringParameterList.size();i++) {
    if (Float.isNaN(TableSpringParameters.getFloat(0,SpringParameterList.get(i).ParameterName))) {
      SpringParameterList.get(i).setTypeString();
    } else {
      SpringParameterList.get(i).setTypeFloat();
    }
  }
  //Find the Min and Max values of each FLOAT Spring Parameter given in the SpringparameterTable
  for (int i = 0;i < SpringParameterList.size();i++) {
    if(SpringParameterList.get(i).isTypeFloat()){
      SpringParameterList.get(i).Min = min(TableSpringParameters.getFloatColumn(SpringParameterList.get(i).ParameterName));
      SpringParameterList.get(i).Max = max(TableSpringParameters.getFloatColumn(SpringParameterList.get(i).ParameterName));
      print(i + " " + SpringParameterList.get(i).ParameterName + ": ");
      println("MIN = " + SpringParameterList.get(i).Min + " Max = " + SpringParameterList.get(i).Max);
    }
  }
  
  //find the Values of each STRING Spring Parameter given in the SpringparameterTable
  for (int i = 0;i < SpringParameterList.size();i++) {
    if(SpringParameterList.get(i).isTypeString()){
      SpringParameterList.get(i).setListOfStringParameterOptions(TableSpringParameters.getStringColumn(SpringParameterList.get(i).ParameterName));
      print(i + " " + SpringParameterList.get(i).ParameterName + ": ");
      println(SpringParameterList.get(i).ListOfStringParameterOptions.size());
    }
  }
  
  //Make ControlWindow  
  cf = addControlFrame("Parameter Selector -- Basic Mass Spring Balancer Calculator",SpringParameterList, 1500,470); //Add a new Control frame to ControlP5 System
  
  //Add a column Selected acting as a filter flag
  TableSpringParameters.addColumn("Selected",Table.INT);
  
  //Add a column "R2min" ,"R2max", "Amin", "Amax" for selected Spring Setup
  TableSpringParameters.addColumn("R2min",Table.FLOAT);
  TableSpringParameters.addColumn("R2max",Table.FLOAT);
  TableSpringParameters.addColumn("Amin",Table.FLOAT);
  TableSpringParameters.addColumn("Amax",Table.FLOAT);
  
  //Initialize "Selected" column
  for (TableRow row : TableSpringParameters.rows()) { 
    row.setInt("Selected", 1);
  }
  
  //2D Scatter-Plots added to ArrayList All2DPlots
  for (SpringParameter P  : SpringParameterList) {
    if (P.isTypeFloat()){
      for (SpringParameter Q  : SpringParameterList) {
        if (Q.isTypeFloat()){
          All2DPlots.add(new ScatterPlot(P,Q));
        }
      }
    }
  }
  
  //3D Scatter-Plots added to ArrayList All3DPlots
  for (SpringParameter P  : SpringParameterList) {
    if (P.isTypeFloat()){
      for (SpringParameter Q  : SpringParameterList) {
        if (Q.isTypeFloat()){
           for (SpringParameter R  : SpringParameterList) {
              if (R.isTypeFloat()){
                All3DPlots.add(new ScatterPlot(Q,R,P));
              }
           }
        }
      }
    }
  }
  
  //set Diagram Parameters 2D 
  for(ScatterPlot h : All2DPlots )
    {
      h.setTable(TableSpringParameters).setColor(color(255,0,0)).setPointSize(3).setLabelSize(15).setPlotOnlySelected(true).setOriginXYZ(75,75,0).setMaxXYZAxis(width-150,height-150,height-150);
    }
    
  //set Diagram Parameters 3D  
  for(ScatterPlot h : All3DPlots )
    {
      h.setTable(TableSpringParameters).setColor(color(255,0,0)).setPointSize(3).setLabelSize(15).setPlotOnlySelected(true).setOriginXYZ(75,75,0).setMaxXYZAxis(width-150,height-150,height-150);
    }
  
  TestCharacteristicsPlot = new SpringCharacteristics(0,0,0); 
  ortho(); //Start in ortho view mode
}

//End setup *******************************************************************************************************************************************************************************

//Begin draw *******************************************************************************************************************************************************************************
void draw() {
  background(0);
  
 //Begin Selection Process *******************************************************************************************************************************************************************************  
  RunSelection();
  
  if(FindAllPossibleSprings){
    cf.console.clear();  
    println("SEARCHING, please wait !");
    float OldMinimumMass = 100000000;
    TableSpringsBalancingSelection.clearRows();
    for(int u=2; u<=(int)MaxNumberOfParallelSprings; u=u+2){
      cf.cp5.getController("Anzahl paralleler Federn").setValue(int(u));
      for(int v=1; v<=MaxMechanicalAdvantageZaehler; v++){
        cf.cp5.getController("Uebersetzungsverhaeltnis Zaehler").setValue(v);
        for(int w=1; w<=MaxMechanicalAdvantageNenner; w++){
          cf.cp5.getController("Uebersetzungsverhaeltnis Nenner").setValue(w);
          NumberOfParallelSprings = u;
          MechanicalAdvantageZaehler = v;
          MechanicalAdvantageNenner = w;
          MinimumMass = 1000000000;
          RunSelection();
          //println("*");
          //Output Spring with minimum Mass
          for(TableRow row : TableSpringParameters.rows()){
            if((row.getFloat("Masse") == MinimumMass) && (row.getInt("Selected")==1)){
              if((MinimumMass*NumberOfParallelSprings)<= OldMinimumMass){
                println(row.getString("Bestellnummer") + " MA= " + MechanicalAdvantageZaehler+ "/" + MechanicalAdvantageNenner +" N= " + NumberOfParallelSprings +" m= " + (row.getFloat("Masse")*NumberOfParallelSprings));
                //OldMinimumMass = MinimumMass * NumberOfParallelSprings;
                TableRow arow = TableSpringsBalancingSelection.addRow();
                arow.setString("Bestellnummer",row.getString("Bestellnummer"));
                arow.setFloat("SelectedMechanicalAdvantageZaehler",MechanicalAdvantageZaehler);
                arow.setFloat("SelectedMechanicalAdvantageNenner",MechanicalAdvantageNenner);
                arow.setFloat("SelectedMechanicalAdvantage",MechanicalAdvantageZaehler/MechanicalAdvantageNenner);
                arow.setFloat("SelectedNumberOfParallelSprings",NumberOfParallelSprings);
                arow.setFloat("OverallMass", NumberOfParallelSprings*row.getFloat("Masse"));
                arow.setFloat("SpringLength", row.getFloat("Ln[mm] Maximal zulaessige Laenge"));
                arow.setFloat("R2min", row.getFloat("R2min"));
                arow.setFloat("R2max", row.getFloat("R2max"));
                arow.setFloat("Amin", row.getFloat("Amin"));
                arow.setFloat("Amax", row.getFloat("Amax"));
                arow.setFloat("A*R2", (mass*gravity*R1/row.getFloat("k[N/mm] Federrate")*MechanicalAdvantage*MechanicalAdvantage /NumberOfParallelSprings));
              }
            }
          }          
        }
      }
    }
    
    //delete equivalent spring combinations (mechanical advantages like 1/1 3/3 4/4 ... are equivalent)
        //TODO
        
    FindAllPossibleSprings =false;
    println("Number of Solutions: " + TableSpringsBalancingSelection.getRowCount());
//    for(TableRow row : TableSpringsBalancingSelection.rows()){
//      //println(row.getString("Bestellnummer") + " " + row.getFloat("SelectedMechanicalAdvantageZaehler")+ " " + row.getFloat("SelectedMechanicalAdvantageNenner")+ " " + row.getFloat("SelectedNumberOfParallelSprings") + " " + row.getFloat("OverallMass") );
//    }
    cf.cp5.getController("Anzahl paralleler Federn").setValue(1);
    cf.cp5.getController("Uebersetzungsverhaeltnis Zaehler").setValue(1);
    cf.cp5.getController("Uebersetzungsverhaeltnis Nenner").setValue(1);
    if(TableSpringsBalancingSelection.getRowCount()>0) {
      cf.cp5.getController("Exportiere Variationen nach *.csv").show();
      cf.cp5.getController("Exportiere Variationen nach *.csv").setBroadcast(true);
    } else {
      cf.cp5.getController("Exportiere Variationen nach *.csv").setBroadcast(false);
      cf.cp5.getController("Exportiere Variationen nach *.csv").hide();
    }
  }
 //End Selection Process *******************************************************************************************************************************************************************************
  
  //export Variations to csv ?? 
  if(ExportVariations){
    cf.cp5.getController("Exportiere Variationen nach *.csv").setBroadcast(false);
    cf.cp5.getController("Exportiere Variationen nach *.csv").hide();
    println("Write Variations to: data/Selection/"+str(year())+nf(month(),2)+nf(day(),2)+"_"+nf(hour(),2)+nf(minute(),2)+nf(second(),2)+"_SpringSelection.csv");
    saveTable(TableSpringsBalancingSelection, "data/Selection/"+str(year())+nf(month(),2)+nf(day(),2)+"_"+nf(hour(),2)+nf(minute(),2)+nf(second(),2)+"_SpringSelection.csv");
    println("DONE");
    ExportVariations=false;
  }
    
  //draw selected Diagram Parameters
  if(WorkContext==1){
    All3DPlots.get(CounterX).setPlotOnlySelected(false).setDrawLimitGraphs(true).setDrawAxis(true).setPointSize(3).setBrightness(50).draw();
    All3DPlots.get(CounterX).setPlotOnlySelected(true).setDrawLimitGraphs(true).setDrawAxis(false).setPointSize(4).setBrightness(255).drawMinimumMass(MinimumMass).draw(); MinimumMass = 1000000000;
  } else if(WorkContext == 2){  
   All3DPlots.get(CounterX).setPlotOnlySelected(false).setDrawAxis(true).setPointSize(3).setBrightness(50).draw();
   All3DPlots.get(CounterX).setPlotOnlySelected(true).setDrawAxis(false).setPointSize(4).setBrightness(255).draw();
  } 
 //TestCharacteristicsPlot.draw();
 
 //Put all Elemets in this function, which should be always on top of the 3D Window
 GraphOverlay();
}
//End draw *******************************************************************************************************************************************************************************

void GraphOverlay(){
  cam.beginHUD();
  text("Number of available Springs: " + TableSpringParameters.getRowCount() + "                    Number of Selected Springs: "+ NumberOfSelectedSprings,40,height-20);
//  stroke(255,0,0);
//  rect(0,0,500,500);
//  stroke(0,255,0);
//  line(AllowedRangeR2[0],0,0,AllowedRangeR2[0],height,0);
//  line(AllowedRangeR2[1],0,0,AllowedRangeR2[1],height,0);
//  line(0,AllowedRangeA[0],0,width,AllowedRangeA[0],0);
//  line(0,AllowedRangeA[1],0,width,AllowedRangeA[1],0);
//  stroke(0,0,255);
//  for(float i = 0; i<500;i++){
//    point(i,mass*gravity*R1/i/2.075,0);
//  }
//  line(370,0,0,370);
  cam.endHUD();
}

void RunSelection(){
  // Calculate scaled values
  mass_sc = mass/NumberOfParallelSprings;
  MechanicalAdvantage=MechanicalAdvantageZaehler/MechanicalAdvantageNenner; //println(MechanicalAdvantage);
  AllowedRangeA_sc[0] =AllowedRangeA[0]/MechanicalAdvantage;
  AllowedRangeA_sc[1] =AllowedRangeA[1]/MechanicalAdvantage;
  AllowedRangeR2_sc[0] =AllowedRangeR2[0]/MechanicalAdvantage;
  AllowedRangeR2_sc[1] =AllowedRangeR2[1]/MechanicalAdvantage;
  
  MassPotentialEnergy = mass_sc * gravity * R1 * 2;
  //Begin Selection Process *******************************************************************************************************************************************************************************  
  SelectionCounter =0;
  //Compare table with the filter rules from Sliders and set flag "Selected"
  for (TableRow row : TableSpringParameters.rows()) {
    boolean IsSelected = true;
    
    //select spring based on MassPotentialEnergy Minimum Spring Potential Energy
    if(!(row.getFloat("V[Nmm] Maximale potentielle Energie") >= MassPotentialEnergy)){
      IsSelected = false;
    }

    //Calculate TheoAmin, TheoAmax, TheoR2min, TheoR2max for the current spring based on Lr,k and the balancing condition   ???
    TheoR2[0] = row.getFloat("Lr[mm] Relevante Laenge")/2 - sqrt(sq(row.getFloat("Lr[mm] Relevante Laenge")  /2)-(mass_sc*gravity*R1/row.getFloat("k[N/mm] Federrate")));
    TheoR2[1] = row.getFloat("Lr[mm] Relevante Laenge")/2 + sqrt(sq(row.getFloat("Lr[mm] Relevante Laenge")  /2)-(mass_sc*gravity*R1/row.getFloat("k[N/mm] Federrate"))); 
    TheoA[0] =  TheoR2[0]; 
    TheoA[1] =  TheoR2[1];    
    
    //Calculate rect[FinalAmin,FinalR2min,FinalAmax,FinalR2max] by intersecting quad[TheoAmin,TheoR2min,TheoAmin,TheoAmax] and rect[AllowedRangeAmin,AllowedRangeAmax,AllowedRangeR2min,AllowedRangeR2max] 
    FinalA[0] = max(TheoA[0],AllowedRangeA_sc[0]);      
    FinalA[1] = min(TheoA[1],AllowedRangeA_sc[1]);
    FinalR2[0] = max(TheoR2[0],AllowedRangeR2_sc[0]);
    FinalR2[1] = min(TheoR2[1],AllowedRangeR2_sc[1]);
    
    if((FinalA[0]>FinalA[1]) || (FinalR2[0]>FinalR2[1])){
      IsSelected = false;
    }
    
    if(!(mass_sc*gravity*R1/row.getFloat("k[N/mm] Federrate")/FinalR2[0] >= FinalA[0]) || !(mass_sc*gravity*R1/row.getFloat("k[N/mm] Federrate")/FinalR2[1]<= FinalA[1])){
      IsSelected = false;
    }
    
    for (int i = 0; i < SpringParameterList.size();i++) {
      if(SpringParameterList.get(i).isTypeFloat())
      {
        IsSelected = IsSelected && SpringParameterList.get(i).isInMyRange(row.getFloat(SpringParameterList.get(i).ParameterName));
      } 
      else if(SpringParameterList.get(i).isTypeString())
      {
         //exclude "Bestellnummer" from isSelected-Test to optimice speed 
        if(SpringParameterList.get(i).ParameterName.equals("Bestellnummer") == false){IsSelected = IsSelected && SpringParameterList.get(i).isInMySelectionList(row.getString(SpringParameterList.get(i).ParameterName));}
      }
      
      if (!IsSelected) { 
        row.setInt("Selected", 0);
        break;
      } else
      {
        row.setInt("Selected", 1);
       
         //Calculate intersection points of the characteristics with rect[FinalA[0],FinalR2[0],FinalA[1],FinalR2[2]] of the real spring (behind ratio)
        FinalA[0] = max(mass_sc*gravity*R1/row.getFloat("k[N/mm] Federrate")/FinalR2[1],FinalA[0]);
        FinalA[1] = min(mass_sc*gravity*R1/row.getFloat("k[N/mm] Federrate")/FinalR2[0],FinalA[1]);
        FinalR2[0] = mass_sc*gravity*R1/row.getFloat("k[N/mm] Federrate")/FinalA[1];
        FinalR2[1] = mass_sc*gravity*R1/row.getFloat("k[N/mm] Federrate")/FinalA[0];
        
        //Write Real Values for "R2min" ,"R2max", "Amin", "Amax" for selected Spring Setup to Table
        row.setFloat("R2min", MechanicalAdvantage *  FinalR2[0]);
        row.setFloat("R2max", MechanicalAdvantage *  FinalR2[1]);
        row.setFloat("Amin",  MechanicalAdvantage *  FinalA[0]);
        row.setFloat("Amax",  MechanicalAdvantage *  FinalA[1]);
      }
    }
  }
  
  //Find minimum mass of single spring
  for(TableRow row : TableSpringParameters.rows()){
    if(row.getInt("Selected") ==1){
      SelectionCounter++;
      MinimumMass =min(MinimumMass,row.getFloat("Masse"));
    } 
  }
  
  NumberOfSelectedSprings=SelectionCounter;
    //Output Spring with Minimum mass in the current Selection
  if(PlotMinimumMass){
    cf.console.clear();  
    println("Spring System(s) with the lowest mass in the current selection:");
    println("--------------------------------------");
    for(TableRow row : TableSpringParameters.rows()){
      if((row.getInt("Selected")==1) && row.getFloat("Masse") == MinimumMass){
        println(NumberOfParallelSprings + "  parallel  " + row.getString("Bestellnummer") + " (" + row.getString("Hersteller") +")");
        println("Mechanical Advantage = " + MechanicalAdvantageZaehler+"/"+MechanicalAdvantageNenner);
        println("Spring Rate = " + row.getFloat("k[N/mm] Federrate") + " N/mm");
        println("Mass of single Spring = " + row.getFloat("Masse") + " g");
        println("Mass of all Springs = " + row.getFloat("Masse")*NumberOfParallelSprings + " g");
        println("Allowed Range A[mm] :  " +  nfc(row.getFloat("Amin"),2)  + " <= A <= " + nfc(row.getFloat("Amax"),2));
        println("Allowed Range R2[mm] : " + nfc(row.getFloat("R2min"),2)  + " <= R2 <= " + nfc(row.getFloat("R2max"),2));
        println("Condition for A and R2 : A*R2 =" + (mass*gravity*R1/row.getFloat("k[N/mm] Federrate")*MechanicalAdvantage*MechanicalAdvantage /NumberOfParallelSprings));
        println("--------------------------------------");   
      }
    }
    PlotMinimumMass =false;   
  }
  
 //End Selection Process *******************************************************************************************************************************************************************************
}
