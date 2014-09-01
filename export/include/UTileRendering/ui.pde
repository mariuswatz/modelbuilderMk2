void mousePressed() {
  for(int i=0; i<10; i++) deform(random(1,5));
}

void keyPressed() {
  if (key=='s' || key=='S') renderTiles();

  else if (key==' ') {
    build();
    colorMesh();
  } else if (key=='a')     colorMesh();
}

void drawCredit() {
  if (frameCount<2)   textFont(createFont("Verdana", 11, false));

  fill(255);
  textSize(11);
  textAlign(RIGHT);
  text(UMB.version(), width-5, 15);
  textAlign(LEFT);
  text(this.getClass().getSimpleName(), 5, 15);

  text("SPACE to randomize, 'S' to save high-res", 5, height-5);
}
