package Code;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JPanel;

import Design.PanelEngine;

public class Grid3D {
	RenderPanel renderPanel;
	public class GridLine3D{
		
		public Vect3D v1, v2;
		public Color c;

		public GridLine3D(Vect3D v1, Vect3D v2) {
			this.v1 = v1;
			this.v2 = v2;
			c = Color.LIGHT_GRAY;
		}
		
		public GridLine3D copy() {
			return new GridLine3D(new Vect3D(v1.x, v1.y, v1.z), new Vect3D(v2.x, v2.y, v2.z));
		}
		public void copy(GridLine3D line) {
			this.v1.copy(line.v1);
			this.v2.copy(line.v2);
		}
	}

	private int rows, columns;
	private ArrayList<GridLine3D> lines = new ArrayList<GridLine3D>();
	GridLine3D lv = new GridLine3D(new Vect3D(0,0,0), new Vect3D(0,0,0)),
			   lt = new GridLine3D(new Vect3D(0,0,0), new Vect3D(0,0,0)),
			   lp = new GridLine3D(new Vect3D(0,0,0), new Vect3D(0,0,0));
	
	
	public Grid3D(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		
		
		//lines.add(new GridLine3D(new Vect3D(-100, 0, 100), new Vect3D(-100,0,-100)));
		
		for(int i = -50; i <= 50; i+=5) {
			lines.add(new GridLine3D(new Vect3D(-i, 0, 50), new Vect3D(-i,0,-50)));
			if(i == 0) lines.get(lines.size()-1).c = Color.RED;
		}
		
		for(int i = -50; i <= 50; i+=5) {
			lines.add(new GridLine3D(new Vect3D(-50, 0, -i), new Vect3D(50,0,-i)));
		}
	}
	
	public void drawGrid3D(Graphics2D g2, Matrix4 matWorld, Matrix4 matView, Matrix4 projM, JPanel panel) {
		
		ArrayList<GridLine3D> linesCopy = new ArrayList<GridLine3D>();
		ArrayList<GridLine3D> lines1Clipping = new ArrayList<GridLine3D>();
				
		for(GridLine3D l:lines) {
			linesCopy.add(l.copy());
		}
		for(GridLine3D l:linesCopy) {
			
			
			
			lt.v1 = l.v1.VectorMultiplyMatrix(matWorld);
			lt.v2 = l.v2.VectorMultiplyMatrix(matWorld);
			
			lv.v1 = lt.v1.VectorMultiplyMatrix(matView);
			lv.v2 = lt.v2.VectorMultiplyMatrix(matView);
			
			GridLine3D ls = new GridLine3D(new Vect3D(0,0,0), new Vect3D(0,0,0));
		
			int nClippedLine=0;
			nClippedLine= LineClipAgainstPlane(new Vect3D(0,0,0.3),new Vect3D(0,0,1),lv,ls);
			for(int n=0;n<nClippedLine;n++){
				lp.v1 = ls.v1.VectorMultiplyMatrix(projM);
				lp.v2 = ls.v2.VectorMultiplyMatrix(projM);
				
				lp.v1 = lp.v1.vectorDiv(lp.v1.w);
				lp.v2 = lp.v2.vectorDiv(lp.v2.w);
				
				lp.v1.x *= -1; 
				lp.v2.x *= -1; 
				lp.v1.y *= -1; 
				lp.v2.y *= -1; 
				
				Vect3D vOffsetView = new Vect3D(1.0f,1.0f,0.0f);
				lp.v1 = lp.v1.vectorAdd(vOffsetView);
				lp.v2 = lp.v2.vectorAdd(vOffsetView);
				
				lp.v1.x *= 0.5f * (double) panel.getWidth();
				lp.v1.y *= 0.5f * (double) panel.getHeight() * ((double)panel.getWidth()/(double)panel.getHeight());
				lp.v2.x *= 0.5f * (double) panel.getWidth();
				lp.v2.y *= 0.5f * (double) panel.getHeight() * ((double)panel.getWidth()/(double)panel.getHeight());
			}
			lines1Clipping.add(lp.copy());		
		}
		LinkedList<GridLine3D> listLines= new LinkedList<GridLine3D>();
		for(GridLine3D l:lines1Clipping) {
			GridLine3D linesc1;
			listLines.add(l);
			int nNewLines=1;
			 for(int p=0;p<4;p++) {
					int nLinesToAdd = 0;
				 while(nNewLines > 0) {
					 GridLine3D test =listLines.peek();
						linesc1=new GridLine3D(new Vect3D(0,0,0), new Vect3D(0,0,0));
						listLines.poll(); // Equivalent to pop_front() in C++
						nNewLines--;
						switch (p){
						case 0:
					        nLinesToAdd = LineClipAgainstPlane(new Vect3D(0.0f, 4f, 0.0f), new Vect3D(0.0f, 1.0f, 0.0f), test,linesc1);
					        break;
					    case 1:
					        nLinesToAdd = LineClipAgainstPlane(new Vect3D(0.0f, (double)panel.getHeight() - 4, 0.0f), new Vect3D(0.0f, -1.0f, 0.0f), test,linesc1);
					        break;
					    case 2:
					        nLinesToAdd = LineClipAgainstPlane(new Vect3D(4f, 0.0f, 0.0f), new Vect3D(1.0f, 0.0f, 0.0f), test,linesc1);
					        break;
					    case 3:
					    	nLinesToAdd = LineClipAgainstPlane(new Vect3D((double)panel.getWidth() - 4, 0.0f, 0.0f), new Vect3D(-1.0f, 0.0f, 0.0f), test,linesc1);
					        break;
						}
						for (int w = 0; w < nLinesToAdd; w++) {
							listLines.add(linesc1);
						}		
				 }
				 nNewLines=listLines.size();
			 }
			 for(GridLine3D i:listLines) {
				 	g2.setColor(Color.LIGHT_GRAY);
					g2.drawLine((int)i.v1.x, (int)i.v1.y, (int)i.v2.x, (int)i.v2.y);
			 }
				listLines.clear();
		}          
		lines1Clipping.clear();
	}
	
