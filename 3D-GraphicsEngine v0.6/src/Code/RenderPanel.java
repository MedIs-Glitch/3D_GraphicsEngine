package Code;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import Design.PanelEngine;
import inputs.KeyboardInputs;
import inputs.KeyboardInputs.keyInputs;
import inputs.MouseInputs;

public class RenderPanel extends JPanel {
	
	private static final long serialVersionUID = -1354251777507926593L;
	
	protected Matrix4 projM;
	public Matrix4 rotMX=new Matrix4(),rotMZ=new Matrix4(),rotMY=new Matrix4();
	protected MeshCube mesh;
	
	protected ArrayList<Triangle>vecTrianglesToRaster= new ArrayList<Triangle>();
	protected ArrayList<Triangle> selectedTriangles = new ArrayList<Triangle>();
	protected ArrayList<Vect3D> selectedVects = new ArrayList<Vect3D>();
	
	//transformation pipeline
	protected Triangle triProjected,triTransformed,triViewed;
	
	protected double rotSpeedX=0f,rotSpeedY=0f,rotSpeedZ=0f;
	protected Vect3D normal,line1,line2;
	
	public ArrayList<MeshObject> meshObj = new ArrayList<MeshObject>();
	public ArrayList<MeshObject> selectedMesh = new ArrayList<MeshObject>();
	public ArrayList<Vect3D> Selectedvertices = new ArrayList<Vect3D>();
	
	protected static Vect3D vCamera,vLookDir;
	
	protected Color shadedColor;
	protected double fthetatime = 0.0;
	
	double fYaw = -45, fXaw = 60;

	private static Vect3D vUp;
	
	public FilesHandler filesHandler=new FilesHandler();
	
	private KeyboardInputs keyboardInputs = new KeyboardInputs();
	private MouseInputs mouseInputs = new MouseInputs(this); 
	
	
	JButton face;
	JButton model;
	JButton vertex;
	
	private int faceidCounter = 0;
	
	private Grid3D grid = new Grid3D(10, 10);
	
	public Editor editor = new Editor();
	
	private static RenderPanel instance;
	
	public static RenderPanel getInstance() {
		if(instance == null) {
			instance = new RenderPanel();
		}
		return instance;
	}
	
	private RenderPanel() {		
		addKeyListener(keyboardInputs);
		addMouseMotionListener(mouseInputs);
		addMouseListener(mouseInputs);
        setFocusable(true);
        
        meshObj.add(new MeshObject(filesHandler, MeshObject.faceidCounter));
        
		vCamera=new Vect3D(7.5,9,7.5);
        projM=new Matrix4().matrixMakeProjection(PanelEngine.WIDTH /PanelEngine.HEIGHT, 1000.0f, 0.1f, 80.0f); 
		//projM = Matrix4.initProjection(-90, PanelEngine.WIDTH, PanelEngine.HEIGHT, 0.1, 1000);
	   	triTransformed= new Triangle();	 
	   	triViewed= new Triangle();	
	   	
	   	
	  //buttons
		setLayout(new BorderLayout());
		JPanel buttom = new JPanel();
		buttom.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 50));
		buttom.setOpaque(false);
		add(buttom, BorderLayout.SOUTH);
			  		
		vertex = new JButton();
			vertex.setFocusPainted(false); 
			vertex.setFocusable(false); 
			//vertex.addActionListener(this); 
			ImageIcon Vicon = new ImageIcon("icons/Vertex32.png");
			vertex.setIcon(Vicon);
			vertex.setBackground(new Color(40, 40, 40, 128));
			vertex.setBorderPainted(false);
			buttom.add(vertex);
		face = new JButton(); 
			face.setFocusPainted(false); 
			face.setFocusable(false); 
			//face.addActionListener(this); 
			ImageIcon Ficon = new ImageIcon("icons/Face32.png");
			face.setIcon(Ficon);
			face.setBackground(new Color(40, 40, 40, 128));
			face.setBorderPainted(false);
			buttom.add(face);
		model = new JButton(); 
			model.setFocusPainted(false); 
			model.setFocusable(false); 
			//model.addActionListener(this); 
			ImageIcon Micon = new ImageIcon("icons/Model32.png");
			model.setIcon(Micon);
			model.setBackground(new Color(40, 40, 40, 128));
			model.setBorderPainted(false);
		buttom.add(model);
	  	
	  			
	  	//Actions
		face.addActionListener(e ->{
			System.out.println("face clicked");
			//EditMode.editMode = EditMode.FACE;
			face.setBackground(new Color(0, 255, 0, 128));
			vertex.setBackground(new Color(40, 40, 40, 128));
			model.setBackground(new Color(40, 40, 40, 128));
			Editor.editMode = Editor.faceMode;
			refrech();
		});
		
		model.addActionListener(e ->{
			System.out.println("model clicked");
			//EditMode.editMode = EditMode.MODEL;
			model.setBackground(new Color(0, 255, 0, 128));
			vertex.setBackground(new Color(40, 40, 40, 128));
			face.setBackground(new Color(40, 40, 40, 128));
			Editor.editMode = Editor.allMeshMode;
			refrech();
		});
		
		vertex.addActionListener(e ->{
			System.out.println("vertex clicked");
			//EditMode.editMode = EditMode.VERTEX;
			vertex.setBackground(new Color(0, 255, 0, 128));
			face.setBackground(new Color(40, 40, 40, 128));
			model.setBackground(new Color(40, 40, 40, 128));
			Editor.editMode = Editor.vertexMode;
			refrech();
		});
		
		
		
	}
	private void refrech() {
		// TODO Auto-generated method stub
		for(int i=0;i<meshObj.size();i++)
	   		for(Triangle t:meshObj.get(i).getObj()) {
	   			t.isSelected = false;
	   			t.color = t.OGColor;
	   			for(Vect3D v:t.getVs()) {
	   				v.isSelected = false;
	   			}
	   			selectedMesh.clear();
	   			selectedTriangles.clear();
	   			Selectedvertices.clear();
	   			selectedVects.clear();
	   		}
		
	}
	public void addObj(MeshObject obj) {
		meshObj.add(obj);
	}
	public void removeObj(int index) {
		meshObj.remove(index);
	}
	public ArrayList<String> getLoadedObjs() {
		 ArrayList<String> modelNames = new ArrayList<>();
		for(MeshObject i:meshObj){
			modelNames.add(i.name);
		}
		return modelNames;
	}
	public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;        
        //clear
        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(0, 0, getWidth(), getHeight());
        
        editor.update(Selectedvertices, selectedMesh);
