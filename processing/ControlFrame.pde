
// The ControlFrame class extends PApplet, so we 
// are creating a new processing applet inside a
// new frame with a controlP5 object loaded
public class ControlFrame extends PApplet {

  int w, h;
  ArrayList<SpringParameter> ParameterList;
  
  ArrayList<Range> ParameterSliderList;
  ArrayList<Slider> SpecialParameterSliderList;
  
  ArrayList<ListBoxExtended> ParameterListBoxList;
  
  Textarea myTextarea;
  Println console;
 
  public void setup() {
    size(w, h);
    frameRate(25);
         
    cp5 = new ControlP5(this);
    
    myTextarea = cp5.addTextarea("txt").moveTo("global").setPosition(w-250, 10).setSize(230, cf.height-100).setLineHeight(15).setColor(color(255)).setColorBackground(color(0,0,100)).setColorForeground(color(255));
  
    console = cp5.addConsole(myTextarea);//
    
    ParameterSliderList = new ArrayList<Range>();
    SpecialParameterSliderList = new ArrayList<Slider>();
    ParameterListBoxList = new ArrayList<ListBoxExtended>();
     
    cp5.getTab("default").setLabel("Auswahlprozedur");
     
    cp5.addTab("Alle Parameter").setColorBackground(color(0, 160, 100)).setColorLabel(color(255)).setColorActive(color(255,128,0));
    
   // create new buttons
   cp5.addButton("Leichteste Federkombination ?").moveTo("default").setValue(1).plugTo(parent,"PlotMinimumMass").setPosition(650,200).setSize(150,50).registerTooltip("Gibt die leichteste Federkombination der momentanen Federauswahl fuer das gewaehlte n und i aus") ;
   cp5.addButton("Ermittle alle Federkombination").moveTo("default").setValue(1).plugTo(parent,"FindAllPossibleSprings").setPosition(830,200).setSize(150,50).registerTooltip("Gibt alle moeglichen Federkombination der momentanen Federauswahl fuer alle moeglichen n und i aus") ;
   cp5.addButton("Exportiere Variationen nach *.csv").setBroadcast(false).moveTo("default").setValue(1).plugTo(parent,"ExportVariations").setPosition(1010,200).setSize(150,50).hide();

   //create one ControlSlider for mass and R1
   cp5.addSlider("R1 [mm]").plugTo(parent,"R1").moveTo("default").setRange(0, 1000).setPosition(15,20).setWidth(1000).setDecimalPrecision(0).setValue(0.01);
   cp5.addSlider("Masse [g]").plugTo(parent,"mass").moveTo("default").setRange(0, 60000).setPosition(15,40).setWidth(1000).setDecimalPrecision(0).setValue(0.01); 
   //create one RangeSlider for AllowedRangeA and AllowedRangeR2  
   cp5.addRange("Bauraum - A[mm]").moveTo("default").setRange(0, 500).setPosition(15,60).setWidth(1000).setDecimalPrecision(0).setRangeValues(0,500);
   cp5.addRange("Bauraum - R2[mm]").moveTo("default").setRange(0, 500).setPosition(15,80).setWidth(1000).setDecimalPrecision(0).setRangeValues(0,500); 
   //create RangeSlider for NumberOfSprings
   cp5.addSlider("Anzahl paralleler Federn").setSliderMode(Slider.FLEXIBLE).plugTo(parent,"NumberOfParallelSprings").moveTo("default").setNumberOfTickMarks(20).setRange(1,20).setPosition(15,100).setWidth(1000).setValue(1); 
   //cp5.addNumberbox("Number of Springs").plugTo(parent,"NumberOfParallelSprings").moveTo("default").setPosition(100,160).setSize(100,14).setScrollSensitivity(0.1).setValue(1);
   
   cp5.addSlider("Uebersetzungsverhaeltnis Zaehler").setSliderMode(Slider.FLEXIBLE).plugTo(parent,"MechanicalAdvantageZaehler").moveTo("default").setNumberOfTickMarks(10).setRange(1,10).setPosition(15,120).setWidth(1000).setValue(1);
   cp5.addSlider("Uebersetzungsverhaeltnis Nenner").setSliderMode(Slider.FLEXIBLE).plugTo(parent,"MechanicalAdvantageNenner").moveTo("default").setNumberOfTickMarks(10).setRange(1,10).setPosition(15,140).setWidth(1000).setValue(1);   
    
    //create one ControlSlider (RangeSlider) for each float parameter
    for (int i = 0;i < ParameterList.size();i++){
      if(ParameterList.get(i).isTypeFloat())
      {
        ParameterSliderList.add(cp5.addRange(ParameterList.get(i).ParameterName).moveTo("Alle Parameter").setPosition(15,20+15*(ParameterSliderList.size())).setWidth(1000).setHandleSize(20).setDecimalPrecision(5).setRange(ParameterList.get(i).Min, ParameterList.get(i).Max).setRangeValues(ParameterList.get(i).Min, ParameterList.get(i).Max));
        //SpecialParameterSliderList.add(cp5.addSlider(ParameterList.get(i).ParameterName + "R").setPosition(15,30+15*(ParameterSliderList.size()-1)).setHeight(2).setWidth(1000).setHandleSize(20).setDecimalPrecision(5).setRange(ParameterList.get(i).Min, ParameterList.get(i).Max).setLabelVisible(false)); 
      } 
      //create one Listbox for each string parameter
      else if (ParameterList.get(i).isTypeString())
      {
        ParameterListBoxList.add(new ListBoxExtended(cp5.addListBox(ParameterList.get(i).ParameterName).moveTo("Alle Parameter").setPosition(10+150*(ParameterListBoxList.size()),cf.height-120).setSize(120, 80).setItemHeight(10).setBarHeight(10).setId(ParameterListBoxList.size())));
        //set Position in OverallParameterList
        ParameterListBoxList.get(ParameterListBoxList.size()-1).PositionInAllParameterlist = i;
        //Add items to ParameterListBox based on entrys in parameters ListOfStringParameterOptions
        for (int n=0;n<ParameterList.get(i).ListOfStringParameterOptions.size();n++) {
          ParameterListBoxList.get(ParameterListBoxList.size()-1).TheListBox.addItem(ParameterList.get(i).ListOfStringParameterOptions.get(n), n);
          ParameterListBoxList.get(ParameterListBoxList.size()-1).ElementsNames.add(ParameterList.get(i).ListOfStringParameterOptions.get(n));
        }
      }
    }

     //Initialize Selection: Select all ListOfStringParameterOptions for each String paramater
    for (ListBoxExtended l : ParameterListBoxList)
    {
      l.SelectAllElements();
    }
    
    for (int i =0;i < ParameterListBoxList.size();i++) {
      ParameterList.get(ParameterListBoxList.get(i).PositionInAllParameterlist).ListOfSelectedParameterOptions = ParameterListBoxList.get(i).SelectedElementsNames;
    }
     
}

