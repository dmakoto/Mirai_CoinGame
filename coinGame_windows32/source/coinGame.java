import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import saito.objloader.*; 
import javax.media.opengl.GL; 
import javax.media.opengl.GL2; 
import java.util.*; 
import processing.serial.*; 
import tsps.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class coinGame extends PApplet {











TSPS tspsReceiver;


Floor floor;
ArrayList<Coin> coins;

ArrayList deleteCoins;

ArrayList<Star> stars;

ArrayList personsX;
ArrayList personsY;

float mouseAtFloorX;
float mouseAtFloorY;

float AoVY = PI/4;

float cameraPosX;
float cameraPosY = 10.0f;
float cameraPosZ;

Serial myPort;       

float initStarTime;
float starDuration = 5000.0f;
int state  = 0;


public void setup(){
  
  //size(800,600, P3D);
  size(1024,1536 , P3D);
  
  
  floor = new Floor();
    
  perspective(AoVY, PApplet.parseFloat(width)/PApplet.parseFloat(height), 
            0.01f, 100);
            
  noStroke();
  
  frameRate(30);
  
  coins = new ArrayList();
  deleteCoins = new ArrayList();
  
  personsX = new ArrayList();
  personsY = new ArrayList();
  
  stars = new ArrayList();
  
  createCoinGrid(-2.5f + .75f/4.0f, 2.5f+ .75f/4.0f, -4.5f, 4.5f, 1.1f, 1.1f);  
  
  myPort = null;
  if(Serial.list().length > 0) myPort = new Serial(this, Serial.list()[0], 57600);
  
  tspsReceiver= new TSPS(this, 12000);
  
}

public void draw(){
  
  if(keyPressed){
    if(key == 'r') {
      coins.clear();
      if(myPort != null) {
        myPort.write('4');
        myPort.write('5');
        myPort.write('d');
      }
    }
    else if(key == 'e') {
      coins.clear();
      createCoinGrid(-2.5f + .75f/4.0f, 2.5f+ .75f/4.0f, -4.5f, 4.5f, 1.1f, 1.1f);  
    }
    else if(key == 's' && (state == 0)) {
      coins.clear();
      createRandomStars();      
    }
  }
  
  background(0,175,200);
  camera(0.0f, 10.0f, 0.0f, 
         0.0f, -1.0f, 0.0f, 
         0.0f, .0f, -1.0f);
         
    
  fill(255);
  
 
  floor.drawFloor();
  
  //players
   tspsReceiver.update();     
   for (Enumeration e = tspsReceiver.people.keys() ; e.hasMoreElements() ;)
  {
    //get person
    int i = (Integer) e.nextElement();
    TSPSPerson person = (TSPSPerson) tspsReceiver.people.get(i);
    
    float[] personPos = calcMousePointerAtFloor(person.centroid.x*width, person.centroid.y*height);
    
    personsX.add(personPos[0]);
    personsY.add(personPos[1]);        
        
    pushMatrix();
    fill(100);
    translate(personPos[0], .75f,personPos[1]);
    scale(.3f,.3f,.3f);
    box(1.0f);
    popMatrix();
      
  };
  
  
  float[] mouseAtFloor = calcMousePointerAtFloor(mouseX, mouseY);
  
//  pushMatrix();
//  translate(mouseAtFloor[0],0,mouseAtFloor[1]);
//  scale(.3,.3,.3);
//  box(1.0);  
//  popMatrix();
  
   //coin.drawCoin();
  
  for(int i = 0; i < coins.size(); i++){
  
    coins.get(i).drawCoin();
    if(coins.get(i).collided(mouseAtFloor[0], .75f, mouseAtFloor[1])) {
      
      coins.get(i).changeState(1);
      //deleteCoins.add(i);
    }
    
    for(int j = 0; j < personsX.size(); j++){
      if(coins.get(i).collided(((Float)personsX.get(j)).floatValue(), .75f, ((Float)personsY.get(j)).floatValue())) {
        coins.get(i).changeState(1);
        //deleteCoins.add(i);
      }
    }
    
  }
  
  
  
  personsX.clear();
  personsY.clear();
  
  if((state == 1) && (millis() - initStarTime > starDuration)){
    stars.clear();
    createCoinGrid(-2.5f + .75f/4.0f, 2.5f+ .75f/4.0f, -4.5f, 4.5f, 1.1f, 1.1f);  
    state = 0;
  }  
  for(int i = 0; i < stars.size(); i++){
  
    stars.get(i).drawStar();
  }
  
  int offSet = 0;
  
  for(int i = 0; i < coins.size(); i++){
  
    if(coins.get(i).getState() == 2){
      coins.remove(i);
      i--;
      if(myPort != null) {
        myPort.write('1');
        myPort.write('5');
        myPort.write('a');
      }
    }    
      
  }
  deleteCoins.clear();
  
     

}

public float[] calcMousePointerAtFloor(float posX, float posY){

  float[] mousePointer = new float[2];
  
  //onlyFromTop for now
  float alpha = AoVY/2.0f;
  
  float h = tan(alpha);
  mousePointer[1] = -cameraPosY*(posY - (height/2.0f))*h/( height/2.0f);
  
  float aspR = (1.0f*height)/(1.0f*width);
  
  float w = h/aspR;
  mousePointer[0] = cameraPosY*(posX - (width/2.0f))*w/( width/2.0f);
  
  return mousePointer;
      
}

public void createCoinGrid(float minX, float maxX, float minZ, float maxZ, float stepX, float stepZ){

  for( float i = minX; i < maxX; i+= stepX){
    for(float j = minZ; j < maxZ; j+= stepZ){
      
      coins.add(new Coin(this, i, .75f, j));
      
    }
  }
  
}

public void createRandomStars(){
  
  if(myPort != null) {
     myPort.write('3');
     myPort.write('6');
     myPort.write('c');
     myPort.write('6');
     myPort.write('6');
  }
  
  state = 1;
  for(int i = 0; i < 35; i++)  stars.add(new Star(this, random(-2.5f, 2.5f), .75f, random(-4.5f, 4.5f), random(-.5f, .5f), 0, random(-.5f, .5f) ));  

  initStarTime = millis();
  
}


class Coin {

  float posX;
  float posZ;

  float posY =  .75f;
  
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
      model.scale(.2f);
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
  
  public void changeState(int val){state = val;}
  public int getState(){return state;}

  public void drawCoin() {
    
    if(state == 0){
        
      angY += (0.75f/30.0f)*2*PI*sin(angAlpha);
      if(angY > 2*PI) angY = 2*PI - angY;
      angAlpha += (0.5f/30.0f)*(PI);
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
      
      posY += 0.35f;
      
      if(posY > sizeY*2.5f) state = 2;
          
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
  
  public boolean collided(float x, float y, float z){
  
    if( (x > (posX - sizeX*1.5f) ) && (x < (posX + sizeX*1.5f) )
        && (y > (posY - sizeY*1.5f) ) && (y < (posY + sizeY*1.5f) ) 
        && (z > (posZ - sizeZ*1.5f) ) && (z < (posZ + sizeZ*1.5f) ) ) {
          return true;}
        else {return false;}    
  }
}

class Floor{

  float blockW = .6f;
  float blockH = .6f;
  float blockT = .6f;
  
  float floorMinW = -2.5f + .75f/5.0f;
  float floorMinH = -4.5f;
  
  float floorMaxW = 2.5f+ .75f/5.0f;
  float floorMaxH = 4.5f;
  
  PImage floorTex;
  
  float minHeight = -100;
  
  float[][] floorCoord;
  
  Floor(){
  
    floorCoord = new float[PApplet.parseInt((floorMaxW - floorMinW)/blockW)][PApplet.parseInt((floorMaxH - floorMinH)/blockH)];
    floorTex = loadImage("Mario-block-.gif");
    
  }
  
  public void updateFloor(){
    
//    for(int i = 0; i < blockNum; i++){
//    }
    
  }
  
  public void drawFloor(){
  
    
    for(float i = floorMinW; i < floorMaxW; i += blockW){
    
      for(float j = floorMinH; j < floorMaxH; j += blockH){
      
        pushMatrix();  
        translate(i,0,j);
        drawblock();
        popMatrix();       
        
      }
      
    }
    
  }
  
  
  public void drawblock(){
    
    hint(DISABLE_DEPTH_TEST);
  
      beginShape(QUADS);
      
        texture(floorTex);        
        
//        //front
//        vertex(-blockW/2.0, -blockT/2.0, blockH/2.0, 0,0);
//        vertex(blockW/2.0, -blockT/2.0, blockH/2.0, floorTex.width,0);
//        vertex(blockW/2.0, blockT/2.0, blockH/2.0, floorTex.width,floorTex.height);
//        vertex(-blockW/2.0, blockT/2.0, blockH/2.0, 0,floorTex.height);    
//          
//        //back
//        vertex(-blockW/2.0, -blockT/2.0, -blockH/2.0, 0,0);
//        vertex(blockW/2.0, -blockT/2.0, -blockH/2.0, floorTex.width,0);
//        vertex(blockW/2.0, blockT/2.0, -blockH/2.0, floorTex.width,floorTex.height);
//        vertex(-blockW/2.0, blockT/2.0, -blockH/2.0, 0,floorTex.height);  
//      
//        //right
//        vertex(blockW/2.0, -blockT/2.0, -blockH/2.0, 0,0);
//        vertex(blockW/2.0, blockT/2.0, -blockH/2.0, floorTex.width,0);
//        vertex(blockW/2.0, blockT/2.0, blockH/2.0, floorTex.width, floorTex.height);
//        vertex(blockW/2.0, -blockT/2.0, blockH/2.0, 0,floorTex.height);    
//        
//        //left
//        vertex(-blockW/2.0, -blockT/2.0, -blockH/2.0, 0,0);
//        vertex(-blockW/2.0, blockT/2.0, -blockH/2.0, floorTex.width,0);
//        vertex(-blockW/2.0, blockT/2.0, blockH/2.0, floorTex.width, floorTex.height);
//        vertex(-blockW/2.0, -blockT/2.0, blockH/2.0, 0,floorTex.height);   
//        
//        //under
//        vertex(-blockW/2.0, -blockT/2.0, -blockH/2.0, 0,0);
//        vertex(blockW/2.0, -blockT/2.0, -blockH/2.0, floorTex.width,0);
//        vertex(blockW/2.0, -blockT/2.0, blockH/2.0, floorTex.width,floorTex.height);
//        vertex(-blockW/2.0, -blockT/2.0, blockH/2.0, 0,floorTex.height);        
//        
        //top
        vertex(-blockW/2.0f, blockT/2.0f, -blockH/2.0f, 0,0);
        vertex(blockW/2.0f, blockT/2.0f, -blockH/2.0f, floorTex.width,0);
        vertex(blockW/2.0f, blockT/2.0f, blockH/2.0f, floorTex.width,floorTex.height);
        vertex(-blockW/2.0f, blockT/2.0f, blockH/2.0f, 0,floorTex.height);        
        
      endShape();

  }
  
  
}



class Star{
  
  PVector pos;
  
  float maxY = 2.0f;
  
  PVector to;
  float speed = 0.1f;
  
  
  float ang;
  
  OBJModel model;
  BoundingBox bbox;
  
  Star(PApplet app, float posX, float posY, float posZ, float toX, float toY, float toZ){
    pos = new PVector(posX, posY, posZ);
    to = new PVector(toX, toY, toZ);
    ang = 0;
    
     model = new OBJModel(app, "star.obj");
      model.scale(.2f);
    
  }
  
  public void drawStar(){
  
    pos.x += to.x*speed;
    pos.y += to.y*speed;
    pos.z += to.z*speed;
    
    ang += (1.0f/30.0f)*PI;
    if(ang > PI*2) ang = ang - PI*2;
    
    pushMatrix();

    translate(pos.x, pos.y + maxY*abs(cos(ang)), pos.z);
    rotateX(PI/5);
    model.draw();
    
    popMatrix();    
    
  }
   

}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "coinGame" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
