ArrayList<String> help;

void drawCredit() {
  if(help==null) {
      help=new ArrayList<String>();
  help.add("n=reset");
  help.add("r=add random face");
  help.add("d=delete random face");
  help.add("g=get random group");
  help.add("r=add random face");
  help.add("a=add adjacent faces");
  help.add("v=add adjacent faces of random vertex");
  help.add("ENTER=subdivide group faces");
  }
  
  if(frameCount<5)   textFont(createFont("arial",11,false));
  fill(255);
  textAlign(RIGHT);
  text(UMB.version(), width-5, 15);
  
  float y=height-5-12*help.size();
  for(String str:help) {
    text(str,width-5,y);
    y=y+12;
  }
  
  textAlign(LEFT);
  text(this.getClass().getSimpleName(), 5, 15);
}
