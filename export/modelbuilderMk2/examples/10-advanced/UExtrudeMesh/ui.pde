void drawCredit() {
  if(frameCount<5)   textFont(createFont("arial",11,false));
  fill(255);
  textAlign(RIGHT);
  text(UMB.version(), width-5, 15);
  
  textAlign(LEFT);
  text(this.getClass().getSimpleName(), 5, 15);
}

void keyPressed() {
  if(key!=CODED && key!=ESC) build();
}
