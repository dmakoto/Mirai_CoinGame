class Floor{

  float blockW = .6;
  float blockH = .6;
  float blockT = .6;
  
  float floorMinW = -2.5 + .75/5.0;
  float floorMinH = -4.5;
  
  float floorMaxW = 2.5+ .75/5.0;
  float floorMaxH = 4.5;
  
  PImage floorTex;
  
  float minHeight = -100;
  
  float[][] floorCoord;
  
  Floor(){
  
    floorCoord = new float[int((floorMaxW - floorMinW)/blockW)][int((floorMaxH - floorMinH)/blockH)];
    floorTex = loadImage("Mario-block-.gif");
    
  }
  
  void updateFloor(){
    
//    for(int i = 0; i < blockNum; i++){
//    }
    
  }
  
  void drawFloor(){
  
    
    for(float i = floorMinW; i < floorMaxW; i += blockW){
    
      for(float j = floorMinH; j < floorMaxH; j += blockH){
      
        pushMatrix();  
        translate(i,0,j);
        drawblock();
        popMatrix();       
        
      }
      
    }
    
  }
  
  
  void drawblock(){
    
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
        vertex(-blockW/2.0, blockT/2.0, -blockH/2.0, 0,0);
        vertex(blockW/2.0, blockT/2.0, -blockH/2.0, floorTex.width,0);
        vertex(blockW/2.0, blockT/2.0, blockH/2.0, floorTex.width,floorTex.height);
        vertex(-blockW/2.0, blockT/2.0, blockH/2.0, 0,floorTex.height);        
        
      endShape();

  }
  
  
}



