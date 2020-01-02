import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.Map; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Terrain extends PApplet {



int cols, rows;
int scl = 20;
int w = 1200;
int h = 900;

PImage textureImg;
float[][] terrain;
float flying = 0;
int carX, carY, carZ;
int carRadius = 50;
int minRidgeHeight = -50;
int maxRidgeHeight = 50;
float movementSpeed = 2.0f;
HashMap<String, Boolean> keys;
PShape car;

public void setup() {
  background(66, 135, 245);
  
  cols = w / scl;
  rows = h / scl;
  terrain = new float[cols][rows];
  textureImg = loadImage("grass.png");
  carX = w/2;
  initializeKeys();
  car = loadShape("Car.obj");
}

public void draw() {
  flying -= 0.1f;
  float yOff = flying;
  for (int y = 0; y < rows; y++) {
    float xOff = 0;
    for (int x = 0; x < cols; x++) {
       terrain[x][y] = map(noise(xOff,yOff), 0, 1, minRidgeHeight, maxRidgeHeight);
       xOff += 0.2f;
    }
    yOff += 0.2f;
  }
  
  background(66, 135, 245);
  translate(width/2, height/2);
  rotateX(PI/3);
  translate(-w/2, -h/2);
  for (int x = 0; x < cols-1; x++) {
    beginShape(TRIANGLE_STRIP);
    if (x*scl > w*0.40f && x*scl < w*0.60f) {
      fill(0);
      stroke(255);
    } else {
      stroke(0);
      texture(textureImg);
    }
    for (int y = 0; y < rows; y++) {
      vertex(x*scl, y*scl, terrain[x][y]);
      vertex((x+1)*scl, y*scl, terrain[x+1][y]);
    }
    endShape();
  }
  
  for (Map.Entry<String, Boolean> keyEntry : keys.entrySet()) {
    if (keyEntry.getValue()) {
        if (keyEntry.getKey().equals(LEFT + "") && carX >= (w*0.45f)) { carX -= movementSpeed; }
        if (keyEntry.getKey().equals(RIGHT + "") && carX <= (w*0.55f)) { carX += movementSpeed; }
    }
  }
 
  noStroke();
  lights();
  translate(carX, h*0.80f, 50);
  scale(20);
  rotateX(PI/2);
  shape(car);
}

public void initializeKeys() {
  keys = new HashMap<String, Boolean>();
  keys.put(LEFT + "", false);
  keys.put(RIGHT + "", false); 
}

public void keyPressed() {
  if (checkKey()) {
    keys.put(keyCode + "", true);
  }
}

public void keyReleased() {
  if (checkKey()) {
    keys.put(keyCode + "", false);
  }
}

public boolean checkKey() {
  if (key !=  CODED) return false;
  Boolean isPressed = keys.get(keyCode + "");
  if (isPressed == null) {
    return false;
  }
  return true;
}
  public void settings() {  size(600, 600, P3D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Terrain" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
