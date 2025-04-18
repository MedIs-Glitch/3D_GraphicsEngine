package Code;

public class Matrix4 {
	
	protected double[][] m=new double[4][4];

	public Matrix4() {
		
	}
	public static Matrix4 matrixMakeIdentity() {
		Matrix4 m=new Matrix4();
		m.m[0][0] = 1.0f;
		m.m[1][1] = 1.0f;
		m.m[2][2] = 1.0f;
		m.m[3][3] = 1.0f;
		return m;
	}
	public static Matrix4 matrixMakeTranslation(double x, double y, double z) {
		Matrix4 m=new Matrix4();
		m.m[0][0] = 1.0f;
		m.m[1][1] = 1.0f;
		m.m[2][2] = 1.0f;
		m.m[3][3] = 1.0f;
		m.m[3][0] = x;
		m.m[3][1] = y;
		m.m[3][2] = z;
		return m;
	}
	public Matrix4 matrixMakeProjection(double fAspectRatio, double fFar, double fNear, double fFov) {
		Matrix4 m=new Matrix4();
		double fFovRad = 1.0f /Math.tan(fFov * 0.5 / 180.0f * 3.14159);
        m.m[0][0] = fAspectRatio * fFovRad;
        m.m[1][1] = fFovRad;
        m.m[2][2] = fFar / (fFar - fNear);
        m.m[3][2] = (-fFar * fNear) / (fFar - fNear);
        m.m[2][3] = 1.0;
        m.m[3][3] = 0.0;
		return m;
	}
	
	public static Matrix4 initProjection(double fov,double width,double height,double zNear,double zFar) {
		Matrix4 m=new Matrix4();
		double ar = width/height;
		double tanHalfFOV = (double) Math.tan(Math.toRadians(fov/2));
		double zRange = zNear - zFar;
		m.m[0][0] = 1.0f / (tanHalfFOV * ar);
		m.m[1][1] = 1.0f / tanHalfFOV ;
		m.m[2][2] = (-zNear -zFar)/zRange;
		m.m[2][3] = (2* zFar * zNear) / zRange;
		m.m[3][2] =1.0f;
		return m;
	}
	
	public Matrix4 matrixMultiplyMatrix(Matrix4 m1) {
		Matrix4 m=new Matrix4();
		for(int c=0;c<4;c++) {
			for(int r=0;r<4;r++) {
				m.m[r][c]=this.m[r][0] * m1.m[0][c] + this.m[r][1] * m1.m[1][c] + this.m[r][2] * m1.m[2][c] + this.m[r][3] * m1.m[3][c];
			}
		}
		return m;
	}
	public static Matrix4 matrixPointAt(Vect3D pos, Vect3D target,Vect3D up) {
		//calculate new forward
			Vect3D newForward=target.vectorSub(pos);
			newForward.normalize();
		//calculate new up
			Vect3D a=newForward.vectorMul(up.calculateDotProduct(newForward));
			Vect3D newUp=up.vectorSub(a);
			newUp.normalize();
		//New Right Direction
			Vect3D newRight=newUp.vectorCrossProduct(newForward);
		//Construct The Matrix
		Matrix4 matrix=new Matrix4();
		matrix.m[0][0] = newRight.x;	matrix.m[0][1] = newRight.y;	matrix.m[0][2] = newRight.z;	matrix.m[0][3] = 0.0f;
		matrix.m[1][0] = newUp.x;		matrix.m[1][1] = newUp.y;		matrix.m[1][2] = newUp.z;		matrix.m[1][3] = 0.0f;
		matrix.m[2][0] = newForward.x;	matrix.m[2][1] = newForward.y;	matrix.m[2][2] = newForward.z;	matrix.m[2][3] = 0.0f;
		matrix.m[3][0] = pos.x;			matrix.m[3][1] = pos.y;			matrix.m[3][2] = pos.z;			matrix.m[3][3] = 1.0f;
		
		return matrix;
	}
	public Matrix4 matrixQuickInverse() // Only for Rotation/Translation Matrices
	{
		Matrix4 matrix=new Matrix4();
		matrix.m[0][0] = this.m[0][0]; matrix.m[0][1] = this.m[1][0]; matrix.m[0][2] = this.m[2][0]; matrix.m[0][3] = 0.0f;
		matrix.m[1][0] = this.m[0][1]; matrix.m[1][1] = this.m[1][1]; matrix.m[1][2] = this.m[2][1]; matrix.m[1][3] = 0.0f;
		matrix.m[2][0] = this.m[0][2]; matrix.m[2][1] = this.m[1][2]; matrix.m[2][2] = this.m[2][2]; matrix.m[2][3] = 0.0f;
		matrix.m[3][0] = -(this.m[3][0] * matrix.m[0][0] + this.m[3][1] * matrix.m[1][0] + this.m[3][2] * matrix.m[2][0]);
		matrix.m[3][1] = -(this.m[3][0] * matrix.m[0][1] + this.m[3][1] * matrix.m[1][1] + this.m[3][2] * matrix.m[2][1]);
		matrix.m[3][2] = -(this.m[3][0] * matrix.m[0][2] + this.m[3][1] * matrix.m[1][2] + this.m[3][2] * matrix.m[2][2]);
		matrix.m[3][3] = 1.0f;
		return matrix;
	}
	public void MatrixRotationZ(double rotSpeedZ) {
		double radRotZ=Math.toRadians(rotSpeedZ);
		// Rotation Z
		this.m[0][0] = Math.cos(radRotZ);
		this.m[0][1] = Math.sin(radRotZ);
		this.m[1][0] = -Math.sin(radRotZ);
		this.m[1][1] = Math.cos(radRotZ);
		this.m[2][2] = 1;
		this.m[3][3] = 1;
	}
	public void MatrixRotationX(double rotSpeedX) {
		double radRotX=Math.toRadians(rotSpeedX);
		// Rotation X
		this.m[0][0] = 1;
		this.m[1][1] = Math.cos(radRotX);
		this.m[1][2] = Math.sin(radRotX);
		this.m[2][1] = -Math.sin(radRotX);
		this.m[2][2] = Math.cos(radRotX);
		this.m[3][3] = 1;
	}
	public void MatrixRotationY(double rotSpeedY) {
		double radRotY=Math.toRadians(rotSpeedY);
		// Rotation Y
		this.m[0][0] = Math.cos(radRotY);
		this.m[0][2] = -Math.sin(radRotY);
		this.m[1][1] = 1;
		this.m[2][0] = Math.sin(radRotY);
		this.m[2][2] = Math.cos(radRotY);
		this.m[3][3] = 1;
	}
	
}
