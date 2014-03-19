
void keyPressed() {
  if(key!=CODED) build();
}

void drawCredit() {
  if(frameCount<5)   textFont(createFont("arial",11,false));
  fill(255);
  textAlign(RIGHT);
  text(UMB.version(), width-5, 15);
  text("any key = randomize", width-5,height-5);

  textAlign(LEFT);
  text(this.getClass().getSimpleName(), 5, 15);
}
