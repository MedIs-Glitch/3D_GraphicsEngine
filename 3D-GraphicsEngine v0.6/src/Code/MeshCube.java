package Code;
import java.awt.Color;
import java.util.ArrayList;

public class MeshCube {
	private ArrayList<Triangle>Obj;

	public MeshCube() {
		Obj=new ArrayList<Triangle>();
	}

	public void generateTriangle(double pos, double zero) {
		 //south
	    Obj.add(new Triangle(new Vect3D(zero, zero, zero), new Vect3D(zero, pos, zero),new Vect3D(pos, pos, zero), Color.getHSBColor(1f, 0.8f, 0.8f)));
	    Obj.add(new Triangle(new Vect3D(zero, zero, zero), new Vect3D(pos, pos, zero),new Vect3D(pos, zero, zero), Color.getHSBColor(1f, 0.8f, 0.8f)));
	  
	    //east
	    Obj.add(new Triangle(new Vect3D(pos, zero, zero), new Vect3D(pos, pos, zero),new Vect3D(pos, pos, pos), Color.red));
	    Obj.add(new Triangle(new Vect3D(pos, zero, zero), new Vect3D(pos, pos, pos),new Vect3D(pos, zero, pos), Color.red));
	  
	    //north
	    Obj.add(new Triangle(new Vect3D(pos, zero, pos), new Vect3D(pos, pos, pos),new Vect3D(zero, pos, pos), Color.blue));
	    Obj.add(new Triangle(new Vect3D(pos, zero, pos), new Vect3D(zero, pos, pos),new Vect3D(zero, zero, pos), Color.blue));
	  
	    //west
	    Obj.add(new Triangle(new Vect3D(zero, zero, pos), new Vect3D(zero, pos, pos),new Vect3D(zero, pos, zero), Color.cyan));
	    Obj.add(new Triangle(new Vect3D(zero, zero, pos), new Vect3D(zero, pos, zero),new Vect3D(zero, zero, zero), Color.cyan));
	    
	    //top
	    Obj.add(new Triangle(new Vect3D(zero, pos, zero), new Vect3D(zero, pos, pos),new Vect3D(pos, pos, pos), Color.green));
	    Obj.add(new Triangle(new Vect3D(zero, pos, zero), new Vect3D(pos, pos, pos),new Vect3D(pos, pos, zero), Color.green));
	    
	    //buttom
	    Obj.add(new Triangle(new Vect3D(pos, zero, pos), new Vect3D(zero, zero, pos),new Vect3D(zero, zero, zero), Color.yellow));
	    Obj.add(new Triangle(new Vect3D(pos, zero, pos), new Vect3D(zero, zero, zero),new Vect3D(pos, zero, zero), Color.yellow));
	}
	
	public ArrayList<Triangle> getObj() {
		return Obj;
	}

	public void setObj(ArrayList<Triangle> obj) {
		Obj = obj;
	}
}
