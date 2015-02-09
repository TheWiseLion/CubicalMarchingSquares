package VoxelSystem.VoxelMaterials;

import java.util.HashMap;
import java.util.Map;

import com.jme3.material.Material;

/**
 * Correlates internal type storage values to
 * user defined materials
 *
 */
public class MaterialRegistry {
	Map<VoxelMaterial, Integer> materialsToIndex;
	Map<Integer, VoxelMaterial> materials;
	
	public Material tangents;
	public Material normals;
	public Material bitangent;
//	public Material debug;
	
	
	public MaterialRegistry(){
		materialsToIndex = new HashMap<VoxelMaterial, Integer>();
		materials = new HashMap<Integer,VoxelMaterial>();
	}
	
	public int registerMaterial(VoxelMaterial vm){
		Integer i = materialsToIndex.get(vm);
		if(i == null){
			i = materials.size();
			vm.typeID = i;
			materials.put(i, vm);
			materialsToIndex.put(vm,i);
		}
		return i;
	}
	
	public VoxelMaterial getMaterial(int type){
		return materials.get(type);
	}
	
	
	
}
