
void build() {  
  UVertexList vl=new UVertexList();

  int n=(int)random(5, 20)*100;
  float w=width/2;

  // We'll use the "golden angle" to create a good circular
  // distribution - see comment below    
  float PHI=1.6180339887498948482; // the golden ratio
  float GOLDENANGLE=TWO_PI/(PHI*PHI);
  println("GOLDENANGLE="+degrees(GOLDENANGLE));


  // create a random circular distribution of points
  // noise(x,y) is used to calculate Z positions
  float noiseScale=random(1,20)/200.0;
  
  for (int i=0; i<n; i++) {
    // 
    float deg=(float)i*GOLDENANGLE;
    float rad=random(0.1f, 1)*w;
    vl.add(new UVertex(rad, 0, 0).rot(deg));      
    
    // calculate Z as a function of noise(x,y)*noiseScale
    float noiseX=(vl.last().x+frameCount%10)*noiseScale;
    float noiseY=(vl.last().y+frameCount%10)*noiseScale;
    vl.last().z=noise(noiseX,noiseY)*w*0.5;
  }

  int t=millis();
  geo=new UTriangulate(vl).mesh;
  println("Triangulation of "+vl.size()+" vertices, "+
    (millis()-t)+" msec elapsed.");
}

/* 

 The "golden angle" is produced by applying the golden ratio (PHI) to a circle.
 It is approximated as TWO_PI/(PHI*PHI) (circa 137.50775 in degrees)
 http://en.wikipedia.org/wiki/Golden_angle
 http://en.wikipedia.org/wiki/Golden_ratio
 
 A sequence of "n" points that are rotated so that the angle "a" at index "i" 
 is a = i*GOLDENANGLE guarantees a good circular distribution.
 
 For instance, the Fibonacci spiral pattern seen in sunflowers etc. is produced 
 by a distribution of polar coordinates (radius,angle)  where: 
 
 radius = constantDistance * index
 angle = goldenAngle * index
 
 In XY coordinates:
 
 radius = constantDistance * index
 x=cos(goldenAngle * index)*radius
 y=sin(goldenAngle * index)*radius
 
 */

