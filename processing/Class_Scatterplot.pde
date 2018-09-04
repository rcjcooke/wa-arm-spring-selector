//Begin class ScatterPlot *******************************************************************************************************************************************************************************
public class ScatterPlot{
  
  Table Database;
  
  SpringParameter ParameterX;
  SpringParameter ParameterY;
  SpringParameter ParameterZ;
  
  float OriginX;
  float OriginY;
  float OriginZ;
  
  float MaxXAxis = width;
  float MaxYAxis = height;
  float MaxZAxis = width;
  float MinimumMass =0;
  
  float PointSize = 1;
  float Brightness = 255;
  color DiagrammColor = 0;
  float LabelSize =10 ;
  
  boolean Is3D;
  boolean Is2D;
  boolean OnlySelected = false;
  boolean DrawAxes = true;
  boolean DrawCube = true;
  boolean DrawMinMaxLabels = true;
  boolean DrawLimitGraphs = false;
  boolean DrawMinimumMass = false;

// 3D  ScatterPlot
  public ScatterPlot(SpringParameter ParameterX, SpringParameter ParameterY, SpringParameter ParameterZ)
  {
    this.ParameterX = ParameterX;
    this.ParameterY = ParameterY;
    this.ParameterZ = ParameterZ;
    Is3D = true;
    Is2D = false;  
  }
// 2D  ScatterPlot
  public ScatterPlot(SpringParameter ParameterX, SpringParameter ParameterY)
  {
    this.ParameterX = ParameterX;
    this.ParameterY = ParameterY;
    this.ParameterZ = ParameterX;
    Is3D = false;
    Is2D = true;
  }
// 3D  ScatterPlot
  public ScatterPlot(SpringParameter ParameterX, SpringParameter ParameterY, SpringParameter ParameterZ, float OriginX, float OriginY, float OriginZ)
  {  
    this.ParameterX = ParameterX;
    this.ParameterY = ParameterY;
    this.ParameterZ = ParameterZ;
    this.OriginX = OriginX;
    this.OriginY = OriginY;
    this.OriginZ = OriginZ;
    Is3D = true;
    Is2D = false;
  }  
  // 3D  ScatterPlot
  public ScatterPlot(SpringParameter ParameterX, SpringParameter ParameterY,float OriginX, float OriginY, float OriginZ)
  {  
    this.ParameterX = ParameterX;
    this.ParameterY = ParameterY;
    this.ParameterZ = ParameterX;
    this.OriginX = OriginX;
    this.OriginY = OriginY;
    this.OriginZ = OriginZ;
    Is3D = false;
    Is2D = true;
  }
  
  public ScatterPlot setTable(Table iDatabase){
    Database = iDatabase;
    return this;
  }

  public ScatterPlot drawMinimumMass(float MinimumMass){
   this.MinimumMass = MinimumMass;
   //println(MinimumMass);
   DrawMinimumMass = true;
   return this;
  }
  
  public ScatterPlot setPlotOnlySelected(boolean OnlySelected){
    this.OnlySelected = OnlySelected;
    return this;
  } 
  
  public ScatterPlot setPlotAll(boolean PlotAll){
    this.OnlySelected = !PlotAll;
    return this;
  } 
  
  public ScatterPlot setColor(color iColor){
    DiagrammColor = iColor;
    return this;
  }  
  
  public ScatterPlot setBrightness(float Brightness ){
    this.Brightness = Brightness;
    return this;
  }  
  
  public ScatterPlot setDrawAxis(boolean DrawAxes){
    this.DrawAxes = DrawAxes;
    return this;
  }
  
  public ScatterPlot setDrawLimitGraphs(boolean DrawLimitGraphs){
    this.DrawLimitGraphs = DrawLimitGraphs;
    return this;
  }   
  
  public ScatterPlot setPointSize(float iPointSize){
    PointSize = iPointSize;
    return this;
  }  
  
  public ScatterPlot setOriginX(float OriginX){
    this.OriginX = OriginX;
    return this;
  } 
  
  public ScatterPlot setOriginY(float OriginY){
    this.OriginY = OriginY;
    return this;
  } 
  
  public ScatterPlot setOriginZ(float OriginZ){
    this.OriginZ = OriginZ;
    return this;
  } 
  
  public ScatterPlot setOriginXYZ(float OriginX, float OriginY, float OriginZ){
    this.OriginX = OriginX;
    this.OriginY = OriginY;
    this.OriginZ = OriginZ;
    return this;
  }  
  
