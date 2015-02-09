import VoxelSystem.VoxelMaterials.MaterialRegistry;
import VoxelSystem.VoxelMaterials.VoxelMaterial;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.MinFilter;
import com.jme3.texture.Texture.WrapMode;


public class Materials {
	public static MaterialRegistry getMaterials(AssetManager assetManager){
		//Load Textures:
    	Texture [] textures = new Texture[]{
    		assetManager.loadTexture("Textures/TextureArray/BlackRockD512.png"),
    		assetManager.loadTexture("Textures/TextureArray/BlackRockH512.png"),
    		assetManager.loadTexture("Textures/TextureArray/BlackRockN512.png"),
    		
    		assetManager.loadTexture("Textures/TextureArray/SandD512.png"),
    		assetManager.loadTexture("Textures/TextureArray/SandS512.png"),
    		assetManager.loadTexture("Textures/TextureArray/SandN512.png"),
    		
    		assetManager.loadTexture("Textures/TextureArray/lavaD512.png"),
    		assetManager.loadTexture("Textures/TextureArray/lavaS512.png"),
    		assetManager.loadTexture("Textures/TextureArray/lavaN512.png"),
    		
    		
    		assetManager.loadTexture("Textures/TextureArray/CrackedMudD512.png"),
    		assetManager.loadTexture("Textures/TextureArray/CrackedMudS512.png"),
    		assetManager.loadTexture("Textures/TextureArray/CrackedMudN512.png"),
    		
    		assetManager.loadTexture("Textures/TextureArray/StonePlatesD512.png"),
    		assetManager.loadTexture("Textures/TextureArray/StonePlatesS512.png"),
    		assetManager.loadTexture("Textures/TextureArray/StonePlatesN512.png"),
    		
    		
    		assetManager.loadTexture("Textures/TextureArray/BlackPebbelsD512.png"),
    		assetManager.loadTexture("Textures/TextureArray/BlackPebbelsS512.png"),
    		assetManager.loadTexture("Textures/TextureArray/BlackPebbelsN512.png")
    		
    		
    	};
    	
    	for(Texture t : textures){
    		t.setWrap(WrapMode.Repeat);
    		t.setMinFilter(MinFilter.Trilinear);
    		t.setAnisotropicFilter(16);
    	}
    	
    	//Initialize Materials:
    	Material rockMaterial = new Material(assetManager, "Shaders/VoxelMaterial.j3md"); 
		rockMaterial.setTexture("DiffuseMap", textures[0]);
		rockMaterial.setTexture("NormalMap", textures[2]);
		rockMaterial.setTexture("SpecularMap", textures[1]);
		
		
		
		
		Material sandMaterial = new Material(assetManager, "Shaders/VoxelMaterial.j3md"); 
		sandMaterial.setTexture("DiffuseMap", textures[3]);
		sandMaterial.setTexture("SpecularMap", textures[4]);
		sandMaterial.setTexture("NormalMap", textures[5]);
		
		
		Material lavaMaterial = new Material(assetManager, "Shaders/VoxelMaterial.j3md"); 
		lavaMaterial.setTexture("DiffuseMap", textures[6]);
		lavaMaterial.setTexture("SpecularMap", textures[7]);
		lavaMaterial.setTexture("NormalMap", textures[8]);
		lavaMaterial.setFloat("Shininess", 30);
		
		Material mudMaterial = new Material(assetManager, "Shaders/VoxelMaterial.j3md"); 
		mudMaterial.setTexture("DiffuseMap", textures[9]);
		mudMaterial.setTexture("SpecularMap", textures[10]);
		mudMaterial.setTexture("NormalMap", textures[11]);
		mudMaterial.setFloat("Shininess", 30);
		
		
		Material stonePlateMaterial = new Material(assetManager, "Shaders/VoxelMaterial.j3md"); 
		stonePlateMaterial.setTexture("DiffuseMap", textures[12]);
		stonePlateMaterial.setTexture("SpecularMap", textures[13]);
		stonePlateMaterial.setTexture("NormalMap", textures[14]);
		stonePlateMaterial.setFloat("Shininess", 30);
		
		
		Material blackPebbleMaterial = new Material(assetManager, "Shaders/VoxelMaterial.j3md"); 
		blackPebbleMaterial.setTexture("DiffuseMap", textures[15]);
		blackPebbleMaterial.setTexture("SpecularMap", textures[16]);
		blackPebbleMaterial.setTexture("NormalMap", textures[17]);
		blackPebbleMaterial.setFloat("Shininess", 30);
		
		Material greenMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		greenMat.setColor("Color", ColorRGBA.Green);
		Material redMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		redMat.setColor("Color", ColorRGBA.Red);
		Material blueMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		blueMat.setColor("Color", ColorRGBA.Blue);
		
		// Initialize Voxel Materials:
//		Map<Integer,VoxelMaterial> types = new HashMap<Integer,VoxelMaterial>();
		VoxelMaterial rock = new VoxelMaterial();
		rock.name = "rock";
		rock.jMEMaterial = rockMaterial;
		
		VoxelMaterial sand = new VoxelMaterial();
		sand.name = "sand";
		sand.jMEMaterial = sandMaterial;
		
		VoxelMaterial lava = new VoxelMaterial();
		lava.name = "lava";
		lava.jMEMaterial = lavaMaterial;
		
		VoxelMaterial mud = new VoxelMaterial();
		mud.name = "mud";
		mud.jMEMaterial = mudMaterial;
		
		VoxelMaterial plates = new VoxelMaterial();
		plates.name = "plates";
		plates.jMEMaterial = stonePlateMaterial;
		
		VoxelMaterial pebbles = new VoxelMaterial();
		pebbles.name = "pebbles";
		pebbles.jMEMaterial = blackPebbleMaterial;
		
		
		MaterialRegistry mr = new MaterialRegistry();
		mr.normals = greenMat;
		mr.tangents = redMat;
		mr.bitangent  = blueMat;
//		mr.registerMaterial(green);
		mr.registerMaterial(sand);
		mr.registerMaterial(rock);
		mr.registerMaterial(lava);
		
		mr.registerMaterial(mud);
		mr.registerMaterial(plates);
		mr.registerMaterial(pebbles);
		return mr;
	}
}
