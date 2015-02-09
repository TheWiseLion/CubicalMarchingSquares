package VoxelSystem.Data;

import VoxelSystem.Data.Storage.OctreeNode;

/***
 * Collapse Policy performs the following functions:
 * - Determines collapse values
 * - Determines if node is topologically safe (with respect to its children)
 * 
 * @author 0xFFFF
 *
 */
public interface CollapsePolicy {
	
	/***
	 * 
	 * @param children - the children of node for determining collapse values
	 * @param cubeCorners - the collapse values (output)
	 * @return boolean indicating whether or not its topologically safe.
	 */
	public boolean collapse(final OctreeNode [] children, EditableVoxelCube cube);
	
	public float getMiniumGeometricError();
}