  public void draw() {
    background(0);
    textSize(20);
    //text("Number of Selected Springs:    "+ NumberOfSelectedSprings,width-500,height-30);
    //text("Number of Available Springs:   "+ TableSpringParameters.getRowCount(),width-500,height-10);
    if(cp5.getTab("default").isActive()){
      image(System,100,170); 
      WorkContext = 1;
      fill(255);
      textSize(10);
      //text(MassPotentialEnergy,1080,30);
      CounterX=6122;
    } else if(cp5.getTab("Alle Parameter").isActive()){
      WorkContext = 2;
    }   
  }
    
  void controlEvent(ControlEvent theControlEvent) {
    
    if(theControlEvent.isFrom("Bauraum - A[mm]")){
      AllowedRangeA[0] = theControlEvent.getController().getArrayValue(0);
      AllowedRangeA[1] = theControlEvent.getController().getArrayValue(1);
    } else if(theControlEvent.isFrom("Bauraum - R2[mm]")) {
      AllowedRangeR2[0] = theControlEvent.getController().getArrayValue(0);
      AllowedRangeR2[1] = theControlEvent.getController().getArrayValue(1);
    }
    
    for (int i = 0;i < ParameterList.size();i++){
      if(theControlEvent.isFrom(ParameterList.get(i).ParameterName)) {
        if (ParameterList.get(i).isTypeFloat()) {
          ParameterList.get(i).CurrentRange[0] = theControlEvent.getController().getArrayValue(0);
          ParameterList.get(i).CurrentRange[1] = theControlEvent.getController().getArrayValue(1);
        }  
        else if (ParameterList.get(i).isTypeString())
        {
          ParameterListBoxList.get(theControlEvent.getId()).ToggleElement((int)theControlEvent.group().value());
          //println(theControlEvent.group().value());
          console.clear();          
         // println(ParameterList.get(i).ParameterName + " " + ParameterListBoxList.get(theControlEvent.getId()).SelectedElements);
          //ListOfSelectedStringParameterOptions
          ParameterList.get(i).ListOfSelectedParameterOptions = ParameterListBoxList.get(theControlEvent.getId()).SelectedElementsNames;
          //println(ParameterListBoxList.get(theControlEvent.getId()).SelectedElementsNames);
          //println("------------");
          println(ParameterListBoxList.get(theControlEvent.getId()).SelectedElementsNames);
            //ElementsNames;
            //SelectedElementsNames;
        }
      } 
    }
  }
  
