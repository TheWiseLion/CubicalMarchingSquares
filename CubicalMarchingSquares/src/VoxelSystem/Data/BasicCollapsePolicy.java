package VoxelSystem.Data;

import VoxelSystem.Data.Storage.OctreeNode;
import VoxelSystem.utils.ExtractorUtils;
import VoxelSystem.utils.VoxelSystemTables;

import com.jme3.math.Vector3f;

/***
 * A simple implementation of a split policy that using the most basic downsampling
 * @author 0xFFFF
 */
public class BasicCollapsePolicy implements CollapsePolicy{
	
	float error;
	public BasicCollapsePolicy(float miniumGeoError){
		error = miniumGeoError;
	}
	
	@Override
	public boolean collapse(OctreeNode[] children, EditableVoxelCube cube) {
		
		
		//The 8 cube corners taken from the children
		cube.setType(0,children[0].getType(0));
		cube.setType(1,children[1].getType(1));
		
		cube.setType(2,children[3].getType(2)); //hmmm		
		cube.setType(3,children[2].getType(3)); //hmmm
		
		cube.setType(4,children[4].getType(4));		
		cube.setType(5,children[5].getType(5));
		
		cube.setType(6,children[7].getType(6)); //hmmm
		cube.setType(7,children[6].getType(7));
		
		//For each active edge get lowest point on that edge
		int edgeInfo = VoxelSystemTables.getEdgeFromMaterials(cube);
		
		for(int i =0; i< 12; i++){
			if ((edgeInfo & (1 << i)) == 0){
				continue;
			}
			
			//Get the lowest point on the edge (there may be as many as two)
			int t1 = cube.getType(VoxelSystemTables.iTable[i*2]);
			OctreeNode n = getE(i, t1, children);
			float a = 0;
			
			int t2 = ExtractorUtils.getMiddleType(i, children);
			if(t1 == t2){ //ugh.
				a += .5f;
			}
			
			Vector3f normal = n.getNormal(i, new Vector3f());
			cube.setNormal(i, normal);
			cube.setIntersection(i, n.getIntersection(i)*.5f + a);
			if(normal == null){
				throw new RuntimeException("WHYY mee i was so young...");
			}
		}
		
		
		return ExtractorUtils.checkSchaeferTopology(children, cube); 
	}

	//return octree node for edge + normal
	//Hard code is the magic.
	private OctreeNode getE(int edge,int t1, OctreeNode[]children){
		switch(edge){
		case 0:
			if(children[1].getType(0) == t1){
				return children[1];
			}else{
				return children[0];
			}
		case 1:
			if(children[3].getType(1) == t1){
				return children[3];
			}else{
				return children[1];
			}
		case 2:
			if(children[3].getType(3) == t1){
				return children[3];
			}else{
				return children[2];
			}
		case 3:
			if(children[2].getType(0) == t1){
				return children[2];
			}else{
				return children[0];
			}
		case 4:
			if(children[5].getType(4) == t1){
				return children[5];
			}else{
				return children[4];
			}
		case 5:
			if(children[5].getType(6) == t1){
				return children[7];
			}else{
				return children[5];
			}
		case 6:
			if(children[6].getType(6) == t1){
				return children[7];
			}else{
				return children[6];
			}
		case 7:
			if(children[4].getType(7) == t1){
				return children[6];
			}else{
				return children[4];
			}
		case 8:
			if(children[4].getType(0) == t1){
				return children[4];
			}else{
				return children[0];
			}
		case 9:
			if(children[1].getType(5) == t1){
				return children[5];
			}else{
				return children[1];
			}
		case 10:
			if(children[3].getType(6) == t1){
				return children[7];
			}else{
				return children[3];
			}
		case 11:
			if(children[2].getType(7) == t1){
				return children[6];
			}else{
				return children[2];
			}
		default:
			throw new RuntimeException("Not a valid edge");
		}
	}

	@Override
	public float getMiniumGeometricError() {
		return error;
	}
	
	
}
