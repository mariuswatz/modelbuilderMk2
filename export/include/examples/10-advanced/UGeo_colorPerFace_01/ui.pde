
void keyPressed() {
  // toggle 
  if(key==' ') doPerVertexColor=!doPerVertexColor;
  // re-build if any non-special key is pressed
  if(key!=CODED) {
    build();
    colorMesh();
  }
}

void drawCredit() {
  if (frameCount<2)   textFont(createFont("courier", 11, false));

  fill(255);
  textAlign(RIGHT);
  text(UMB.version(), width-5, 15);
  textAlign(LEFT);
  text(this.getClass().getSimpleName(), 5, 15);
}

