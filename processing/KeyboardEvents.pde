void keyPressed() {
 int i=0;
 cf.console.clear();
 if ((key == 'S') || (key == 's')) {
   cf.console.clear();  
  for (TableRow row : TableSpringParameters.rows()) {
    if(row.getInt("Selected") == 1)
    {      
      println(row.getString("Bestellnummer") + " K= " + row.getFloat("k[N/mm] Federrate") + " m= " + row.getFloat("Masse"));
      println(AllowedRangeA_sc[0]  + " <= A_A <= " + AllowedRangeA_sc[1]);
      println(AllowedRangeR2_sc[0]  + " <= R2_A <= " + AllowedRangeR2_sc[1]);
      println(row.getFloat("Amin")  + " <= A <= " + row.getFloat("Amax"));
      println(row.getFloat("R2min")  + " <= R2 <= " + row.getFloat("R2max"));
      i++;
    }
  }
  println("Anzahl: " + i);
 } else if ((key == 'O') || (key == 'o'))
 {
  ortho();//
 } else if ((key == 'P') || (key == 'p'))
 {
  perspective();
 } else if (keyCode == RIGHT)
 {
   if(CounterX<All3DPlots.size()-1 ){CounterX = CounterX +1;} else {CounterX = 0; }
   //cam.reset(0); //reset the 
 } else if (keyCode == LEFT)
 {
   if(CounterX>0 ){CounterX = CounterX -1;} else {CounterX = All3DPlots.size()-1;}
   //cam.reset(0);
 }
 else if (keyCode == UP)
 {
   if(CounterX<All3DPlots.size()-1 ){CounterX = CounterX +1;} else {CounterX = 0; }
   //cam.reset(0); //reset the 
 } else if (keyCode == DOWN)
   {
   if(CounterX>0 ){CounterX = CounterX -1;} else {CounterX = All3DPlots.size()-1;}
   //cam.reset(0);
 } else if (key == 'c')
   {
    All3DPlots.get(CounterX).setPlotOnlySelected(false).setPointSize(3).setBrightness(50).drawToGnuPlot();
 } 
 else if (key == 'm')
   {
    PlotMinimumMass = !PlotMinimumMass;
 }
 else if (key == 'f')
   {
    FindAllPossibleSprings = true;
 }
}
