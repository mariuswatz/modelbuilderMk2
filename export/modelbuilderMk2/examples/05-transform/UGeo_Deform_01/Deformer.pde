// simple magnetic deformation based on a force
// with a given location and radius of effect

class Deformer {
  UVertex loc;
  float rad, mult;

  Deformer(UVertex input) {
    loc=input;
    if (random(100)<80) rad=random(50, 100);
    else rad=random(200, 300);
    
    // force multiplier
    mult=random(0.4f, 1);
  }

  void deform(UGeo model) {
    for (UVertex vv:model.getV()) {
      float d=loc.dist(vv);
      if (d<rad) {
        d=d/rad; // normalized value indicating distance
        
        // force applied is inversely proportional to distance,
        // squared and multiplied by a parameter to give subtle effect 
        float force=sq(1-d)*mult;

        // get the delta vector betw. loc and vv
        UVertex delta=vv.copy().sub(loc);

        // add the delta vector scaled by calculated force
        vv.add(delta.mult(force));
      }
    }
  }
}
