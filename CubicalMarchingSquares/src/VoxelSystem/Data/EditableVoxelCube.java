package VoxelSystem.Data;


import com.jme3.math.Vector3f;

public interface EditableVoxelCube extends VoxelCube{
	
//	public void setNormal(int corner, AXIS a, Vector3f normal);
//	public void setIntersection(int corner,AXIS a, Float value);
	
	/***
	 * Returns true if there has been a change in value
	 */
	public boolean setType(int corner, int type);
	
	/***
	 * Returns true if there has been a change in value
	 */
	public boolean setNormal(int edgeNum, Vector3f normal);
	
	/***
	 * Returns true if there has been a change in value
	 */
	public boolean setIntersection(int edgeNum, Float value);
	
//	public boolean set(VoxelCube vc);
}
