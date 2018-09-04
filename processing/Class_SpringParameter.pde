public class SpringParameter {
  public String ParameterName;
  public float Min;
  public float Max;
  public ArrayList<String> ListOfStringParameterOptions = new ArrayList<String>(); 
  public ArrayList<String> ListOfSelectedParameterOptions = new ArrayList<String>();
  public float SelectionMin;
  public float SelectionMax;
  public float[] CurrentRange = new float[2];
  public char ParameterType;
  
 SpringParameter(String Name){
    ParameterName = Name;
  }
  
  void setListOfStringParameterOptions(String[] ParameterOptions)
  {  
    for (String s : ParameterOptions) { 
     if(!ListOfStringParameterOptions.contains(s))
      {
        ListOfStringParameterOptions.add(s); 
      } 
    }  
  }
  
  boolean isInMyRange(float ValueToTest) { 
    return ((ValueToTest>=CurrentRange[0] & ValueToTest<=CurrentRange[1]));//||((ValueToTest>=CurrentRange[0]/2) & (ValueToTest<=CurrentRange[1]/2))||((ValueToTest>=CurrentRange[0]/4) & (ValueToTest<=CurrentRange[1]/4))||((ValueToTest>=CurrentRange[0]/6) & (ValueToTest<=CurrentRange[1]/6))||((ValueToTest>=CurrentRange[0]/8) & (ValueToTest<=CurrentRange[1]/8))); //||((ValueToTest>=CurrentRange[0]/2) & (ValueToTest<=CurrentRange[1]/2))
  }
  
   boolean isInMySelectionList(String StringToTest) { 
    return (ListOfSelectedParameterOptions.contains(StringToTest));
  }
 
  void setTypeFloat() {
    ParameterType = 'F';
  }
  
  void setTypeString() {
    ParameterType = 'S';
  }

  boolean isTypeString() {
    if (ParameterType == 'S') {
    return true;
    }
    else {
    return false;
    }
  }
  boolean isTypeFloat() {
    if (ParameterType == 'F') {return true;}
    else {return false;}
  }  
 
 String getParameterName() {
    return ParameterName;
  } 
}
