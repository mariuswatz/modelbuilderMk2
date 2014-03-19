
void build() {  
  geo=new UGeo();

  geo.add(UGeo.box(150, 200, 150).
    rotZ(radians(45)).rotX(radians(45)).
    translate(-100, 125));
  geo.add(UGeo.cyl(150, 200, 12).
    rotZ(radians(45)).rotX(radians(45)).
    translate(100, 125));

  geo.add(
  UGeoGenerator.meshPlane(150, 200, 3).
    rotZ(radians(45)).rotX(radians(45)).
    translate(-100, -125));

  geo.add(
  UGeoGenerator.meshBox(150, 200, 150, 3).
    rotZ(radians(45)).rotX(radians(45)).
    translate(100, -125));
}
