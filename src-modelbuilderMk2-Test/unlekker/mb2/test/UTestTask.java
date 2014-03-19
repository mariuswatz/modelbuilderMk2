/*
 * modelbuilderMk2
 */
package unlekker.mb2.test;

import java.awt.Frame;

import processing.core.PApplet;
import processing.core.PGraphics;
import unlekker.mb2.geo.UCurve;
import unlekker.mb2.geo.UFace;
import unlekker.mb2.geo.UGeo;
import unlekker.mb2.geo.UIntersections;
import unlekker.mb2.geo.UNav3D;
import unlekker.mb2.geo.UTriangulate;
import unlekker.mb2.geo.UVertex;
import unlekker.mb2.geo.UVertexList;
import unlekker.mb2.util.UMB;
import unlekker.mb2.util.UTask;

public class UTestTask extends UTest {
  UTask task;
  float perc=0;
  
  public void init() {
    task=new UTask(className(this));

  }
  
  public void draw() {
    if(p.frameCount%100==0) {
      task.update(perc);
      perc+=0.1f;
      log("perc "+nf(perc)+" "+task.elapsed+" "+task.elapsedFrames);
    }
    if(perc>=1) {
      logDivider(task.name);
      task.done();
      for(UTask.UTaskEvent ev:task.ev) {
        log(ev.str());
      }
      p.exit();
    }
  }

  
}
