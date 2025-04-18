package Code;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class MeshObject {
	
	protected ArrayList<Triangle>triangles;	
	public ArrayList<Vect3D>verts;
	protected String name;
	public double rotX = 0,rotY = 0,rotZ = 0;
	public double x = 0, y = 1, z = 0;
	public double scaleX = 1, scaleY = 1, scaleZ = 1;
	public static int faceidCounter = 0;
	public boolean isSeleceted;

	public MeshObject(FilesHandler  filesHandler,int objNumber) {
		triangles=new ArrayList<Triangle>();
		verts= new ArrayList<Vect3D>();
		objFile2Vect(filesHandler,objNumber);
 	}
	
	public MeshObject(File file) {
		triangles=new ArrayList<Triangle>();
		verts= new ArrayList<Vect3D>();
		objFile2Vect(file);
 	}
	
	public MeshObject() {
		triangles=new ArrayList<Triangle>();
		verts= new ArrayList<Vect3D>();
	}
	public void objFile2Vect(FilesHandler  filesHandler,int objNumber) {
    	String modelName = filesHandler.objFiles.get(objNumber).getName();
    	this.name=modelName;
    	int jj = 0;
		File i=filesHandler.objFiles.get(objNumber);
			try {
				Scanner scanner=new Scanner(i);				
				while(scanner.hasNextLine()) {
					//System.out.println(jj++);
					String line=scanner.nextLine();
					@SuppressWarnings("unused")
					char junk;
					if(!line.isBlank()) {
						if(line.startsWith("v ")) {
							junk = line.charAt(0);
		                    String[] parts = line.split("\\s+");
		                    double x = Double.parseDouble(parts[1]);
		                    double y = Double.parseDouble(parts[2]);
		                    double z = Double.parseDouble(parts[3]);
							
		                    Vect3D v = new Vect3D(x, y, z);
		                    verts.add(v);  
						}
						if(line.startsWith("f ")) {
							junk = line.charAt(0);
		                    String[] parts = line.split("\\s+");
		                    int v1 = Integer.parseInt(parts[1].split("/")[0]);
		                    int v2 = Integer.parseInt(parts[2].split("/")[0]);
		                    int v3 = Integer.parseInt(parts[3].split("/")[0]);
		                    int v4 = 0;
		                    if(parts.length == 5) v4 = Integer.parseInt(parts[4].split("/")[0]);
		                    triangles.add(new Triangle(verts.get(v1-1),verts.get(v2-1),verts.get(v3-1), this, faceidCounter,v1,v2,v3));
		                    if(parts.length == 5) triangles.add(new Triangle(verts.get(v1-1),verts.get(v3-1),verts.get(v4-1), this, faceidCounter,v1,v3,v4));
		                    faceidCounter++;
						}
					}
				}
				scanner.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public void objFile2Vect(File  file) {
    	String modelName = file.getName();
    	this.name=modelName;
    	int jj = 0;
			try {
				Scanner scanner=new Scanner(file);				
				while(scanner.hasNextLine()) {
					//System.out.println(jj++);
					String line=scanner.nextLine();
					@SuppressWarnings("unused")
					char junk;
					if(!line.isBlank()) {
						if(line.startsWith("v ")) {
							junk = line.charAt(0);
		                    String[] parts = line.split("\\s+");
		                    double x = Double.parseDouble(parts[1]);
		                    double y = Double.parseDouble(parts[2]);
		                    double z = Double.parseDouble(parts[3]);
							
		                    Vect3D v = new Vect3D(x, y, z);
		                    verts.add(v);  
						}
						if(line.startsWith("f ")) {
							junk = line.charAt(0);
		                    String[] parts = line.split("\\s+");
		                    int v1 = Integer.parseInt(parts[1].split("/")[0]);
		                    int v2 = Integer.parseInt(parts[2].split("/")[0]);
		                    int v3 = Integer.parseInt(parts[3].split("/")[0]);
		                    int v4 = 0;
		                    if(parts.length == 5) v4 = Integer.parseInt(parts[4].split("/")[0]);
		                    triangles.add(new Triangle(verts.get(v1-1),verts.get(v2-1),verts.get(v3-1), this, faceidCounter,v1,v2,v3));
		                    if(parts.length == 5) triangles.add(new Triangle(verts.get(v1-1),verts.get(v3-1),verts.get(v4-1), this, faceidCounter,v1,v3,v4));
		                    faceidCounter++;
						}
					}
				}
				scanner.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public ArrayList<Triangle> getObj() {
		return triangles;
	}
	
	public void rotate(double rx, double ry, double rz) {
		rotX += rx;
		rotY += ry;
		rotZ += rz;
		//System.out.println(rotX);
	}
	
	public void translate(double x, double y, double z) {
		this.x += x;
		this.y += y;
		this.z += z;
		//System.out.println(x);
	}
	
	public static void showLoadingPopup(Component parentComponent) {
        JDialog loadingDialog = new JDialog((Frame) parentComponent, "Loading...", true);
        loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        // Create a label and add it to the dialog
        JLabel label = new JLabel("Loading, please wait...");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        loadingDialog.add(label);

        loadingDialog.setSize(200, 100);
        loadingDialog.setLocationRelativeTo(parentComponent);
        loadingDialog.setVisible(true);
    
	}
	
	public void changeColor() {
		for (Triangle triangle : triangles) {
			triangle.color = Color.CYAN;
		}
	}
	
	public void OGColor() {
		for (Triangle triangle : triangles) {
			triangle.color = triangle.OGColor;
		}
	}
	
	public String getName() {
		return this.name;
	}

}
