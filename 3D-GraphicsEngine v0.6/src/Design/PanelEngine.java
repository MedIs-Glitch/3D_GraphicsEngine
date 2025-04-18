package Design;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ScrollPaneLayout;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultTreeCellRenderer;

import Code.Matrix4;
import Code.MeshObject;
import Code.RenderPanel;
import Code.Triangle;
import Code.Vect3D;
import inputs.MouseInputs;


public class PanelEngine {
	
	public static File mainProject;
	public static String timeStamp = "Not saved yet";
	

	/**
	 * Launch the application.
	 */
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	private static final int FPS_SET = 60;
	public static final String Title="Display Engine";
	
	
	protected static int nbModels=2;
	
	private JFrame frametest;
	
	protected static String folderLoad="ObjectHandler";
	protected static String folderInsert="Project 3";
	private JMenuBar menuBar;
	private JMenu mnNewMenu;
	private JMenu editMenu;
	private JMenu helpMenu;
	private JMenu mnNewMenu_2;
	private JMenu mnNewMenu_1;
	private JMenuItem mntmNewMenuItem_1;
	private JMenuItem mntmNewMenuItem_2;
	private JMenuItem mntmNewMenuItem_3;
	private JMenuItem mntmNewMenuItem_4;
	private JMenuItem mntmNewMenuItem_5;
	private JMenuItem mntmNewMenuItem;
	private JMenuItem mntmNewMenuItem_6;
	
	public static void main(String[] args) {
		
		RenderPanel renderPanel = RenderPanel.getInstance();
		
		JFrame frame = new JFrame(Title);
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PanelEngine window = new PanelEngine(renderPanel, frame);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		// Loop
		double timePerFrame = 1000000000.0 / FPS_SET;
		
		long previousTime = System.nanoTime();
		
		int frames = 0;
		
		long lastCheck = System.currentTimeMillis();
		
		double deltaF = 0;
		
		while(true) {
			
			long currentTime = System.nanoTime();
			
			deltaF += (currentTime - previousTime) / timePerFrame;
			
			previousTime = currentTime;
			
			if(deltaF >= 1) {//update the render
				
				renderPanel.repaint();
				
				
				deltaF--;
				frames++;
			}
			
			//calculates every second
			if(System.currentTimeMillis() - lastCheck >= 1000) {
				lastCheck = System.currentTimeMillis();
				
			    String name;
			    if(mainProject != null) name = mainProject.getName();
			    else name = "No Project";
				
				frame.setTitle(String.format("3D Modeler, %s, Last save time: %s, FPS: %d", name, timeStamp, frames));
				frames = 0;
			}
			
		}
	}

