
void keyPressed() {
  // build if any non-special key is pressed
  if(key!=CODED) randomSVG();
}

void drawCredit() {
  if(frameCount<2)   textFont(createFont("courier", 11, false));

  fill(255);
  textAlign(RIGHT);
  text(UMB.version()+" + Geomerative", width-5, 15);
  textAlign(LEFT);
  text(this.getClass().getSimpleName(), 5, 15);
}

