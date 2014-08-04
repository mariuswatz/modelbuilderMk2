boolean doPerVertexColor=false;

void keyPressed() {
  // toggle 
  if (key==' ') {
    doPerVertexColor=!doPerVertexColor;
    if (doPerVertexColor) {
      geo.enable(UMB.COLORVERTEX);
      geo.disable(UMB.COLORFACE );
    }
    else {
      geo.disable(UMB.COLORVERTEX );
      geo.enable(UMB.COLORFACE );
    }
  }

  // re-build if any non-special key is pressed
  else if (key!=CODED) {
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