  public ScatterPlot setMaxXAxis(float MaxXAxis){
    this.MaxXAxis = MaxXAxis;
    return this;
  } 
  
  public ScatterPlot setMaxYAxis(float MaxYAxis){
    this.MaxYAxis = MaxYAxis;
    return this;
  } 

  
  public ScatterPlot setMaxZAxis(float MaxZAxis){
    this.MaxZAxis = MaxZAxis;
    return this;
  }

  public ScatterPlot setMaxXYZAxis(float MaxXAxis, float MaxYAxis, float MaxZAxis){
    this.MaxXAxis = MaxXAxis;
    this.MaxYAxis = MaxYAxis;
    this.MaxZAxis = MaxZAxis;
    return this;
  }   
  
  public ScatterPlot setLabelSize(float LabelSize){
    this.LabelSize = LabelSize;
    return this;
  }
  
  //draw to screen
  public void drawToGnuPlot(){
    //GnuPlot *.plt file erstellen 
    saveTable(Database, "data/CurrentSelection.csv");
    GnuPlotOutput = createWriter(ParameterX.ParameterName +"_"+ ParameterY.ParameterName +"_" +ParameterZ.ParameterName +".plt");
    GnuPlotOutput.println("set datafile separator \",\"");
    GnuPlotOutput.println("set nokey");
    GnuPlotOutput.println("set title '" + ParameterX.ParameterName +" "+ ParameterY.ParameterName +" " +ParameterZ.ParameterName +"'");
    GnuPlotOutput.println("cd \'C:\\Users\\Frank\\Dropbox\\NRL\\Processing\\SpringSelectorBalancer\\data\'");
    GnuPlotOutput.println("set xlabel \"" + ParameterX.ParameterName + "\"");
    GnuPlotOutput.println("set ylabel \"" + ParameterY.ParameterName + "\"");
    GnuPlotOutput.println("set zlabel \"" + ParameterZ.ParameterName + "\"");
    GnuPlotOutput.println("set datafile separator \",\"");
    GnuPlotOutput.println("set ticslevel 0");
    GnuPlotOutput.println("set palette model HSV rgbformulae 3,2,2");
    GnuPlotOutput.println("splot \'CurrentSelection.csv\' u ($"+(Database.getColumnIndex("Selected")+1)+"==1 ? $"+ (Database.getColumnIndex(ParameterX.ParameterName)+1) +":1/0):"+ (Database.getColumnIndex(ParameterY.ParameterName)+1)+":"+ (Database.getColumnIndex(ParameterZ.ParameterName)+1) + " lc palette");
    println(Database.getColumnCount());
    GnuPlotOutput.flush(); // Writes the remaining data to the file
    GnuPlotOutput.close(); // Finishes the file
  }
  
