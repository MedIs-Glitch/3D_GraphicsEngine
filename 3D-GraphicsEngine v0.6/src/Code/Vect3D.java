package Code;

public class Vect3D {
	
	protected double x;
	public double tx = 0;
	protected double rx = 0;
	protected double y;
	public double ty = 0;
	protected double ry = 0;
	protected double z;
	public double tz = 0;
	protected double rz = 0;
	protected double w;
	
	public static int idCount = 0;
	protected int id;
	public boolean isSelected = false;
	
	public Vect3D(double x, double y, double z) {
		this.x=x;
		this.y=y;
		this.z=z;
		this.w=1;
		id = idCount++;
	}
	
	public Vect3D(double x, double y, double z, double w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		id = idCount++;
	}
	public Vect3D() {
		this.x=0;
		this.y=0;
		this.z=0;
		this.w=1;
	}
	public Vect3D(Vect3D vect3d) {
		this.x=vect3d.x;
		this.y=vect3d.y;
		this.z=vect3d.z;
		this.w=vect3d.w;
		id = idCount++;
	}
	 // Copy method to copy the properties of another Vect3D object into this one
    public void copy(Vect3D source) {
        this.x = source.x;
        this.y = source.y;
        this.z = source.z;
        this.w = source.w;
    }
	public Vect3D VectorMultiplyMatrix(Matrix4 m) {
		return new Vect3D(
				this.x* m.m[0][0] + this.y * m.m[1][0] +this.z * m.m[2][0] + this.w * m.m[3][0],
				this.x* m.m[0][1] + this.y * m.m[1][1] +this.z * m.m[2][1] + this.w * m.m[3][1],
				this.x* m.m[0][2] + this.y * m.m[1][2] +this.z * m.m[2][2] + this.w * m.m[3][2],
				this.x* m.m[0][3] + this.y * m.m[1][3] +this.z * m.m[2][3] + this.w * m.m[3][3]
				);
	}
	public Vect3D normalize() {
		double f= Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z);
	   	return new Vect3D(this.x/=f, this.y/=f, this.z/=f);
	}
	public double calculateDotProduct(Vect3D normal2) {
		return this.x*normal2.x + this.y*normal2.y + this.z*normal2.z;
	}
	public Vect3D vectorMul(double k) {
		return new Vect3D(this.x*k,this.y*k,this.z*k);
	}
	public Vect3D vectorDiv(double k) {
		return new Vect3D(this.x/k,this.y/k,this.z/k);
	}
	public Vect3D vectorAdd(Vect3D v2) {
		return new Vect3D(this.x+v2.x,this.y+v2.y,this.z+v2.z);
	}
	public Vect3D vectorSub(Vect3D v2) {
		return new Vect3D(this.x-v2.x,this.y-v2.y,this.z-v2.z);
	}
	public Vect3D vectorCrossProduct(Vect3D v2) {
		return new Vect3D(
				this.y*v2.z - this.z*v2.y,
				this.z*v2.x - this.x*v2.z,
				this.x*v2.y - this.y*v2.x
				);
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
	@Override
	public String toString() {
		return "Vect3D [x=" + x + ", y=" + y + ", z=" + z + ", w="+ w + "]";
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public Vect3D transform(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}
	
	public Vect3D translate(double x, double y, double z) {
		this.tx += x;
		this.ty += y;
		this.tz += z;
		return this;
	}
	
	public boolean equals(Vect3D v) {
		return x == v.x && y == v.y && z == v.z;
	}
	
	public Vect3D translated(double x, double y, double z) {
		return new Vect3D(this.x + x, this.y + y, this.z + z);
	}
	
	public double dist2D(Vect3D v) {
		
		double xDifference = v.getX() - this.getX();
        double yDifference = v.getY() - this.getY();

        return Math.sqrt(xDifference * xDifference + yDifference * yDifference);
	}
	
	public int getId() {
		return this.id;
	}
	
	public Vect3D scaleX(double scaleX) {
		return new Vect3D(this.x * scaleX, this.y, this.z);
		
	}
	
	public Vect3D scaleY(double scaleY) {
		return new Vect3D(this.x , this.y * scaleY, this.z);
	}
	
	public Vect3D scaleZ(double scaleZ) {
		return new Vect3D(this.x , this.y, this.z * scaleZ);
	}
}
