import saito.objloader.*;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import java.util.*;

import processing.serial.*;

import tsps.*;

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
float cameraPosY = 10.0;
float cameraPosZ;

Serial myPort;       

float initStarTime;
float starDuration = 5000.0;
int state  = 0;


void setup(){
  
  //size(800,600, P3D);
  size(1024,1536 , P3D);
  
  
  floor = new Floor();
    
  perspective(AoVY, float(width)/float(height), 
            0.01, 100);
            
  noStroke();
  
  frameRate(30);
  
  coins = new ArrayList();
  deleteCoins = new ArrayList();
  
  personsX = new ArrayList();
  personsY = new ArrayList();
  
  stars = new ArrayList();
  
  createCoinGrid(-2.5 + .75/4.0, 2.5+ .75/4.0, -4.5, 4.5, 1.1, 1.1);  
  
  myPort = null;
  if(Serial.list().length > 0) myPort = new Serial(this, Serial.list()[0], 57600);
  
  tspsReceiver= new TSPS(this, 12000);
  
}

void draw(){
  
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
      createCoinGrid(-2.5 + .75/4.0, 2.5+ .75/4.0, -4.5, 4.5, 1.1, 1.1);  
    }
    else if(key == 's' && (state == 0)) {
      coins.clear();
      createRandomStars();      
    }
  }
  
  background(0,175,200);
  camera(0.0, 10.0, 0.0, 
         0.0, -1.0, 0.0, 
         0.0, .0, -1.0);
         
    
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
    translate(personPos[0], .75,personPos[1]);
    scale(.3,.3,.3);
    box(1.0);
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
    if(coins.get(i).collided(mouseAtFloor[0], .75, mouseAtFloor[1])) {
      
      coins.get(i).changeState(1);
      //deleteCoins.add(i);
    }
    
    for(int j = 0; j < personsX.size(); j++){
      if(coins.get(i).collided(((Float)personsX.get(j)).floatValue(), .75, ((Float)personsY.get(j)).floatValue())) {
        coins.get(i).changeState(1);
        //deleteCoins.add(i);
      }
    }
    
  }
  
  
  
  personsX.clear();
  personsY.clear();
  
  if((state == 1) && (millis() - initStarTime > starDuration)){
    stars.clear();
    createCoinGrid(-2.5 + .75/4.0, 2.5+ .75/4.0, -4.5, 4.5, 1.1, 1.1);  
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

float[] calcMousePointerAtFloor(float posX, float posY){

  float[] mousePointer = new float[2];
  
  //onlyFromTop for now
  float alpha = AoVY/2.0;
  
  float h = tan(alpha);
  mousePointer[1] = -cameraPosY*(posY - (height/2.0))*h/( height/2.0);
  
  float aspR = (1.0*height)/(1.0*width);
  
  float w = h/aspR;
  mousePointer[0] = cameraPosY*(posX - (width/2.0))*w/( width/2.0);
  
  return mousePointer;
      
}

void createCoinGrid(float minX, float maxX, float minZ, float maxZ, float stepX, float stepZ){

  for( float i = minX; i < maxX; i+= stepX){
    for(float j = minZ; j < maxZ; j+= stepZ){
      
      coins.add(new Coin(this, i, .75, j));
      
    }
  }
  
}

void createRandomStars(){
  
  if(myPort != null) {
     myPort.write('3');
     myPort.write('6');
     myPort.write('c');
     myPort.write('6');
     myPort.write('6');
  }
  
  state = 1;
  for(int i = 0; i < 35; i++)  stars.add(new Star(this, random(-2.5, 2.5), .75, random(-4.5, 4.5), random(-.5, .5), 0, random(-.5, .5) ));  

  initStarTime = millis();
  
}