	public int LineClipAgainstPlane(Vect3D plane_p,Vect3D plane_n, GridLine3D lineE,GridLine3D lineS) {
		plane_n=plane_n.normalize();
		 Vect3D[] inside_points = new Vect3D[2];
	   		for (int i = 0; i < 2; i++) {
	   			inside_points[i] = new Vect3D(); 
	   		}
		    int nInsidePointCount = 0;
		    Vect3D[] outside_points = new Vect3D[2];
		    for (int i = 0; i < 2; i++) {
		    	outside_points[i] = new Vect3D(); 
	   		}
		    int nOutsidePointCount = 0;
		    
		    double d0 = dist(lineE.v1,plane_p,plane_n);
		    double d1 = dist(lineE.v2,plane_p,plane_n);
		    
		    if(d0>=0) {
   		        inside_points[nInsidePointCount++]=lineE.v1;
		    }
		    else {
   		        outside_points[nOutsidePointCount++]=lineE.v1;
   		    }
   		    if (d1 >= 0) {
   		        inside_points[nInsidePointCount++]=lineE.v2;
   		    } else {
   		        outside_points[nOutsidePointCount++]=lineE.v2;
   		    }
   		    if (nInsidePointCount == 0){
   				return 0; 
   			}
   		    if (nInsidePointCount == 2){
				lineS.copy(lineE);
				return 1; 
			}
		    if(nInsidePointCount == 1 & nOutsidePointCount == 1) {
		    	lineS.v1=inside_points[0];
		    	lineS.v2=VectorIntersectPlane(plane_p, plane_n, inside_points[0], outside_points[0]);
		    	return 1;
		    }
		    return -1;
	}
		public Vect3D VectorIntersectPlane(Vect3D plane_p,Vect3D plane_n,Vect3D lineStart, Vect3D lineEnd) {
   			double plane_d = -plane_n.calculateDotProduct(plane_p);
   			double ad = lineStart.calculateDotProduct(plane_n);
   			double bd = lineEnd.calculateDotProduct(plane_n);
   			double t = (-plane_d - ad) / (bd-ad);
   			Vect3D lineStartToEnd = lineEnd.vectorSub(lineStart);
   			Vect3D lineToIntersect = lineStartToEnd.vectorMul(t);
   		    return lineStart.vectorAdd(lineToIntersect);
   		}
   		
	public double dist(Vect3D p, Vect3D plane_p, Vect3D plane_n) {
   	    return plane_n.x * p.x + plane_n.y * p.y + plane_n.z * p.z - plane_n.calculateDotProduct(plane_p);
   	}
}
