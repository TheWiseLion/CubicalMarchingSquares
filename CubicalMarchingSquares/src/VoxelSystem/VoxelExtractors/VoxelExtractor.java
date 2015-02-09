package VoxelSystem.VoxelExtractors;


import VoxelSystem.Data.VoxelCube;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector3f;

/***
 * Interface for obtaining voxel data
 * This interface will allow for both octree's and grid based things
 * however it will be prone to floating point errors
 */
public interface VoxelExtractor {

	/***
	 * Takes corner 0 of the cube
	 * @param cubeCorner - corner 0 of the cube
	 * @param len - length/width/height of cube
	 * @return
	 */
	public VoxelCube getVoxels(Vector3f cubeCorner, float len);
	
//	public VoxelEdge getEdge(Vector3f p1, Vector3f p2);

	/**
	 * Super sampling an octree can create many ambiguities. 
	 * To prevent that this function indicates if a query will 
	 * return a meaningful result.
	 * 
	 * In addition this will vastly increase the speed of many
	 * shapes e.g. cubes and spheres since most of the volume
	 * need not be queried. 
	 * <- Gives extractor ability to direct what needs to be extracted -> 
	 *
	 * @param cubeCorner
	 * @param len
	 * @return true if the query should be further subdivided
	 */
	public boolean shouldSplitQuery(Vector3f cubeCorner, float len);
	
	/**
	 * Returns bounding volume for voxel surface
	 * TODO: BoundingVolume instead of bounding box!!!
	 * @return
	 */
	public BoundingBox getVolume();
}
