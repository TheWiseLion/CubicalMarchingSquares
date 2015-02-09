package VoxelSystem.Data.Storage;


import VoxelSystem.utils.VoxelSystemTables;
import VoxelSystem.utils.VoxelSystemTables.AXIS;
import VoxelSystem.Data.VoxelCube;

import com.jme3.math.Vector3f;

/**
 * The storage implementation of the tree. 
 * 
 * @author wiselion
 *
 */
public class BasicVoxelTree extends OctreeNode{

	int [] materials;//TODO: JME Material
	Vector3f [] normals;
	float [] intersections;

	public BasicVoxelTree(Vector3f lowestCorner,float length){
		super(lowestCorner, length);
		 materials = new int[]{
			-1,-1,-1,-1,
			-1,-1,-1,-1	 
		 };
		 normals = new Vector3f[12];
		 intersections = new float[12];
	}
	
	@Override
	public Vector3f getCorner() {
		return this.minBound;
	}

	@Override
	public float getCubeLength() {
		return this.length;
	}

	@Override
	public Vector3f getCorner(int corner, Vector3f out) {
		int dx = VoxelSystemTables.cTable[corner*3];
		int dy = VoxelSystemTables.cTable[corner*3 + 1];
		int dz = VoxelSystemTables.cTable[corner*3 + 2];
		
		out.set(this.minBound.x + dx*this.length, this.minBound.y + dy*this.length, this.minBound.z + dz*this.length);
		return out;
	}

	@Override
	public int getType(int corner) {
		return materials[corner];
	}

	@Override
	public Vector3f getIntersectionPoint(int edgeNum, Vector3f out) {
		float f = intersections[edgeNum] * length;
		int corner = VoxelSystemTables.iTable[edgeNum*2];
		getCorner(corner, out);
		
		AXIS a = VoxelSystemTables.aTable[edgeNum];
		if(a == AXIS.X){
			out.addLocal(f, 0, 0);
		}else if(a == AXIS.Y){
			out.addLocal(0, f, 0);
		}else{
			out.addLocal(0, 0, f);
		}
		
		return out;
	}

	@Override
	public float getIntersection(int edgeNum) {
			return intersections[edgeNum];
	}

	@Override
	public Vector3f getNormal(int edgeNum, Vector3f out) {
		Vector3f n = normals[edgeNum];
		if(n == null){
			out.set(0,0,0);
		}else{
			out.set(n);
		}
		return out;
	}
	
	@Override
	public boolean setType(int corner, int type) {
		boolean change = materials[corner]!=type;
		materials[corner]=type;
		return change;
	}

	@Override
	public boolean setNormal(int edgeNum, Vector3f normal) {
		if(normal.lengthSquared() > 1.1){
			System.out.println("WHO DAIRS");
		}
		boolean change = normals[edgeNum]==null || normals[edgeNum].equals(normal);
		normals[edgeNum] = normal;
		return change;
	}

	@Override
	public boolean setIntersection(int edgeNum, Float value) {
		boolean change = intersections[edgeNum] != value;
		intersections[edgeNum] = value;
		return change;
	}
	
	
	/**
	 * boiler
	 */
	@Override
	public OctreeNode produceChild(Vector3f lowestCorner, float length) {
		return new BasicVoxelTree(lowestCorner,length);
	}
	
}
