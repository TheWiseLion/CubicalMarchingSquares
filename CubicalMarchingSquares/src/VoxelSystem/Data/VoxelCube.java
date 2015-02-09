package VoxelSystem.Data;


import com.jme3.math.Vector3f;

/***
 * Interface to allow for different underlying data thus allowing voxel
 * data to be stored as what ever primitive you like.
 */
public interface VoxelCube {
	/***
	 * Coordinates of corner 0.
	 * Which is corner with smallest x,y,z
	 */
	public Vector3f getCorner();
	public float getCubeLength();
	
	/***
	 * Coordinates of given corner
	 * @param corner
	 * @param out
	 */
	public Vector3f getCorner(int corner, Vector3f out);
	
	public int getType(int corner);
	
	public Vector3f getIntersectionPoint(int edgeNum, Vector3f out);
	public float getIntersection(int edgeNum);
	public Vector3f getNormal(int edgeNum, Vector3f out);
	
//	public Vector3f getIntersectionPoint(int corner, AXIS a, Vector3f out);
//	public Float getIntersection(int corner,AXIS a);
//	public Vector3f getNormal(int corner, AXIS a);
//	public int numSegments(int face);
//	public Vector3f[] segments(int face);
	

}