//	   	rotate();
		this.rotSpeedX+=0;
		this.rotSpeedZ+=0;
		this.rotSpeedY+=0;
		rotMX.MatrixRotationX(rotSpeedX);
		rotMZ.MatrixRotationZ(rotSpeedZ);
		rotMY.MatrixRotationY(rotSpeedY);

	   	Matrix4 matTrans = Matrix4.matrixMakeTranslation(0, 0, 15);
	   	
	   	Matrix4 matWorld= Matrix4.matrixMakeIdentity();
	   	
	   	matWorld=rotMX.matrixMultiplyMatrix(rotMY);
	   	matWorld=matWorld.matrixMultiplyMatrix(rotMZ);
	   	matWorld=matWorld.matrixMultiplyMatrix(matTrans);
	   	
	   	vUp=new Vect3D(0,1,0);
	   	Vect3D vTarget=new Vect3D(0,0,1);
	   	
	   	Matrix4 matCameraRotY= new Matrix4();
	   	Matrix4 matCameraRotX= new Matrix4();
	   	matCameraRotY.MatrixRotationY(fYaw);
	   	matCameraRotX.MatrixRotationX(fXaw);
	   	matCameraRotY = matCameraRotX.matrixMultiplyMatrix(matCameraRotY);
	   	vLookDir= vTarget.VectorMultiplyMatrix(matCameraRotY);
	   	vTarget= vCamera.vectorAdd(vLookDir);
	   	
	   	Matrix4 matCamera= Matrix4.matrixPointAt(vCamera, vTarget, vUp);
	   	//make view matrix from camera
	   	Matrix4 matView= matCamera.matrixQuickInverse();

		grid.drawGrid3D(g2, matWorld, matView, projM, this);

	   	for(int i=0;i<meshObj.size();i++) {
		   	render(matWorld,meshObj.get(i),matView);
	   	}
	   	
	   	
	   	
        Comparator<Triangle> customComparator = new Comparator<Triangle>() {
            @Override
            public int compare(Triangle t1, Triangle t2) {
                double z1 = (t1.v[0].z + t1.v[1].z + t1.v[2].z) / 3.0f;
                double z2 = (t2.v[0].z + t2.v[1].z + t2.v[2].z) / 3.0f;
                // Sort in descending order based on Z-coordinate
                return Double.compare(z2, z1);
            }
        };
        
        Triangle temp = null, templeft = null, tempright = null; // This triangle is used to get referece of the last triangle that the mouse is hovering on it;
        
        MeshObject tempMesh = null;
        
        Collections.sort(vecTrianglesToRaster, customComparator);
        
     // TODO triangle selections
	   	for(Triangle t:vecTrianglesToRaster) {
	   		if(mouseInputs.isMouseInTriangle(t)) {
	   			temp = t;
	   		}
	   	}
	   	
	   	
	   	int j = 0;
	   	if(editor.editMode == Editor.faceMode) {
		   	if(temp != null) {
		   		temp.isHovered = true;
		   		
		   		if(MouseInputs.MouseStaticInputs.isPressed && !MouseInputs.MouseStaticInputs.isDragged) {
		   			for(int i=0;i<meshObj.size();i++)
				   		for(Triangle t:meshObj.get(i).getObj()) {
					   		if(t.id == temp.id) {
					   			if(!keyInputs.isCtrlPressed) {
						   			t.color = Color.CYAN;
						   			t.isSelected = true;
						   			temp.isSelected = true;
						   			
						   			if(meshObj.get(i).getObj().size() > j+1 && meshObj.get(i).getObj().get(j+1).faceid == t.faceid) {
						   				meshObj.get(i).getObj().get(j+1).color = Color.CYAN;
						   				meshObj.get(i).getObj().get(j+1).isSelected = true;
						   			}
						   			else {
						   				if(j-1 >= 0 && meshObj.get(i).getObj().get(j-1).faceid == t.faceid) {
						   					meshObj.get(i).getObj().get(j-1).color = Color.CYAN;
						   					meshObj.get(i).getObj().get(j-1).isSelected = true;
						   				}
						   			}
						   			
					   			}
					   			else {
					   				t.color = t.OGColor;
						   			t.isSelected = false;
						   			temp.isSelected = false;
						   			
						   			Selectedvertices.remove(t.v[0]);
									Selectedvertices.remove(t.v[1]);
									Selectedvertices.remove(t.v[2]);
									
									if(meshObj.get(i).getObj().size() > j+1 && meshObj.get(i).getObj().get(j+1).faceid == t.faceid) {
						   				meshObj.get(i).getObj().get(j+1).color = meshObj.get(i).getObj().get(j+1).OGColor;
						   				meshObj.get(i).getObj().get(j+1).isSelected = false;
						   				Selectedvertices.remove(meshObj.get(i).getObj().get(j+1).v[0]);
										Selectedvertices.remove(meshObj.get(i).getObj().get(j+1).v[1]);
										Selectedvertices.remove(meshObj.get(i).getObj().get(j+1).v[2]);
						   			}
						   			else {
						   				if(j-1 >= 0 && meshObj.get(i).getObj().get(j-1).faceid == t.faceid) {
						   					meshObj.get(i).getObj().get(j-1).color = meshObj.get(i).getObj().get(j-1).OGColor;
						   					meshObj.get(i).getObj().get(j-1).isSelected = false;
						   					Selectedvertices.remove(meshObj.get(i).getObj().get(j-1).v[0]);
											Selectedvertices.remove(meshObj.get(i).getObj().get(j-1).v[1]);
											Selectedvertices.remove(meshObj.get(i).getObj().get(j-1).v[2]);
						   				}
						   			}
					   			}
					   			break;
					   		}
					   		j++;
					   	}
		   			temp = null;
		   			//MouseInputs.MouseStaticInputs.isPressed = false;
		   		}
		   		
		   	}
		   	else if(MouseInputs.MouseStaticInputs.isPressed && keyInputs.isCtrlPressed  && !MouseInputs.MouseStaticInputs.isDragged) {
		   		for(int i=0;i<meshObj.size();i++)
			   		for(Triangle t:meshObj.get(i).getObj()) {
			   			t.color = t.OGColor;
			   			t.isSelected = false;
			   			t.isSelected = false;
			   			
			   			Selectedvertices.remove(t.v[0]);
						Selectedvertices.remove(t.v[1]);
						Selectedvertices.remove(t.v[2]);
			   		}
		   	}
	   	}
	   	
	   	else if(editor.editMode == Editor.allMeshMode) {
	   		if(temp != null) {
		   		if(MouseInputs.MouseStaticInputs.isPressed && !MouseInputs.MouseStaticInputs.isDragged) {
		   			for(int i=0;i<meshObj.size();i++)
				   		for(Triangle t:meshObj.get(i).getObj()) {
					   		if(t.id == temp.id && !keyInputs.isCtrlPressed) {
					   			tempMesh = meshObj.get(i);
					   			if(!tempMesh.isSeleceted) tempMesh.changeColor();
					   			tempMesh.isSeleceted = true;
					   			if(!selectedMesh.contains(tempMesh)) {
					   				selectedMesh.add(tempMesh);
					   				System.out.println("mesh selected, " + selectedMesh.get(0).name);
					   			}
					   			
					   			break;
					   		}
					   		else if(t.id == temp.id) {
					   			tempMesh = meshObj.get(i);
					   			if(tempMesh.isSeleceted) tempMesh.OGColor();
					   			tempMesh.isSeleceted = false;
					   			if(selectedMesh.contains(tempMesh)) selectedMesh.remove(tempMesh);
					   			break;
					   		}
					   		if(tempMesh != null) break;
				   		}
		   		}
	   		}
	   		else if(MouseInputs.MouseStaticInputs.isPressed && keyInputs.isCtrlPressed && !MouseInputs.MouseStaticInputs.isDragged){
	   			
	   			for(MeshObject m:selectedMesh) {
	   				if(m.isSeleceted) m.OGColor();
	   				m.isSeleceted = false;
	   			}
	   			selectedMesh.clear();
	   		}
	   	}
	   	
	   	//vetcs 
	   	Vect3D hovered = null;
	   	
	   	if(editor.editMode == Editor.vertexMode)
   		for(Triangle t:vecTrianglesToRaster)
	   		for(Vect3D v:t.getVs()) {
	   			if(v.dist2D(mouseInputs.getMousePos()) < 5) {
	   				hovered = v;
	   	
	   				if(MouseInputs.MouseStaticInputs.isPressed && !MouseInputs.MouseStaticInputs.isDragged && !keyInputs.isCtrlPressed) {
	   					
	   					
	   					for(int i=0;i<meshObj.size();i++)
					   		for(Triangle t2:meshObj.get(i).getObj())
					   			for(Vect3D v2:t2.getVs()) {
					   				if(hovered.id == v2.id) {
					   					v2.isSelected = true;
					   					if(!Selectedvertices.contains(v2)) Selectedvertices.add(v2);
					   					System.out.println(Selectedvertices);
					   				}
					   					
					   			}
	   				}
	   				
	   				else if(MouseInputs.MouseStaticInputs.isPressed && !MouseInputs.MouseStaticInputs.isDragged  && keyInputs.isCtrlPressed) {
	   					
	   					for(int i=0;i<meshObj.size();i++)
					   		for(Triangle t2:meshObj.get(i).getObj())
					   			for(Vect3D v2:t2.getVs()) {
					   				if(hovered.id == v2.id) {
					   					v2.isSelected = false;
					   					if(Selectedvertices.contains(v2)) {
					   						Selectedvertices.remove(v2);
					   						
					   					}
					   				}
					   			}
	   						
	   				}
	   				
	   				
	   				
	   			}
	   		}
	   	if(MouseInputs.MouseStaticInputs.isPressed && !MouseInputs.MouseStaticInputs.isDragged  && keyInputs.isCtrlPressed && hovered == null)
	   		Selectedvertices.clear();
	   	
	   	
	   	for(int i=0;i<meshObj.size();i++)
		   	for(Triangle t:meshObj.get(i).getObj()) {
				if(t.isSelected) {
					if(!Selectedvertices.contains(t.v[0])) Selectedvertices.add(t.v[0]);
					if(!Selectedvertices.contains(t.v[1])) Selectedvertices.add(t.v[1]);
					if(!Selectedvertices.contains(t.v[2])) Selectedvertices.add(t.v[2]);
				}
			}
     ///
	   	
        LinkedList<Triangle> listTriangles=new LinkedList<Triangle>();
        
        for(Triangle t:vecTrianglesToRaster) {
        	Triangle[] clipped1=new Triangle[2];
            
            listTriangles.add(t);
            int nNewTriangles=1;
            for(int p=0;p<4;p++) {
				int nTrisToAdd = 0;
				while (nNewTriangles > 0){
					Triangle test =listTriangles.peek();
						for (int i = 0; i < 2; i++) { 
			           	    clipped1[i] = new Triangle(); // Initialize each element with a new Triangle object
			            }
					    listTriangles.poll(); // Equivalent to pop_front() in C++
					    nNewTriangles--;
					switch (p){
					case 0:
				        nTrisToAdd = TriangleClipAgainstPlane(new Vect3D(0.0f, 0.0f, 0.0f), new Vect3D(0.0f, 1.0f, 0.0f), test, clipped1[0], clipped1[1]);
				        break;
				    case 1:
				        nTrisToAdd = TriangleClipAgainstPlane(new Vect3D(0.0f, (double)getHeight() - 1, 0.0f), new Vect3D(0.0f, -1.0f, 0.0f), test, clipped1[0], clipped1[1]);
				        break;
				    case 2:
				        nTrisToAdd = TriangleClipAgainstPlane(new Vect3D(0.0f, 0.0f, 0.0f), new Vect3D(1.0f, 0.0f, 0.0f), test, clipped1[0], clipped1[1]);
				        break;
				    case 3:
				    	nTrisToAdd = TriangleClipAgainstPlane(new Vect3D((double)getWidth() - 1, 0.0f, 0.0f), new Vect3D(-1.0f, 0.0f, 0.0f), test, clipped1[0], clipped1[1]);
				        break;
					}
					for (int w = 0; w < nTrisToAdd; w++) {
					    listTriangles.add(clipped1[w]);
					}	
				}
				nNewTriangles=listTriangles.size();
            }
            for(Triangle t1:listTriangles) {
    	   	 	Path2D path = new Path2D.Double();
    	        g2.setColor(t1.color);
    	        path.moveTo(t1.v[0].x,t1.v[0].y);
    	        path.lineTo(t1.v[1].x,t1.v[1].y);
    	        path.lineTo(t1.v[2].x,t1.v[2].y);
    	        path.closePath();
    	        g2.fill(path);
    	        
    	    }
            
            //draw vects
            if(editor.editMode == Editor.vertexMode) {
	            g2.setColor(Color.yellow);
	            if(hovered != null) g2.fillArc((int)(hovered.x-5), (int)(hovered.y-5), 10, 10, 0, 360);
	            
	            g2.setColor(Color.cyan);
	            if(!Selectedvertices.isEmpty())
		            for (Vect3D v: Selectedvertices) {
		            	for(Triangle t2:vecTrianglesToRaster)
		        	   		for(Vect3D v2:t.getVs())
		        	   			if(v2.id == v.id)
		        	   			g2.fillArc((int)(v2.x-5), (int)(v2.y-5), 10, 10, 0, 360);
					}
            }
            
            
            // Display Hovered triangles
            if(t.isHovered) {
	        	t.drawOutlines(g2);
	        }
            listTriangles.clear();
        }
        vecTrianglesToRaster.clear();
        updateObjectPosition();

	        
	    }
	
		public void render(Matrix4 matWorld,MeshObject obj, Matrix4 matView) {

			if(obj.isSeleceted) editor.updateRotation(obj);
			rotate(obj);
			
	        for(Triangle t :obj.getObj()) {	
	        	
				normal=new Vect3D();
				line1=new Vect3D();
				line2=new Vect3D();
				
				Triangle triScaled = new Triangle();
				triScaled.v[0] = t.v[0].scaleX(obj.scaleX);
				triScaled.v[1] = t.v[1].scaleX(obj.scaleX);
				triScaled.v[2] = t.v[2].scaleX(obj.scaleX);
				
				triScaled.v[0] = triScaled.v[0].scaleY(obj.scaleY);
				triScaled.v[1] = triScaled.v[1].scaleY(obj.scaleY);
				triScaled.v[2] = triScaled.v[2].scaleY(obj.scaleY);
				
				triScaled.v[0] = triScaled.v[0].scaleZ(obj.scaleZ);
				triScaled.v[1] = triScaled.v[1].scaleZ(obj.scaleZ);
				triScaled.v[2] = triScaled.v[2].scaleZ(obj.scaleZ);
				
				Triangle triRotatedX = new Triangle();
				triRotatedX.v[0] = triScaled.v[0].VectorMultiplyMatrix(rotMX);
				triRotatedX.v[1] = triScaled.v[1].VectorMultiplyMatrix(rotMX);
				triRotatedX.v[2] = triScaled.v[2].VectorMultiplyMatrix(rotMX);
				
				Triangle triRotatedY = new Triangle();
				triRotatedY.v[0] = triRotatedX.v[0].VectorMultiplyMatrix(rotMY);
				triRotatedY.v[1] = triRotatedX.v[1].VectorMultiplyMatrix(rotMY);
				triRotatedY.v[2] = triRotatedX.v[2].VectorMultiplyMatrix(rotMY);
				
				Triangle triRotatedZ = new Triangle();
				triRotatedZ.v[0] = triRotatedY.v[0].VectorMultiplyMatrix(rotMZ);
				triRotatedZ.v[1] = triRotatedY.v[1].VectorMultiplyMatrix(rotMZ);
				triRotatedZ.v[2] = triRotatedY.v[2].VectorMultiplyMatrix(rotMZ);
				
				Triangle triTranslated = new Triangle();

				triTranslated.v[0] = triRotatedZ.v[0].transform(obj.x + t.v[0].tx, obj.y + t.v[0].ty, obj.z + t.v[0].tz);
				triTranslated.v[1] = triRotatedZ.v[1].transform(obj.x + t.v[1].tx, obj.y + t.v[1].ty, obj.z + t.v[1].tz);
				triTranslated.v[2] = triRotatedZ.v[2].transform(obj.x + t.v[2].tx, obj.y + t.v[2].ty, obj.z + t.v[2].tz);
				
				
	        	triTransformed.v[0] = triTranslated.v[0].VectorMultiplyMatrix(matWorld);
	        	triTransformed.v[1] = triTranslated.v[1].VectorMultiplyMatrix(matWorld);
	        	triTransformed.v[2] = triTranslated.v[2].VectorMultiplyMatrix(matWorld);

	 			line1= triTransformed.v[1].vectorSub(triTransformed.v[0]);
			   	line2= triTransformed.v[2].vectorSub(triTransformed.v[0]);
			   	
			   	normal.x= line1.y*line2.z - line1.z*line2.y;
			   	normal.y= line1.z*line2.x - line1.x*line2.z;
			   	normal.z= line1.x*line2.y - line1.y*line2.x;
		
			   	normal= line1.vectorCrossProduct(line2);
			   	//normalize the normal
			   	normal=normal.normalize();
	        	 Vect3D vCameraRay = triTransformed.v[0].vectorSub(vCamera);
	        	 //if(normal.z<0) 
	        	 if(normal.calculateDotProduct(vCameraRay)< 0.0f){
	        		 //Light
	        		 Vect3D lightDirection=new Vect3D(0,200f,200f);
	        		 lightDirection=vCameraRay.normalize();
	        		 lightDirection = lightDirection.vectorMul(-1);
	        		 double dp=lightDirection.calculateDotProduct(normal);
	        		 //setting Up Color Based on the Triangles color
	        		 Color baseColor = t.color;
	        		 // Calculate the new color with shading based on the dot product
	        		 int red = (int) (Math.max (25, Math.min(235, baseColor.getRed() * dp)) ) ;
	        		 int green = (int) (Math.max(25, Math.min(235, baseColor.getGreen() * dp)));
	        		 int blue = (int) (Math.max(25, Math.min(235, baseColor.getBlue() * dp)));
	        		 
	        		 // Create a new Color with the shaded values
	        		 Color shadedColor = new Color(red, green, blue);
	        		 triTransformed.color = shadedColor;

	        		 //Convert World Space to View Space
	        		 triViewed.v[0]= triTransformed.v[0].VectorMultiplyMatrix(matView); 
	                 triViewed.v[1]= triTransformed.v[1].VectorMultiplyMatrix(matView);
	                 triViewed.v[2]= triTransformed.v[2].VectorMultiplyMatrix(matView);
	        		 triViewed.color=triTransformed.color;
	        		 
	                 //Clip Viewed Triangle Against The Plane
	                 int nClippedTriangles=0;
	                 Triangle[] clipped=new Triangle[2];
	                 for (int i = 0; i < 2; i++) {
	                	    clipped[i] = new Triangle(); // Initialize each element with a new Triangle object
	                }	                 
	                 nClippedTriangles = TriangleClipAgainstPlane(new Vect3D(0,0,0.3),new Vect3D(0,0,1),triViewed,clipped[0],clipped[1]);
	                 for (int n=0;n<nClippedTriangles;n++) {     	 
	                	 //Project from 3D to 2D
	            		 triProjected = new Triangle();

	            		 triProjected.v[0]= clipped[n].v[0].VectorMultiplyMatrix(projM);
	            	     triProjected.v[1]= clipped[n].v[1].VectorMultiplyMatrix(projM);
	            		 triProjected.v[2]= clipped[n].v[2].VectorMultiplyMatrix(projM);

	            		 triProjected.color= shadedColor;
	            		 //Normalizing The Projection And Scaling
	            		 triProjected.v[0]=triProjected.v[0].vectorDiv(triProjected.v[0].w);
	            		 triProjected.v[1]=triProjected.v[1].vectorDiv(triProjected.v[1].w);
	            		 triProjected.v[2]=triProjected.v[2].vectorDiv(triProjected.v[2].w);		 
	            		 
	                	 // X/Y are inverted so put them back
	 					 triProjected.v[0].x *= -1.0f;
	 					 triProjected.v[1].x *= -1.0f;
	 					 triProjected.v[2].x *= -1.0f;
	 					 triProjected.v[0].y *= -1.0f;
	 					 triProjected.v[1].y *= -1.0f;
	 					 triProjected.v[2].y *= -1.0f;

	                	 //Offset Into Visible Normalized Space
	            		 Vect3D vOffsetView = new Vect3D(1.0f,1.0f,0.0f);
	                	 triProjected.v[0]=triProjected.v[0].vectorAdd(vOffsetView);
	                	 triProjected.v[1]=triProjected.v[1].vectorAdd(vOffsetView);
	                	 triProjected.v[2]=triProjected.v[2].vectorAdd(vOffsetView);
	                	 
	                	 triProjected.v[0].x *= 0.5f * (double) getWidth();
	                	 triProjected.v[0].y *= 0.5f * (double) getHeight() * ((double)getWidth()/(double)getHeight());
	                	 triProjected.v[1].x *= 0.5f * (double) getWidth();
	                	 triProjected.v[1].y *= 0.5f * (double) getHeight() * ((double)getWidth()/(double)getHeight());
	                	 triProjected.v[2].x *= 0.5f * (double) getWidth();
	                	 triProjected.v[2].y *= 0.5f * (double) getHeight() * ((double)getWidth()/(double)getHeight());
	                	 triProjected.id = t.id;
	                	 
	                	 vecTrianglesToRaster.add(triProjected);
	                	 
	                	 triProjected.v[0].id = t.v[0].id;
	                	 triProjected.v[1].id = t.v[1].id;
	                	 triProjected.v[2].id = t.v[2].id;
	                 }
	        	 } 
	        }
		}
		

		

		
		public void rotate(MeshObject obj) {
			
			rotMZ.MatrixRotationZ(obj.rotZ);
			rotMX.MatrixRotationX(obj.rotX);
			rotMY.MatrixRotationY(obj.rotY);
		}
		
		
   		
   		private void updateObjectPosition() {
   			double cameraSpeed = 0.3;
   			if(KeyboardInputs.keyInputs.isShiftPressed) cameraSpeed = 1;
   					
   		    Vect3D vForward = vLookDir.vectorMul(cameraSpeed);
   		    Vect3D vRight = vLookDir.vectorCrossProduct(vUp); // Calculate the rightward direction
   		    
   		    vRight=vRight.normalize();
   		    vRight = vRight.vectorMul(cameraSpeed);

   		    
   		    fYaw -= MouseInputs.MouseStaticInputs.rotationX;
		    fXaw +=  MouseInputs.MouseStaticInputs.rotationY;
		    mouseInputs.refresh();
		    clampRotation();
   		    
   		    // Forward camera movements
   		    if (KeyboardInputs.keyInputs.forMovement > 0) {
   		        vCamera = vCamera.vectorAdd(vForward);
   		    } else if (KeyboardInputs.keyInputs.forMovement < 0) {
   		        vCamera = vCamera.vectorSub(vForward);
   		    }
   		    
   		    
   		    // Horizontal camera movements
   		    if (KeyboardInputs.keyInputs.horMovement < 0) {
   		    	vCamera = vCamera.vectorSub(vRight);
   		    } else if (KeyboardInputs.keyInputs.horMovement > 0) {
   		    	vCamera = vCamera.vectorAdd(vRight);
   		    }
   		    
   		}
   		public int TriangleClipAgainstPlane(Vect3D plane_p,Vect3D plane_n, Triangle in_tri, Triangle out_tri1,Triangle out_tri2){
   			plane_n=plane_n.normalize();
   		    // Create two temporary storage arrays to classify points either side of the plane
   		    // If distance sign is positive, the point lies on the "inside" of the plane
   		    Vect3D[] inside_points = new Vect3D[3];
	   		for (int i = 0; i < 3; i++) {
	   			inside_points[i] = new Vect3D(); // Initialize each element with a new Triangle object
	   		}
   		    int nInsidePointCount = 0;
   		    Vect3D[] outside_points = new Vect3D[3];
   		    for (int i = 0; i < 3; i++) {
   		    	outside_points[i] = new Vect3D(); // Initialize each element with a new Triangle object
	   		}
   		    int nOutsidePointCount = 0;
   		    
   		    // Get signed distance of each point in the triangle to the plane
   		    double d0 = dist(in_tri.v[0],plane_p,plane_n);
   		    double d1 = dist(in_tri.v[1],plane_p,plane_n);
   		    double d2 = dist(in_tri.v[2],plane_p,plane_n);

   		    if (d0 >= 0) {
   		        inside_points[nInsidePointCount++]=in_tri.v[0];
   		    } else {
   		        outside_points[nOutsidePointCount++]=in_tri.v[0];
   		    }
   		    if (d1 >= 0) {
   		        inside_points[nInsidePointCount++]=in_tri.v[1];
   		    } else {
   		        outside_points[nOutsidePointCount++]=in_tri.v[1];
   		    }
   		    if (d2 >= 0) {
   		        inside_points[nInsidePointCount++]=in_tri.v[2];
   		    } else {
   		        outside_points[nOutsidePointCount++]=in_tri.v[2];
   		    }   
   		    if (nInsidePointCount == 0){
   				// All points lie on the outside of plane, so clip whole triangle
   				// It ceases to exist
   				return 0; // No returned triangles are valid
   			}
   			if (nInsidePointCount == 3){
   				// All points lie on the inside of plane, so do nothing
   				// and allow the triangle to simply pass through
   				out_tri1.copy(in_tri);
   				return 1; // Just the one returned original triangle is valid
   			}
   			if (nInsidePointCount == 1 && nOutsidePointCount == 2){
   				// Triangle should be clipped. As two points lie outside
   				// the plane, the triangle simply becomes a smaller triangle

   				// Copy appearance info to new triangle
   				out_tri1.color=in_tri.color;
   				// The inside point is valid, so keep that...
   				out_tri1.v[0]=inside_points[0];
   				// but the two new points are at the locations where the 
   				// original sides of the triangle (lines) intersect with the plane

   				//probleme here , outside_points[1] values change
   				out_tri1.v[1]=VectorIntersectPlane(plane_p, plane_n, inside_points[0], outside_points[0]);
   				out_tri1.v[2]=VectorIntersectPlane(plane_p, plane_n, inside_points[0], outside_points[1]);
   				return 1; // Return the newly formed single triangle
   			}
   			if (nInsidePointCount == 2 && nOutsidePointCount == 1) {
   				out_tri1.color=in_tri.color;
   				out_tri2.color=in_tri.color;
   				// The first triangle consists of the two inside points and a new
   				// point determined by the location where one side of the triangle
   				// intersects with the plane
   				out_tri1.v[0]=inside_points[0];
   				out_tri1.v[1]=inside_points[1];
   				out_tri1.v[2]=VectorIntersectPlane(plane_p, plane_n, inside_points[0], outside_points[0]);
   				// The second triangle is composed of one of he inside points, a
   				// new point determined by the intersection of the other side of the 
   				// triangle and the plane, and the newly created point above
   				out_tri2.v[0]=inside_points[1];
   				out_tri2.v[1]=out_tri1.v[2];
   				out_tri2.v[2]=VectorIntersectPlane(plane_p, plane_n, inside_points[1], outside_points[0]);
   				
   				return 2;
   			}
   			return -1;
   		}
   		public double dist(Vect3D p, Vect3D plane_p, Vect3D plane_n) {
   		    return plane_n.x * p.x + plane_n.y * p.y + plane_n.z * p.z - plane_n.calculateDotProduct(plane_p);
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
   		
   		public void clampRotation() {
   			//System.out.println(fYaw + ", " + fXaw);
   			if(fXaw >= 89) fXaw = 89;
   			else if(fXaw <= -89) fXaw = -89;
   		}
		public void update() {
			// TODO Auto-generated method stub
//			for(int i=0;i<meshObj.size();i++)
//		   		for(Triangle t:meshObj.get(i).getObj()) {
//			   		if(t.isSelected) {
//			   			t.transform((double)KeyboardInputs.keyInputs.horizontal/10,(double) KeyboardInputs.keyInputs.vertical/10, 0);
//			   		}
//			   	}
			if(Editor.editMode == Editor.faceMode)
				for(Vect3D v:Selectedvertices) {
					v.transform((double)KeyboardInputs.keyInputs.horizontal/10,(double) KeyboardInputs.keyInputs.vertical/10, 0);
				}
			else if(Editor.editMode == Editor.allMeshMode)
				for(MeshObject m:selectedMesh) {
					m.translate((double)KeyboardInputs.keyInputs.horizontal/10,(double) KeyboardInputs.keyInputs.vertical/10, 0);
				}
			else if(Editor.editMode == Editor.vertexMode) {
				for(Vect3D v:Selectedvertices) {
					v.transform((double)KeyboardInputs.keyInputs.horizontal/10,(double) KeyboardInputs.keyInputs.vertical/10, 0);
				}
			}
				
		}
		public MeshObject getMesh(String name) {
			// TODO Auto-generated method stub
			for(MeshObject m:meshObj) {
				if(name.equals(m.getName())) {
					return m;
				}
			}
			return null;
		}
		public void newProjcet() {
			filesHandler = new FilesHandler();
			meshObj.clear();
			meshObj.add(new MeshObject(filesHandler, 0));
	        
			vCamera=new Vect3D(7.5,9,7.5);
			fYaw = -45;
			fXaw = 60;
			projM=new Matrix4().matrixMakeProjection(PanelEngine.WIDTH /PanelEngine.HEIGHT, 1000.0f, 0.1f, 80.0f); 
			//projM = Matrix4.initProjection(-90, PanelEngine.WIDTH, PanelEngine.HEIGHT, 0.1, 1000);
		   	triTransformed= new Triangle();	 
		   	triViewed= new Triangle();
			
		}
}
