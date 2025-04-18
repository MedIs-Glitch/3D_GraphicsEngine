package Code;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.util.Random;

public class Triangle {
	
	MeshObject parent;
	
	public Vect3D[] v=new Vect3D[3];
    protected Color color, OGColor;
    
    public int id, faceid;
    public static int counter = 0;
    
    protected boolean isHovered, isSelected;
    
    public int v1,v2,v3;

	public Triangle(Vect3D v1, Vect3D v2, Vect3D v3, Color color) {
        v[0] = v1;
        v[1] = v2;
        v[2] = v3;
        this.color=color;
        OGColor = this.color;
        id = counter;
        counter++;
    }
	public Triangle(Vect3D v1, Vect3D v2, Vect3D v3, MeshObject parent, int faceid, int i1, int i2, int i3) {
        v[0] = v1;
        v[1] = v2;
        v[2] = v3;
        this.parent = parent;
        this.color=Color.LIGHT_GRAY;
        OGColor = this.color;
        id = counter;
        counter++;
        this.faceid = faceid;
        
        this.v1 = i1;
        this.v2 = i2;
        this.v3 = i3;
        
    }
	
	public Triangle(Vect3D v1, Vect3D v2, Vect3D v3, MeshObject parent, int faceid) {
        v[0] = v1;
        v[1] = v2;
        v[2] = v3;
        this.parent = parent;
        this.color=Color.LIGHT_GRAY;
        OGColor = this.color;
        id = counter;
        counter++;
        this.faceid = faceid;
        
    }
	public Triangle() {
        v[0] = new Vect3D(0,0,0);
        v[1] = new Vect3D(0,0,0);
        v[2] = new Vect3D(0,0,0);
        generateRandomColor();
        this.color=new Color(50,180,50);
        OGColor = this.color;
        id = counter;
        counter++;
    }
	public Triangle(Triangle tri) {
		this.v = new Vect3D[3];
	    for (int i = 0; i < 3; i++) {
	        this.v[i]= new Vect3D(tri.v[i]); // Assuming Vect3D has a copy constructor
	    }
	    this.color = tri.color;
	    OGColor = this.color;
	    id = counter;
        counter++;
	}
	public void copy(Triangle source) {
        this.color = source.color;
        for (int i = 0; i < 3; i++) {
            this.v[i].copy(new Vect3D(source.v[i]));
        }
        OGColor = this.color;
    }
	@Override
	public String toString() {
		return "Triangle [" + v[0].toString() + v[1].toString() + v[2].toString() + "]";
	}
	public void generateRandomColor() {
	     // Create an instance of the Random class
        Random rand = new Random();

        // Generate random values for red, green, and blue components
        int red = rand.nextInt(256); // 0-255
        int green = rand.nextInt(256); // 0-255
        int blue = rand.nextInt(256); // 0-255

        // Create a Color object with the random values
        Color randomColor = new Color(red, green, blue);

        // Set your object's color to the random color
        this.color = randomColor;
	}
	
	public Vect3D getV(int index) {
		return this.v[index];
	}
	
	public Vect3D[] getVs() {
		return v;
	}
	
	public void drawOutlines(Graphics2D g2) {
		Path2D path = new Path2D.Double();
        g2.setColor(Color.CYAN);
        path.moveTo(v[0].x,v[0].y);
        path.lineTo(v[1].x,v[1].y);
        path.lineTo(v[2].x,v[2].y);
        path.closePath();
        g2.draw(path);
	}
	
	public Triangle translate(double x, double y, double z) {
		Triangle tri = new Triangle();
		for (int i = 0; i < 3; i++) {
			tri.v[i].x = v[i].x + x;
			tri.v[i].x = v[i].y + y;
			tri.v[i].x = v[i].z + z;
        }
		return tri;
	}
	
	public boolean equals(Triangle t) {
		return v[0].equals(t.getV(0)) && v[1].equals(t.getV(1)) && v[2].equals(t.getV(2));
	}
}
