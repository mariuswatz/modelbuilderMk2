// File management stuff for demo purposes, not needed
// if you just want to use svgToVL(filename).

String lastFile;
ArrayList<String> filenames;
String dir;

void getFilelist() {
  if (dir==null) dir=sketchPath("data")+"/";
  UMB.logDivider(dir);

  // get list of .svg files in "data" folder
  filenames=UFile.list(dir, null, "svg");

  // print file list
  UMB.log(UMB.str(filenames));
}

void randomSVG() {
  String file=null;
  
  if (lastFile!=null && filenames.size()>0) {
    int index=filenames.indexOf(lastFile)+1;

    file=filenames.get(index%filenames.size());
  }
  else file=filenames.get(0);

  vl=svgToVL(file);
  lastFile=file;
}

