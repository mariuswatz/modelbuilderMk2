
void keyPressed() {
  if(key==' ') doDrawModel=!doDrawModel;
  else if(key!=CODED) build();
}

void drawCredit() {
  fill(255);
  textAlign(RIGHT);
  text(UMB.version(), width-5, 15);
  textAlign(LEFT);
  text(this.getClass().getSimpleName(), 5, 15);
}

