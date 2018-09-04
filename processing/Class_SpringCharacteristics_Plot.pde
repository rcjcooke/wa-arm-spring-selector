//Begin class SpringCharacteristics *******************************************************************************************************************************************************************************
public class SpringCharacteristics{
  
  Table Database;
   
  SpringParameter ParameterX;
  SpringParameter ParameterY;
  SpringParameter ParameterZ;
  
  float OriginX;
  float OriginY;
  float OriginZ;
  
  public SpringCharacteristics(float OriginX, float OriginY, float OriginZ)
  {  
    this.OriginX = OriginX;
    this.OriginY = OriginY;
    this.OriginZ = OriginZ;
  }
  
  public SpringCharacteristics setOriginX(float OriginX){
    this.OriginX = OriginX;
    return this;
  } 
  
  public SpringCharacteristics setOriginY(float OriginY){
    this.OriginY = OriginY;
    return this;
  } 
  
  public SpringCharacteristics setOriginZ(float OriginZ){
    this.OriginZ = OriginZ;
    return this;
  } 
  
  public SpringCharacteristics setOriginXYZ(float OriginX, float OriginY, float OriginZ){
    this.OriginX = OriginX;
    this.OriginY = OriginY;
    this.OriginZ = OriginZ;
    return this;
  }
  public void draw(){
    stroke(255);
    //translate(OriginX,OriginY,OriginZ);
    scale(1,-1,1);
    //XYAxis
    line(0,0,0,0,200,0);
    line(0,0,0,200,0,0);
    //Characteristic
    line(50,0,0,150,100,0);
    line(100,0,0,100,50,0);
    line(150,0,0,150,100,0);  
  }
}  
//End class SpringCharacteristics *******************************************************************************************************************************************************************************