  public void draw(){
   translate(OriginX,OriginY,OriginZ);   
   if (Is3D) {if(DrawCube){drawCube();} }
   if (Is2D) {if(DrawCube){drawRect();} }
   if(DrawAxes){drawAxes();}
   //scale((MaxXAxis / ParameterX.Max),(MaxYAxis / ParameterY.Max),(MaxZAxis / ParameterZ.Max));
   //translate(ParameterX.Min,ParameterY.Min,ParameterZ.Min);
   scale((MaxXAxis / (ParameterX.Max-ParameterX.Min)),(MaxYAxis / (ParameterY.Max-ParameterY.Min)),(MaxZAxis / (ParameterZ.Max-ParameterZ.Min)));
   for (TableRow row : Database.rows()) { 
     if(row.getInt("Selected") == 1 || OnlySelected == false){
        if(((MinimumMass*0.999) < row.getFloat("Masse")) && (row.getFloat("Masse") < (MinimumMass*1.001)) && (OnlySelected)){ strokeWeight(PointSize*4);} else {strokeWeight(PointSize);}
        stroke(DiagrammColor);
        //if(DiagrammColor == 0){
          colorMode(HSB);
          stroke(row.getFloat(ParameterZ.ParameterName)/ParameterZ.Max*255,255,Brightness);
          colorMode(RGB);
        //}
        pushMatrix();
          if (Is3D) {
            translate(row.getFloat(ParameterX.ParameterName)-ParameterX.Min, -row.getFloat(ParameterY.ParameterName)+ParameterY.Min, row.getFloat(ParameterZ.ParameterName)-ParameterZ.Min);
          } else if (Is2D) {
            //set Scaling to the following
            translate(row.getFloat(ParameterX.ParameterName)-ParameterX.Min , -row.getFloat(ParameterY.ParameterName)+ParameterY.Min,0);
          }
          point (0,0,0);
        popMatrix();
     }
   }
   
   if(DrawLimitGraphs){
     hint(DISABLE_DEPTH_TEST);
     stroke(255,255,0,200);
     strokeWeight(1);
     //DrawLimitGraphs
     translate(-ParameterX.Min,ParameterY.Min,0);
     for(float i = ParameterX.Min; i<ParameterX.Max;i++){
       if(ParameterY.Max > (2*MassPotentialEnergy/i)){
         line(i,-2*MassPotentialEnergy/i,0,i+1,-2*MassPotentialEnergy/(i+1),0);
       }
       //line(AllowedRangeA[0]+AllowedRangeR2[0],0,AllowedRangeA[0]+AllowedRangeR2[0],-ParameterY.Max);
       //line(AllowedRangeA[0]+AllowedRangeR2[0],0,AllowedRangeA[0]+AllowedRangeR2[0],-ParameterY.Max);
       //line(AllowedRangeA[1]+AllowedRangeR2[1],0,AllowedRangeA[1]+AllowedRangeR2[1],-ParameterY.Max);
     }
     line(0,0,0,ParameterX.Max,-ParameterX.Max*MassPotentialEnergy/2/AllowedRangeR2_sc[0]/AllowedRangeA_sc[0],0);
     line(0,0,0,ParameterX.Max,-ParameterX.Max*MassPotentialEnergy/2/AllowedRangeR2_sc[1]/AllowedRangeA_sc[1],0);
     
     translate(ParameterX.Min,+ParameterY.Min,0);
     hint(ENABLE_DEPTH_TEST);
   }  
   
   scale(((ParameterX.Max-ParameterX.Min) / MaxXAxis),((ParameterY.Max-ParameterY.Min) / MaxYAxis),((ParameterZ.Max-ParameterZ.Min) / MaxZAxis));
  // translate(-ParameterX.Min,-ParameterY.Min,-ParameterZ.Min);
   translate(-OriginX,-OriginY,-OriginZ);
   //println(Anzahll);
  }

  private void drawAxes(){
   textSize(LabelSize);   
    stroke(50); fill(250); strokeWeight(2);
    line(0,0,0,MaxXAxis,0,0);
    text(ParameterX.ParameterName,MaxXAxis*1/5,+2+ 2*LabelSize,0);
    text(ParameterX.Min,0,+2+LabelSize,0);
    text(ParameterX.Max,MaxXAxis*1,+2+LabelSize,0);
    line(0,0,0,0,-MaxYAxis,0);
    pushMatrix();
    translate(0,-MaxYAxis,0);
    rotateZ(-HALF_PI);
    text(ParameterY.ParameterName,-MaxYAxis*4/5,-2*LabelSize,0);
    text(ParameterY.Min,-MaxYAxis,-LabelSize,0);
    text(ParameterY.Max,MaxYAxis*1-MaxYAxis,-LabelSize,0);
    popMatrix();
    if (Is3D) {
    line(0,0,0,0,0,MaxZAxis);
    pushMatrix();
    translate(0,0,MaxZAxis);
    rotateY(-HALF_PI);
    rotateX(PI+HALF_PI);
    text(ParameterZ.ParameterName,-MaxZAxis*4/5,-2-LabelSize,0);
    text(ParameterZ.Min,-MaxZAxis,-2,0);
    text(ParameterZ.Max,MaxZAxis*1 -MaxZAxis,-2,0);
    popMatrix();
    }
  }  
 
 private void drawCube(){ 
   stroke(50); noFill(); strokeWeight(1);
   translate(MaxXAxis/2,-MaxYAxis/2,MaxZAxis/2);
   box(MaxXAxis,MaxYAxis,MaxZAxis);
   translate(-MaxXAxis/2,MaxYAxis/2,-MaxZAxis/2);
 }
 
  private void drawRect(){ 
   stroke(50); noFill(); strokeWeight(1);
   rect(0,0,MaxXAxis,-MaxYAxis);
 }
    
}
//End class ScatterPlot *******************************************************************************************************************************************************************************
