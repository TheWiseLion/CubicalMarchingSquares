package VoxelSystem;

import VoxelSystem.Data.BasicCollapsePolicy;
import VoxelSystem.Data.Storage.BasicVoxelTree;
import VoxelSystem.Data.Storage.OctreeNode;
import VoxelSystem.VoxelExtractors.VoxelExtractor;
import VoxelSystem.VoxelMaterials.MaterialRegistry;

import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;


/***
 * Contains a sparse tree of octree's
 * Mesh level of detail changes based on camera position 
 * and other parameters
 *
 */
public class VoxelObject {
	public float minVoxelSize;
	public MaterialRegistry materials;
	public OctreeNode root;
	Node meshCollection;

	//TODO: max voxel size?
	
	public VoxelObject(float minVoxel){
		this.minVoxelSize = minVoxel;
		root = new BasicVoxelTree(new Vector3f(-minVoxel*32,-minVoxel*32,-minVoxel*32/2),minVoxel*64);
		meshCollection = new Node();
	}
	
	public void setVoxelData(VoxelExtractor ve, float maxVoxelSize){
//		root = root.getRoot(ve.getVolume());
		root.extractVoxelData(ve, maxVoxelSize, new BasicCollapsePolicy(0));
		
	}
	
	public MaterialRegistry getMaterialRegistry(){
		return materials;
	}
	
	public void setMaterialRegistry(MaterialRegistry materials){
		this.materials = materials;
	}

	
	public Node getObjectNode() {
		return meshCollection;
	}

	public void render(Material cubeLines,Material edgePoint, Material normal) {
		// TODO Auto-generated method stub
		root.render(meshCollection, cubeLines, edgePoint, normal);
	}
	
}
