import java.util.Map;

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
float movementSpeed = 2.0;
HashMap<String, Boolean> keys;
PShape car;

void setup() {
  background(66, 135, 245);
  size(600, 600, P3D);
  cols = w / scl;
  rows = h / scl;
  terrain = new float[cols][rows];
  textureImg = loadImage("grass.png");
  carX = w/2;
  initializeKeys();
  car = loadShape("Car.obj");
}

void draw() {
  flying -= 0.1;
  float yOff = flying;
  for (int y = 0; y < rows; y++) {
    float xOff = 0;
    for (int x = 0; x < cols; x++) {
       terrain[x][y] = map(noise(xOff,yOff), 0, 1, minRidgeHeight, maxRidgeHeight);
       xOff += 0.2;
    }
    yOff += 0.2;
  }
  
  background(66, 135, 245);
  translate(width/2, height/2);
  rotateX(PI/3);
  translate(-w/2, -h/2);
  for (int x = 0; x < cols-1; x++) {
    beginShape(TRIANGLE_STRIP);
    if (x*scl > w*0.40 && x*scl < w*0.60) {
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
        if (keyEntry.getKey().equals(LEFT + "") && carX >= (w*0.45)) { carX -= movementSpeed; }
        if (keyEntry.getKey().equals(RIGHT + "") && carX <= (w*0.55)) { carX += movementSpeed; }
    }
  }
 
  noStroke();
  lights();
  translate(carX, h*0.80, 50);
  scale(20);
  rotateX(PI/2);
  shape(car);
}

void initializeKeys() {
  keys = new HashMap<String, Boolean>();
  keys.put(LEFT + "", false);
  keys.put(RIGHT + "", false); 
}

void keyPressed() {
  if (checkKey()) {
    keys.put(keyCode + "", true);
  }
}

void keyReleased() {
  if (checkKey()) {
    keys.put(keyCode + "", false);
  }
}

boolean checkKey() {
  if (key !=  CODED) return false;
  Boolean isPressed = keys.get(keyCode + "");
  if (isPressed == null) {
    return false;
  }
  return true;
}
