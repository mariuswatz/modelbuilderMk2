
void keyPressed() {
  if(key==' ') doDrawModel=!doDrawModel;
  else if(key!=CODED) build();
}

void drawCredit() {
  if(frameCount<5)   textFont(createFont("arial",11,false));
  fill(255);
  textAlign(RIGHT);
  text(UMB.version(), width-5, 15);
  text("SPACE = wireframe on/off, any other key = randomize",
    width-5,height-5);
  textAlign(LEFT);
  text(this.getClass().getSimpleName(), 5, 15);
}

