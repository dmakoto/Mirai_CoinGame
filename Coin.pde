class Coin {

  float posX;
  float posZ;

  float posY =  .75;
  
  float sizeX;
  float sizeY;
  float sizeZ; 

  OBJModel model;
  BoundingBox bbox;
  
  float angY; 
  float angAlpha;
  
  int state = 0;  

  Coin(PApplet app, float posX, float posY, float posZ) {    
    
    
    if(model == null) {
      model = new OBJModel(app, "coin.obj");
      model.scale(.2);
      model.translateToCenter();
    }
    bbox = new BoundingBox(app, model);
    
    this.posX = posX;
    this.posY = posY;
    this.posZ = posZ;
    
    sizeX = bbox.getWHD().x;
    sizeY = bbox.getWHD().y;
    sizeZ = bbox.getWHD().z;
    
  }
  
  void changeState(int val){state = val;}
  int getState(){return state;}

  void drawCoin() {
    
    if(state == 0){
        
      angY += (0.75/30.0)*2*PI*sin(angAlpha);
      if(angY > 2*PI) angY = 2*PI - angY;
      angAlpha += (0.5/30.0)*(PI);
      if(angAlpha > PI) angAlpha = (PI) - angAlpha;
          
      pushMatrix();
      fill(255);
      noStroke();
      translate(posX, posY, posZ);
      rotateX(PI/5);
      rotateY(angY);
      model.draw();
      noFill();
      stroke(255, 0, 255);
      //bbox.draw();
      noStroke();
      
      popMatrix();
      
    }    
    else if(state == 1){
      
      posY += 0.35;
      
      if(posY > sizeY*2.5) state = 2;
          
      pushMatrix();
      fill(255);
      noStroke();
      translate(posX, posY, posZ);
      rotateX(PI/5);
      model.draw();
      noFill();
      stroke(255, 0, 255);
      //bbox.draw();
      noStroke();
      
      popMatrix();
      
    }
    
    
    
  }
  
  boolean collided(float x, float y, float z){
  
    if( (x > (posX - sizeX*1.5) ) && (x < (posX + sizeX*1.5) )
        && (y > (posY - sizeY*1.5) ) && (y < (posY + sizeY*1.5) ) 
        && (z > (posZ - sizeZ*1.5) ) && (z < (posZ + sizeZ*1.5) ) ) {
          return true;}
        else {return false;}    
  }
}

