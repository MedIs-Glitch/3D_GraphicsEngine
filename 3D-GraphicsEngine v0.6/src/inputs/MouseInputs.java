package inputs;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import Code.MeshObject;
import Code.Triangle;
import Code.Vect3D;

public class MouseInputs implements MouseMotionListener, MouseListener {
	
	public static class MouseStaticInputs {
		public static boolean isPressed, isRightPressed, isDragged;
		public static double rotationY, rotationX;
		public static double preX = rotationX, preY = rotationY, dragX, dragY;
	}
	
	JPanel panel;
	MeshObject model;
	Vect3D screenPos = new Vect3D(0,0,0);
	private double sensetivity = 0.5;
	
	
	public MouseInputs(JPanel panel) {
		this.panel = panel;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		screenPos.setX(e.getLocationOnScreen().x - panel.getLocationOnScreen().x);
		screenPos.setY(e.getLocationOnScreen().y - panel.getLocationOnScreen().y);
		//System.out.println(screenPos.getX()+ ", " + (screenPos.getY()));
		
		if(MouseStaticInputs.isRightPressed) {
			MouseStaticInputs.rotationY = (screenPos.getY() - MouseStaticInputs.preY) * sensetivity;
			MouseStaticInputs.rotationX = (screenPos.getX() - MouseStaticInputs.preX) * sensetivity;
			
			//System.out.println(MouseStaticInputs.rotationX + ", " + MouseStaticInputs.rotationY);
		}
		if(MouseStaticInputs.isPressed) {
			MouseStaticInputs.dragX = (screenPos.getY() - MouseStaticInputs.preY) * sensetivity;
			MouseStaticInputs.dragY = (screenPos.getX() - MouseStaticInputs.preX) * sensetivity;
			
			//System.out.println(MouseStaticInputs.dragX + ", " + MouseStaticInputs.dragY);
			MouseStaticInputs.isDragged = true;
		}
		//refresh();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		screenPos.setX(e.getLocationOnScreen().x - panel.getLocationOnScreen().x);
		screenPos.setY(e.getLocationOnScreen().y - panel.getLocationOnScreen().y);
		MouseStaticInputs.isDragged = false;
	}
	
	public void addModel(MeshObject model) {
		this.model = model;
	}
	
	public boolean isMouseInTriangle(Triangle t) {
	    double x = screenPos.getX();
	    double y = screenPos.getY();
	    
	    double x1 = t.getV(0).getX();
	    double y1 = t.getV(0).getY();
	    double x2 = t.getV(1).getX();
	    double y2 = t.getV(1).getY();
	    double x3 = t.getV(2).getX();
	    double y3 = t.getV(2).getY();
	    
	    double denominator = (y2 - y3) * (x1 - x3) + (x3 - x2) * (y1 - y3);
	    
	    double alpha = ((y2 - y3) * (x - x3) + (x3 - x2) * (y - y3)) / denominator;
	    double beta = ((y3 - y1) * (x - x3) + (x1 - x3) * (y - y3)) / denominator;
	    double gamma = 1 - alpha - beta;
	    
	    return alpha >= 0 && beta >= 0 && gamma >= 0;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		//System.out.println("mouse pressed");
		if(e.getButton() == MouseEvent.BUTTON3) {
			System.out.println("right clicked");
			MouseStaticInputs.isRightPressed = true;
		}
		else if(e.getButton() == MouseEvent.BUTTON1) {
			System.out.println("left pressed");
			MouseStaticInputs.isPressed = true;
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//System.out.println("mouse released");
		//isPressed = false;
		if(e.getButton() == MouseEvent.BUTTON3) {
			System.out.println("right released");
			MouseStaticInputs.isRightPressed = false;
		}
		else if(e.getButton() == MouseEvent.BUTTON1) {
			System.out.println("Left released");
			MouseStaticInputs.isPressed = false;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}
	
	public void refresh() {
		MouseStaticInputs.preY = screenPos.getY();
		MouseStaticInputs.preX = screenPos.getX();
		
		MouseStaticInputs.rotationY = screenPos.getY() - MouseStaticInputs.preY;
		MouseStaticInputs.rotationX = screenPos.getX() - MouseStaticInputs.preX;
		MouseStaticInputs.dragY = screenPos.getY() - MouseStaticInputs.preY;
		MouseStaticInputs.dragX = screenPos.getX() - MouseStaticInputs.preX;
	}
	
	public Vect3D getMousePos() {
		return screenPos;
	}

	
}