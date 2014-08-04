
void keyPressed() {
  // build if any non-special key is pressed
  if(key!=CODED) build();
}

void drawCredit() {
  fill(255);
  textAlign(RIGHT);
  text(UMB.version(), width-5, 15);
  textAlign(LEFT);
  text(this.getClass().getSimpleName(), 5, 15);
}

