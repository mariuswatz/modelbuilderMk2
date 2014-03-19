void drawCredit() {
  if(frameCount<2)   textFont(createFont("courier", 11, false));

  fill(255);
  textAlign(RIGHT);
  text(UMB.version(), width-5, 15);
  textAlign(LEFT);
  text(this.getClass().getSimpleName(), 5, 15);
}