	/**
	 * Create the application.
	 */
	public PanelEngine(RenderPanel renderPanel, JFrame frame) {
		initialize(renderPanel, frame);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(RenderPanel renderPanel, JFrame frame) {

		frametest = frame;
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(5, 0));

		frame.getContentPane().setBackground(Color.lightGray);
		frame.setResizable(true);
		
		ImageIcon logo = new ImageIcon("icons/Vertex.png");
		frame.setIconImage(logo.getImage());
		
		frame.setSize(900, 600);
				
		renderPanel.setPreferredSize(frame.getSize());
		frame.setBackground(Color.LIGHT_GRAY);		
		frame.getContentPane().add(renderPanel, BorderLayout.CENTER);
		
//		//left pane
//		JTabbedPane tabbedPane = new JTabbedPane();
//		tabbedPane.setFocusable(false);
//		
//        JPanel hierarchyTabPanel = new JPanel();
//        tabbedPane.addTab("Hierarchy", hierarchyTabPanel);
//        hierarchyTabPanel.setBackground(Color.DARK_GRAY);
//        hierarchyTabPanel.setLayout(new BoxLayout(hierarchyTabPanel, BoxLayout.Y_AXIS));
//
//        JTree tree = new JTree();
//        tree.setFocusable(false);
//        JScrollPane treeScrollPane = new JScrollPane(tree);
//        treeScrollPane.setPreferredSize(new Dimension(150, treeScrollPane.getPreferredSize().height));
//        
//        DefaultTreeCellRenderer cellRenderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
//        cellRenderer.setBackgroundNonSelectionColor(new Color(0, 0, 0, 0));
//        cellRenderer.setTextNonSelectionColor(Color.white);
//        tree.setBackground(Color.DARK_GRAY);
//        hierarchyTabPanel.add(treeScrollPane);
//        
//        
//        frame.add(tabbedPane, BorderLayout.WEST);
		
		menuBar = new JMenuBar();
		menuBar.setBorderPainted(false);
		menuBar.setBackground(Color.DARK_GRAY);
		frame.getContentPane().add(menuBar, BorderLayout.NORTH);
		
		mnNewMenu = new JMenu("File");
		mnNewMenu.setForeground(Color.white);
		mnNewMenu.setBackground(SystemColor.desktop);
		menuBar.add(mnNewMenu);
		
		editMenu = new JMenu("Edit");
		editMenu.setForeground(Color.white);
		editMenu.setBackground(SystemColor.desktop);
		menuBar.add(editMenu);
		
		helpMenu = new JMenu("Help");
		helpMenu.setForeground(Color.white);
		helpMenu.setBackground(SystemColor.desktop);
		menuBar.add(helpMenu);
		
		mnNewMenu_2 = new JMenu("Project");
		mnNewMenu.add(mnNewMenu_2);
		
		mntmNewMenuItem_3 = new JMenuItem("New project");
		mnNewMenu_2.add(mntmNewMenuItem_3);
		mntmNewMenuItem_3.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	        	 int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to create a new project? \nAny unsaved informations will be lost",null, JOptionPane.YES_NO_OPTION);
        			if(result == JOptionPane.YES_OPTION) {
        				System.out.println("New project Created");
        				mainProject = null;
        			    renderPanel.newProjcet();
        			} 
        			
	         }
	      });
		
		
		mntmNewMenuItem_4 = new JMenuItem("Open Project");
		mnNewMenu_2.add(mntmNewMenuItem_4);
		mntmNewMenuItem_4.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	            JFileChooser fileChooser = new JFileChooser();
	            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	            int option = fileChooser.showOpenDialog(frame);
	            if(option == JFileChooser.APPROVE_OPTION){
	               File file = fileChooser.getSelectedFile();
	               System.out.println("Folder Selected: " + file.getName() + ", path: " + file.getAbsolutePath());
	               
	               if(isProject(file)) {
	            	    try {
	            	    	mainProject = file;
	            		    loadProjectFiles(mainProject, renderPanel, frame);
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
	               }
	               else {
	            	   System.out.println("Open command canceled");
		            	JOptionPane.showMessageDialog(frame, "Selected directory is not a Project");
	               }
	               
	               
	            }
	            else{
	            	System.out.println("Open command canceled");
	            	JOptionPane.showMessageDialog(frame, "No Project Directory Selected");	            	
	            }
	         }

			private boolean isProject(File file) {
				File fileList[] = file.listFiles();
				for(File f:fileList) {
					if(f.getName().equals("project.txt"))
						return true;
				}
				return false;
			}

	      });
		
		mntmNewMenuItem_5 = new JMenuItem("Save Project as");
		mnNewMenu_2.add(mntmNewMenuItem_5);
		mntmNewMenuItem_5.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	        	 
	        	 String projectName = JOptionPane.showInputDialog(frame, "Enter project name:");

	             if (projectName != null && !projectName.isEmpty()) {
	                 // Process the project name (store it in a variable, etc.)
	                 System.out.println("Project Name: " + projectName);
	                 // choose path
	                 
	                JFileChooser fileChooser = new JFileChooser();
	 	            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	 	            int option = fileChooser.showOpenDialog(frame);
	 	            if(option == JFileChooser.APPROVE_OPTION){
	 	               File file = fileChooser.getSelectedFile();
	 	               if(!file.exists()) {
	 	            	   JOptionPane.showMessageDialog(frame, "The file path is invalid", "Error", JOptionPane.ERROR_MESSAGE);
	 	            	   System.out.println("Error");
	 	               }
	 	               else {
	 	            	   
		 	               System.out.println("Folder Selected: " + file.getName() + ", path: " + file.getAbsolutePath());
		 	               saveProjcetas(file, projectName, renderPanel, frame);
		 	              if(!file.exists()) JOptionPane.showMessageDialog(frame, "The Project Created Seccussfully");
	 	               }
	 	            }
	 	            else if(option == JFileChooser.CANCEL_OPTION){
	 	            	System.out.println("Open command canceled " + option);
	 	            	JOptionPane.showMessageDialog(frame, "Operation is Canceled");	            	
	 	            }
	                 
	             } else {
	                 System.out.println("Project creation canceled.");
	                 if(projectName != null) JOptionPane.showMessageDialog(frame, "Project name can't be Empty");
	             }
	        	 
	         }

			
			
	      });
		
		mntmNewMenuItem_6 = new JMenuItem("Save Project");
		mnNewMenu_2.add(mntmNewMenuItem_6);
		mntmNewMenuItem_6.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	        	 
	        	 if(mainProject == null) {
	        		 JOptionPane.showMessageDialog(frame, "You might save as new Project");
	        	 }
	        	 else {
	        		 JOptionPane.showMessageDialog(frame, "Project saved");
	        		 timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
	        		 saveProjcet(renderPanel, frame);
	        	 }
	        	 
	         }
	      });
		
		mnNewMenu_1 = new JMenu("Model");
		mnNewMenu.add(mnNewMenu_1);
		
		mntmNewMenuItem_1 = new JMenuItem("Export Model");
		mnNewMenu_1.add(mntmNewMenuItem_1);
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	        	 
	        	 String modelName = JOptionPane.showInputDialog(frame, "Enter Model name:");

	             if (modelName != null && !modelName.isEmpty()) {
	                 // Process the project name (store it in a variable, etc.)
	                 System.out.println("modelName Name: " + modelName);
	                 // choose path
	                 
	                JFileChooser fileChooser = new JFileChooser();
	 	            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	 	            int option = fileChooser.showOpenDialog(frame);
	 	            if(option == JFileChooser.APPROVE_OPTION){
	 	               File file = fileChooser.getSelectedFile();
	 	               System.out.println("Folder Selected: " + file.getName() + ", path: " + file.getAbsolutePath());
	 	               try {
						ExportModel(file, modelName, renderPanel, frame);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	 	            }
	 	            else{
	 	            	System.out.println("Open command canceled");
	 	            	JOptionPane.showMessageDialog(frame, "No Project Directory Selected");	            	
	 	            }
	                 
	             } else {
	                 System.out.println("model Name creation canceled.");
	                 if(modelName != null) JOptionPane.showMessageDialog(frame, "model Name can't be Empty");
	             }
	        	 
	         }

			private void ExportModel(File folder, String modelName, RenderPanel renderPanel, JFrame frame) throws IOException {
				// TODO Auto-generated method stub
				Matrix4 rotMX=renderPanel.rotMX ,rotMZ=renderPanel.rotMZ,rotMY=renderPanel.rotMY;
				
				BufferedWriter writer2;
				FileWriter fileWriter = new FileWriter(new File(folder, "\\" + modelName + ".obj"));
				writer2 = new BufferedWriter(fileWriter);
				
				int i = 0;
				for(MeshObject m:renderPanel.meshObj) {
					
					ArrayList<Vect3D> vects = new ArrayList<>();
					renderPanel.rotate(m);
					for(Triangle t: m.getObj()) {
						
						Triangle triScaled = new Triangle();
						triScaled.v[0] = t.v[0].scaleX(m.scaleX);
						triScaled.v[1] = t.v[1].scaleX(m.scaleX);
						triScaled.v[2] = t.v[2].scaleX(m.scaleX);
						
						triScaled.v[0] = triScaled.v[0].scaleY(m.scaleY);
						triScaled.v[1] = triScaled.v[1].scaleY(m.scaleY);
						triScaled.v[2] = triScaled.v[2].scaleY(m.scaleY);
						
						triScaled.v[0] = triScaled.v[0].scaleZ(m.scaleZ);
						triScaled.v[1] = triScaled.v[1].scaleZ(m.scaleZ);
						triScaled.v[2] = triScaled.v[2].scaleZ(m.scaleZ);
						
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

						triTranslated.v[0] = triRotatedZ.v[0].transform(m.x + t.v[0].tx, m.y + t.v[0].ty, m.z + t.v[0].tz);
						triTranslated.v[1] = triRotatedZ.v[1].transform(m.x + t.v[1].tx, m.y + t.v[1].ty, m.z + t.v[1].tz);
						triTranslated.v[2] = triRotatedZ.v[2].transform(m.x + t.v[2].tx, m.y + t.v[2].ty, m.z + t.v[2].tz);
						
						for(Vect3D v: triTranslated.getVs()) {
							
							if(!vects.contains(v)) vects.add(v);

						}

					}
					
					for(Vect3D v: vects) {
						writer2.write("v " + v.getX() +" "+ + v.getY() +" "+ v.getZ());
						writer2.newLine();
					}
					
					
					for(Triangle t: m.getObj()) {

						writer2.write("f " + (t.v1) +" "+ (t.v2) +" "+ (t.v3));
						writer2.newLine();
					}
					
					for(Vect3D v: vects) {
						i++;
					}
					vects.clear();
					
					
				}
				writer2.flush();
				try {
					writer2.close();
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}

			
			
	      });
		
		
		
		
		
		mntmNewMenuItem_2 = new JMenuItem("Import Model");
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			 public void actionPerformed(ActionEvent e) {
	                // Create a file chooser
	                JFileChooser fileChooser = new JFileChooser();
	                fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
	                fileChooser.setFileFilter(new FileNameExtensionFilter("Obj Files", "obj"));
	                // Show the file chooser dialog
	                int result = fileChooser.showOpenDialog(frame);
	                if (result == JFileChooser.APPROVE_OPTION) {
	                    // User selected a file
	                    File selectedFile = fileChooser.getSelectedFile();
	                    renderPanel.filesHandler.addFile(selectedFile);
	                    mntmNewMenuItem.setText("Load Model ("+renderPanel.filesHandler.objFiles.size()+")");
	                }
	            }
		});
		mnNewMenu_1.add(mntmNewMenuItem_2);
		
		mntmNewMenuItem = new JMenuItem("Load Model (0)");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		        // Get the list of model names from your local ArrayList
		        ArrayList<String> modelNames = renderPanel.filesHandler.getModelNames();
		        
		        if (modelNames.isEmpty()) {
		            JOptionPane.showMessageDialog(frame, "No models available for loading.");
		        } else {
		            // Display a JOptionPane with the list of model names for the user to choose from
		            String[] options = modelNames.toArray(new String[0]);
		            String selectedModel = (String) JOptionPane.showInputDialog(
		                frame,
		                "Select a model to load internally:",
		                "Load Model Internally",
		                JOptionPane.QUESTION_MESSAGE,
		                null,
		                options,
		                options[0]
		            );
		            if (selectedModel != null) {
		                // Get the index of the selected model in the list
		                int selectedIndex = modelNames.indexOf(selectedModel);

		                // Now you have the selected index, you can add it as an obj
		                renderPanel.addObj(new MeshObject(renderPanel.filesHandler, selectedIndex));
		            }
		        }
		    }
		});
		
		mnNewMenu_1.add(mntmNewMenuItem);
		
		mntmNewMenuItem_6 = new JMenuItem("Remove Loaded Model");
		mntmNewMenuItem_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                
				// Get the list of model names from your local ArrayList
		        ArrayList<String> modelNames = renderPanel.getLoadedObjs();

		        if (modelNames.isEmpty()) {
		            JOptionPane.showMessageDialog(frame, "No Loaded Models To Remove.");
		        } else {
		            // Display a JOptionPane with the list of model names for the user to choose from
		            String[] options = modelNames.toArray(new String[0]);
		            String selectedModel = (String) JOptionPane.showInputDialog(
		                frame,
		                "Select a model to load internally:",
		                "Load Model Internally",
		                JOptionPane.QUESTION_MESSAGE,
		                null,
		                options,
		                options[0]
		            );
		            if (selectedModel != null) {
		                // Get the index of the selected model in the list
		                int selectedIndex = modelNames.indexOf(selectedModel);

		                // Now you have the selected index, you can add it as an obj
		                renderPanel.removeObj(selectedIndex);
		            }
		        }
		    }
		});
		mnNewMenu_1.add(mntmNewMenuItem_6);
		frame.setLocationRelativeTo(null);
	}
	
	private void loadProjectFiles(File file, RenderPanel renderPanel, JFrame frame) throws IOException {
		// TODO Auto-generated method stub
		renderPanel.meshObj.clear();
		File fileList[] = file.listFiles();
		for(File f:fileList) {
			if(!f.getName().equals("project.txt")) {
				renderPanel.filesHandler.addFile(f);
				renderPanel.addObj(new MeshObject(f));
			}
			else {
				System.out.println("project.txt found");
			}
			
		}
		applyProp(renderPanel, file, frame);
	}

	private void applyProp(RenderPanel renderPanel, File file, JFrame frame) throws IOException {
		// TODO Auto-generated method stub
		File project = new File(file.getAbsolutePath() + "/project.txt");
		FileReader freader = new FileReader(project);
		BufferedReader reader = new BufferedReader(freader);
		String line;
		while ((line = reader.readLine()) != null) {
			if(!line.isBlank()) {
				String parts[] = line.split("\\s+");
				MeshObject obj = renderPanel.getMesh(parts[0]);
				if(obj != null) {
					obj.x = Double.parseDouble(parts[1]); 
					obj.y = Double.parseDouble(parts[2]); 
					obj.z = Double.parseDouble(parts[3]); 
					
					obj.rotX = Double.parseDouble(parts[4]); 
					obj.rotY = Double.parseDouble(parts[5]); 
					obj.rotZ = Double.parseDouble(parts[6]);
					
					obj.scaleX = Double.parseDouble(parts[7]); 
					obj.scaleY = Double.parseDouble(parts[8]); 
					obj.scaleZ = Double.parseDouble(parts[9]); 
				}
				else {
					JOptionPane.showMessageDialog(frame, "'" + parts[0] + "' file does not exist");
				}
			}
			
		}
		try {
			freader.close();
			reader.close();
		} catch (Exception e) {
			// TODO: handle exception
		} 
	}
	
	private void saveProjcetas(File file, String projectName, RenderPanel renderPanel, JFrame frame) {
		// TODO Auto-generated method stub
		try {
			File folder = new File(file.getAbsolutePath()+"\\"+projectName);
			
			if(!folder.exists()) {
				folder.mkdirs();
				FileWriter fileWriter = new FileWriter(new File(folder, "\\project.txt"));
				BufferedWriter writer = new BufferedWriter(fileWriter);
				mainProject = folder;
				timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
				
				for(MeshObject m:renderPanel.meshObj) {
					writer.write(m.getName() + " " + m.x + " " + m.y + " " + m.z + " " + m.rotX + " " + m.rotY + " " + m.rotZ + " " + m.scaleX + " " + m.scaleY + " " + m.scaleZ);
					writer.newLine();
				}
				
				writer.flush();
				try {
					writer.close();
				} catch (Exception e) {
					// TODO: handle exception
				} 
				
				BufferedWriter writer2;
				for(MeshObject m:renderPanel.meshObj) {
					fileWriter = new FileWriter(new File(folder, "\\" + m.getName()));
					writer2 = new BufferedWriter(fileWriter);
					ArrayList<Vect3D> vects = new ArrayList<>();
					for(Triangle t: m.getObj()) {
						
						for(Vect3D v: t.getVs()) {
							
							if(!vects.contains(v)) vects.add(v);

						}

					}
					for(Vect3D v: m.verts) {
						writer2.write("v " + v.getX() +" "+ + v.getY() +" "+ v.getZ());
						writer2.newLine();
					}
					
					for(Triangle t: m.getObj()) {
						
						writer2.write("f " + t.v1 +" "+ t.v2 +" "+ t.v3);
						writer2.newLine();
					}
					
					writer2.flush();
					try {
						writer2.close();
					} catch (Exception e) {
						// TODO: handle exception
					}
					
				}
			}
			else {
				JOptionPane.showMessageDialog(frame, "project folder already exists", "Error", JOptionPane.ERROR_MESSAGE);
			}
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void saveProjcet(RenderPanel renderPanel, JFrame frame) {
		// TODO Auto-generated method stub
		try {
			File folder = new File(mainProject.getAbsolutePath());
			System.out.println(folder.getAbsolutePath());
			if(folder.exists()) {
				FileWriter fileWriter = new FileWriter(new File(folder, "\\project.txt"));
				BufferedWriter writer = new BufferedWriter(fileWriter);
				mainProject = folder;
				timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
				
				for(MeshObject m:renderPanel.meshObj) {
					writer.write(m.getName() + " " + m.x + " " + m.y + " " + m.z + " " + m.rotX + " " + m.rotY + " " + m.rotZ + " " + m.scaleX + " " + m.scaleY + " " + m.scaleZ);
					writer.newLine();
				}
				
				writer.flush();
				try {
					writer.close();
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				
				BufferedWriter writer2;
				for(MeshObject m:renderPanel.meshObj) {
					fileWriter = new FileWriter(new File(folder, "\\" + m.getName()));
					writer2 = new BufferedWriter(fileWriter);
					ArrayList<Vect3D> vects = new ArrayList<>();
					for(Triangle t: m.getObj()) {
						
						for(Vect3D v: t.getVs()) {
							
							if(!vects.contains(v)) vects.add(v);

						}

					}
					for(Vect3D v: m.verts) {
						writer2.write("v " + v.getX() +" "+ + v.getY() +" "+ v.getZ());
						writer2.newLine();
					}
					
					for(Triangle t: m.getObj()) {
						
						writer2.write("f " + t.v1 +" "+ t.v2 +" "+ t.v3);
						writer2.newLine();
					}
					
					writer2.flush();
					try {
						writer2.close();
					} catch (Exception e) {
						// TODO: handle exception
					}
					
				}
			}
			else {
				JOptionPane.showMessageDialog(frame, "Error");
			}
				
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