 private ControlFrame() { 
  }


  public ControlFrame(Object theParent,ArrayList<SpringParameter> iSpringParameterList, int theWidth, int theHeight) {
    parent = theParent;
    w = theWidth;
    h = theHeight;
    ParameterList = iSpringParameterList;
    
  }

  public ControlP5 control() {
    return cp5;
  }
  
  
  ControlP5 cp5;

  Object parent;
  Range RangeA;
}

ControlFrame addControlFrame(String theName, ArrayList<SpringParameter> iSpringParameterList, int theWidth, int theHeight) {
  Frame f = new Frame(theName);
  ControlFrame p = new ControlFrame(this, iSpringParameterList, theWidth, theHeight);
  f.add(p);
  p.init();
  f.setTitle(theName);
  f.setSize(p.w, p.h);
  f.setLocation(100, 100);
  f.setResizable(true);
  f.setVisible(true);
  return p;
}

public class ListBoxExtended
{
  ListBox TheListBox;
  ArrayList<Integer> SelectedElements;
  ArrayList<String>  ElementsNames;
  ArrayList<String>  SelectedElementsNames;
  int PositionInAllParameterlist;
  
  ListBoxExtended(ListBox iListBox) 
  {
  TheListBox = iListBox;
  SelectedElements = new ArrayList<Integer>();
  ElementsNames = new ArrayList<String>();
  SelectedElementsNames = new ArrayList<String>();
  }
  
  ArrayList<Integer> getSelectedElements()
  {
    return SelectedElements;
  }
  
  String ElementName(int Elementnumber)
  {
    return TheListBox.getItem(SelectedElements.indexOf(Elementnumber)).getName();    
  }
  
  void SelectElement(int Elementnumber)
  {
    if(!SelectedElements.contains(Elementnumber))
      {
        SelectedElements.add(Elementnumber);
        SelectedElementsNames.add(ElementsNames.get(Elementnumber));
        //println("+++" + ElementName(Elementnumber));
        TheListBox.getItem(Elementnumber).setColorBackground(color(0,150,0));
      }
  }
  void ToggleElement(int Elementnumber)
  {
    if(!SelectedElements.contains(Elementnumber))
      {
        SelectElement(Elementnumber);
        //println(TheListBox.getItem(Elementnumber).getName());
      }
      else 
      {
        UnSelectElement(Elementnumber);
      }
  }
  
  void UnSelectElement(int Elementnumber)
  {
    if(SelectedElements.contains(Elementnumber))
      {
        TheListBox.getItem(Elementnumber).setColorBackground(color(150,0,0));
        SelectedElements.remove(SelectedElements.indexOf(Elementnumber));
        SelectedElementsNames.remove(ElementsNames.get(Elementnumber));
      }
  }
  
  void SelectAllElements()
  {
    SelectedElements.clear(); 
    for(int i = 0;i<TheListBox.getListBoxItems().length;i++)
    {
      SelectElement(i);
    }
  }

  void UnSelectAllElements()
  {
    for(int i = 0;i<TheListBox.getListBoxItems().length;i++)
    {
      UnSelectElement(i);
    }
  }
}
