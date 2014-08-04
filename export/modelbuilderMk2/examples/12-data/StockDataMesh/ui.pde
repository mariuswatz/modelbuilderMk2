
void keyPressed() {
  build();
}

void drawCredit() {
  fill(255);
  textAlign(RIGHT);
  text(UMB.version(), width-5, 15);
  textAlign(LEFT);
  text(this.getClass().getSimpleName(), 5, 15);
  
  String dataStr=filename+" | "+
    values.size()+" values ["+
    nf(minVal,0,2)+" > "+nf(maxVal,0,2)+"]";
  text(dataStr, 5, height-5);
}
