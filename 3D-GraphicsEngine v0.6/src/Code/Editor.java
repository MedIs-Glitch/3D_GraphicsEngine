package Code;

import java.util.ArrayList;

import inputs.KeyboardInputs;
import inputs.KeyboardInputs.keyInputs;
import inputs.MouseInputs;
import inputs.MouseInputs.MouseStaticInputs;

public class Editor {
	
	public static int editMode = 0;
	public static final int vertexMode = 0;
	public static final int faceMode = 1;
	public static final int allMeshMode = 2;
	
	
	public void update(ArrayList<Vect3D> Selectedvertices, ArrayList<MeshObject> selectedMesh){
			
		if(!KeyboardInputs.keyInputs.isRPressed && !KeyboardInputs.keyInputs.isSPressed)
			if(Editor.editMode == Editor.faceMode || Editor.editMode == Editor.vertexMode)
				if(!keyInputs.isShiftPressed)
					for(Vect3D v:Selectedvertices) {
						if(!KeyboardInputs.keyInputs.isRPressed) v.transform(-(double)MouseStaticInputs.dragY/10,-(double) MouseStaticInputs.dragX/10, 0);
					}
				else
					for(Vect3D v:Selectedvertices) {
						if(!KeyboardInputs.keyInputs.isRPressed) v.transform(0,-(double) MouseStaticInputs.dragX/10, -(double)MouseStaticInputs.dragY/10);
					}
					
			else if(Editor.editMode == Editor.allMeshMode)
				if(!keyInputs.isShiftPressed)
					for(MeshObject m:selectedMesh) {
						m.translate(-(double)MouseStaticInputs.dragY/10,-(double) MouseStaticInputs.dragX/10, 0);
					}
				else
					for(MeshObject m:selectedMesh) {
						m.translate(0,-(double) MouseStaticInputs.dragX/10, -(double)MouseStaticInputs.dragY/10);
					}
		MouseStaticInputs.dragY = 0;
		MouseStaticInputs.dragX = 0;
	}
	
	public void updateRotation(MeshObject obj) {
		if(KeyboardInputs.keyInputs.isRPressed)
			obj.rotate((double)KeyboardInputs.keyInputs.horizontal,(double) KeyboardInputs.keyInputs.vertical, 0);
		if(KeyboardInputs.keyInputs.isXPressed) {
			
			obj.scaleY += (double)KeyboardInputs.keyInputs.vertical/10;
			if(!KeyboardInputs.keyInputs.isShiftPressed) {
				obj.scaleX += (double)KeyboardInputs.keyInputs.horizontal/10;
			}
			else {
				obj.scaleZ += (double)KeyboardInputs.keyInputs.horizontal/10;
			}
		}
	}
}
