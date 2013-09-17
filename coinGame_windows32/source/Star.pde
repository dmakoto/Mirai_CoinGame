class Star{
  
  PVector pos;
  
  float maxY = 2.0;
  
  PVector to;
  float speed = 0.1;
  
  
  float ang;
  
  OBJModel model;
  BoundingBox bbox;
  
  Star(PApplet app, float posX, float posY, float posZ, float toX, float toY, float toZ){
    pos = new PVector(posX, posY, posZ);
    to = new PVector(toX, toY, toZ);
    ang = 0;
    
     model = new OBJModel(app, "star.obj");
      model.scale(.2);
    
  }
  
  void drawStar(){
  
    pos.x += to.x*speed;
    pos.y += to.y*speed;
    pos.z += to.z*speed;
    
    ang += (1.0/30.0)*PI;
    if(ang > PI*2) ang = ang - PI*2;
    
    pushMatrix();

    translate(pos.x, pos.y + maxY*abs(cos(ang)), pos.z);
    rotateX(PI/5);
    model.draw();
    
    popMatrix();    
    
  }
   

}
