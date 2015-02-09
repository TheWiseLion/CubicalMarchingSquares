package VoxelSystem.VoxelExtractors;

import VoxelSystem.Data.VoxelCube;
import VoxelSystem.Data.DensityVolumes.DensityVolume;
import VoxelSystem.utils.VoxelSystemTables;
import VoxelSystem.utils.VoxelSystemTables.AXIS;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector3f;

/***
 * Converts from a density function to voxel data
 * @author 0xFFFF
 *
 */
public class VoxelConverter implements VoxelExtractor{
	final DensityVolume dv;
	
	public VoxelConverter(DensityVolume dv){
		this.dv = dv;
	}

	@Override
	public VoxelCube getVoxels(final Vector3f cubeCorner,final float len) {
		return new VoxelCube() {
			@Override
			public int getType(int corner) {
				int dx = VoxelSystemTables.cTable[corner*3];
				int dy = VoxelSystemTables.cTable[corner*3 + 1];
				int dz = VoxelSystemTables.cTable[corner*3 + 2];
				
				float x = cubeCorner.x + dx * len;
				float y = cubeCorner.y + dy * len;
				float z = cubeCorner.z + dz * len;
				
				return dv.getType(x,y,z);
			}
			
			@Override
			public Vector3f getNormal(int edgeNum, Vector3f out) {
				getIntersectionPoint(edgeNum,out);
				dv.getSurfaceNormal(out.x, out.y, out.z, out);
				return out;
			}
			
			@Override
			public Vector3f getIntersectionPoint(int edgeNum, Vector3f out) {
				int corner1 = VoxelSystemTables.iTable[edgeNum*2];
				int dx = VoxelSystemTables.cTable[corner1*3];
				int dy = VoxelSystemTables.cTable[corner1*3 + 1];
				int dz = VoxelSystemTables.cTable[corner1*3 + 2];
				
				float x = cubeCorner.x + dx * len;
				float y = cubeCorner.y + dy * len;
				float z = cubeCorner.z + dz * len;
				
				out.set(x,y,z);
				float d1 = dv.getDensity(x, y,z);
				
				AXIS a = VoxelSystemTables.aTable[edgeNum];
				if(a == AXIS.X){
					x+= len;
				}else if(a == AXIS.Y){
					y+=len;
				}else{
					z+=len;
				}
				
				float d2 = dv.getDensity(x, y, z);
				
				if(d1 >0 && d2 >0 || d1<=0 && d2 <= 0){
					throw new RuntimeException("Invalid query");
				}
				
				
				float f =  VoxelSystemTables.getIntersection(d1, d2);
				out.x += (x - out.x) * f;
				out.y += (y - out.y) * f;
				out.z += (z - out.z) * f;
				
				return out;
			}
			
			@Override
			public float getIntersection(int edgeNum) {
				int corner1 = VoxelSystemTables.iTable[edgeNum*2];
				int dx = VoxelSystemTables.cTable[corner1*3];
				int dy = VoxelSystemTables.cTable[corner1*3 + 1];
				int dz = VoxelSystemTables.cTable[corner1*3 + 2];
				
				float x = cubeCorner.x + dx * len;
				float y = cubeCorner.y + dy * len;
				float z = cubeCorner.z + dz * len;
				
				float d1 = dv.getDensity(x, y,z);
				
				AXIS a = VoxelSystemTables.aTable[edgeNum];
				if(a == AXIS.X){
					x+= len;
				}else if(a == AXIS.Y){
					y+=len;
				}else{
					z+=len;
				}
				
				float d2 = dv.getDensity(x, y, z);
				
				if(d1 > 0 && d2 > 0 || d1 <= 0 && d2 <= 0){
					int t1 = dv.getType(cubeCorner.x + dx * len, cubeCorner.y + dy * len,
							cubeCorner.z + dz * len);
					int t2 = dv.getType(x, y, z);
					int t3 = this.getType(6);
					int t4 = this.getType(7);
					
					
					throw new RuntimeException("Invalid query");
				}
				
				
				return VoxelSystemTables.getIntersection(d1, d2);
			}
			
			@Override
			public float getCubeLength() {
				return len;
			}
			
			@Override
			public Vector3f getCorner(int corner, Vector3f out) {
				int dx = VoxelSystemTables.cTable[corner*3];
				int dy = VoxelSystemTables.cTable[corner*3 + 1];
				int dz = VoxelSystemTables.cTable[corner*3 + 2];
				
				float x = cubeCorner.x + dx * len;
				float y = cubeCorner.y + dy * len;
				float z = cubeCorner.z + dz * len;
				out.set(x,y,z);
				return out;
			}
			
			@Override
			public Vector3f getCorner() {
				return cubeCorner;
			}
		};
	}

	@Override
	public BoundingBox getVolume() {
		return dv.getEffectiveVolume();
	}

	@Override
	public boolean shouldSplitQuery(Vector3f cubeCorner, float len) {
		return true;
	}
}
