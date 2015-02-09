package VoxelSystem.VoxelMaterials;

import com.jme3.material.Material;


public class VoxelMaterial {
	public String name;
	public Material jMEMaterial;
	private boolean renderSubsurface = false;
	int typeID;
	
	/***
	 * This is true if setRenderSubsurface is called with argument true.
	 * If this is true then FaceCullMode will be disable and blend mode will be set to
	 * BlendMode.Alpha and queue bracket set to Bucket.Transparent
	 * 
	 * @return
	 */
	public boolean willRenderSubsurface(){
		return renderSubsurface;
	}
	
	public void setRenderSubsurface(boolean b){
		renderSubsurface = b;
	}
	
	public int getUniqueID(){
		return typeID;
	}
	
}
