/**
 * ComputationalGeometry
 * A simple, lightweight library for generating meshes such as isometric surfaces, boundary hulls and skeletons.
 * http://thecloudlab.org/processing/library.html
 *
 * Copyright (C) 2013 Mark Collins & Toru Hasegawa http://proxyarch.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 * 
 * @author      Mark Collins & Toru Hasegawa http://proxyarch.com
 * @modified    07/25/2013
 * @version     2 (2)
 */

package ComputationalGeometry;

import java.util.ArrayList;

import processing.core.*;

public class IsoContour implements PConstants{
 
  PVector start, end;
  int cols, rows;
  PVector[] vertexes;
  float[] vertexValues;
  int falloff;  
  PApplet theParent;
  
  
  // MW
  ArrayList<PVector> points;
  
  int edgeToVertexA[] = {
    0, 1, 2, 3, 0, 1, 2, 3
  };  
  int edgeToVertexB[] = {
    0, 1, 2, 3, 1, 2, 3, 0
  };  

  int faces[] =  {
    -1,-1,-1,    -1,-1,-1,   -1,-1,-1,   -1,-1,-1,
    4,8,7,    -1,-1,-1,   -1,-1,-1,   -1,-1,-1,
    6,3,7,    -1,-1,-1,   -1,-1,-1,   -1,-1,-1,
    8,6,4,	4,6,3,   -1,-1,-1,   -1,-1,-1,
    5,2,6,	-1,-1,-1,   -1,-1,-1,   -1,-1,-1,
    4,8,7, 	8,5,7,    7,5,6,   5,6,2,
    5,3,7,	5,2,3,   -1,-1,-1,   -1,-1,-1,
    4,8,3,   8,5,3,    5,2,3,  -1,-1,-1,
    1,5,8,    -1,-1,-1,   -1,-1,-1,   -1,-1,-1,
    1,5,7,    1,7,4,   -1,-1,-1,   -1,-1,-1,
    7,6,3,    8,6,7,  8,5,6,  8,1,5,
    1,5,4,   5,6,4,   4,6,3,   -1,-1,-1,
    1,2,8,   8,2,6,   -1,-1,-1,   -1,-1,-1,
    1,2,6,   1,6,7,   1,7,4,   -1,-1,-1,
    8,1,2,    8,2,7,   2,3,7,   -1,-1,-1,
    1,2,3,    1,3,4,   -1,-1,-1,   -1,-1,-1
  };
  
  public IsoContour(PApplet _theParent, PVector _start, PVector _end, int _cols, int _rows){
    start = _start;
    end = _end;
    falloff = 1;
    theParent = _theParent;
    cols = _cols; 
    rows = _rows;
    
    points=new ArrayList<PVector>();

    this.construct();
    
  }
  
  public void construct(){
    vertexValues = new float[ (cols+1) * (rows+1)];
    vertexes = new PVector[ (cols+1) * (rows+1)];
    float colDistance = (end.x - start.x)/cols;
    float rowDistance = (end.x - start.y)/rows;
    int count = 0;
    for(int i=0; i <= cols; i++){
      for(int j=0; j <= rows; j++){
        vertexes[count] = new PVector(start.x + colDistance*i, start.y + rowDistance*j, start.z);
        count++;
      }
    }
    this.clear();
  }

  public void clear(){
    for(int i=0; i<vertexValues.length; i++){
      vertexValues[i] = 0.0f;
    } 
    
    // MW
    points.clear();
  }

  public ArrayList<PVector> getPoints() {
    return points;
  }
  
  public void addPoint(PVector _pt, float weight){
    
    // for each vertex, figure _pt's contribution
    for (int i=0; i<vertexes.length; i++){
      float d = PApplet.max(1.0f,_pt.dist(vertexes[i]));
      vertexValues[i] += (1.0f / PApplet.pow(d,2))*weight;
    }
    
    // MW
    _pt.z=weight;
    points.add(_pt);
  }
  
  public void addPoint(PVector _pt){
    this.addPoint(_pt, 1.0f);
  }
  
  public void plotGrid(){
	  float colDistance = (end.x - start.x)/cols;
	    float rowDistance = (end.y - start.y)/rows;
	    for(int i=0; i<=cols; i++){
	    	theParent.line(start.x + colDistance*i, start.y, 0, start.x + colDistance*i, end.y, 0);
	    }
	    for(int i=0; i<=rows; i++){
	    	theParent.line(start.x, start.y + rowDistance*i, 0, end.x, start.y + rowDistance*i, 0);
	    }   
  }
  
  public void plot(float threshold){
    for(int i=0; i<cols; i++){
      for(int j=0; j<rows; j++){
        this.lookUpAndDraw(i, j, cols+1, rows+1, threshold);
      }
    }
  }

  public void lookUpAndDraw(int i, int j, int cols, int rows, float threshold){
    int[] ref = new int[4];
    ref[0] =  i*rows + j;  	//lowerleft
    ref[1] =  (i+1)*rows + j;	//lower right
    ref[2] = (i+1)*rows + (j+1);	//upper right
    ref[3] = i*rows + (j+1);	//upper left

    String myString = "";
    for(int k=0; k<4; k++){
      if(vertexValues[ ref[k] ] > threshold){ 
        myString = myString + "1"; 
      }       
      else { 
        myString = myString + "0"; 
      }
    }
    //convert on/off states in string to case number
    int lookUp = PApplet.unbinary(myString);

    //use case number to get triangle data
    int[] triangles = new int[12];
    for(int k=0;k<12;k++){
      triangles[k]=faces[lookUp*12+k];
    }

    //for each of possible 4 triangles, draw it
    for(int k=0;k<4;k++){
      if(triangles[k*3] != -1){
    	theParent.beginShape(TRIANGLES);
        //for each vertex of the triangle
        for(int v=0;v<3;v++){
          // get related vertexes, to do average
          int num1 = edgeToVertexA[ triangles[k*3+v] - 1 ];
          int num2 = edgeToVertexB[ triangles[k*3+v] - 1 ];
          float vtx1P,vtx2P;
          float spread = PApplet.abs(vertexValues[ref[num1]] - vertexValues[ref[num2]]);

          if(spread != 0){
            if (vertexValues[ref[num1]] < vertexValues[ref[num2]]){
              vtx1P = 1-PApplet.abs((threshold - vertexValues[ref[num1]])/spread);
              vtx2P = 1-PApplet.abs((vertexValues[ref[num2]] - threshold)/spread);
            }
            else{
              vtx1P = 1-PApplet.abs((vertexValues[ref[num1]] - threshold)/spread);
              vtx2P = 1-PApplet.abs((threshold - vertexValues[ref[num2]])/spread);
            }
          }
          else{
            vtx1P = .5f;
            vtx2P = .5f;
          }
          float x = (vertexes[ref[num1]].x * vtx1P + vertexes[ref[num2]].x * vtx2P);
          float y = (vertexes[ref[num1]].y * vtx1P + vertexes[ref[num2]].y * vtx2P);
          theParent.vertex(x,y,0.0f);
        }
        theParent.endShape();
      }
    }
  }
}


 