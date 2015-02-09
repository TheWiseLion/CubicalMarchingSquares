package VoxelSystem.Data.DensityVolumes.Shapes;

import VoxelSystem.utils.SimplexNoise;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector3f;

public class NoiseShape extends VolumeShape{
	int dimX = 10000;
	int dimY = 500;
	int dimZ = 10000;
	float step;
	public NoiseShape(float res){
		this.step = res;
	}
	
	
	@Override
	public int getType(float x, float y, float z){
		if(!isOutside(x, y, z)){
//			if( x > 5){
//				return 3;
//			}else{
				return 0;
//			}
		}else{
			return -1;
		}
	}
	
	@Override
	public float getDensity(float x, float y, float z) {
		if(Math.abs(x)>dimX || Math.abs(y) > dimY || Math.abs(z)>dimZ){
//			if(y<0){
//				return 1;
//			}
			return -1;
		}
//		float r  =  10f;
//		float rInnter = r/2.0f;
//		float len = (float) Math.sqrt(x*x + y*y );
//		if(len > r){
//			return -1;
//		}else if(len < rInnter){
//			return -1;
//		}else{
//			return 0;
//		}
		if(y <= 2f){
			return 1;
		}
		
		int octaves = 1;
		
		float amplitude = 200;//1.70f;
		
		
		float baseFreq = 500f;
		float FreqMultiplier = 10f;
		float d = 0;
		//"Mountain"  Low Frequency high octave
		for(int i = 0; i< octaves;i++){
			float localFreq = baseFreq + FreqMultiplier*i;
			d += SimplexNoise.noise(x/localFreq,z/localFreq,y/localFreq) * amplitude * (i+1);
		}
		
//		float localFreq = 100f;
//		amplitude = 25f;
//		d += SimplexNoise.noise(x/localFreq,z/localFreq) * amplitude ;
//		d -= FastMath.abs(x)/10;
//		d -= FastMath.abs(z)/10;
//		System.out.println(d-y);
		return d-y;
		
		
		//"Cave Octave"
		
//		octaves = 4;
//		base = 1.5f;
//		multiplier = .5f;
//		baseFreq = 3f;
//		FreqMultiplier = 2f;
//		
//		for(int i = 0; i< octaves;i++){
//			float localFreq = baseFreq + baseFreq*FreqMultiplier*i;
//			d -= SimplexNoise.noise(x/localFreq,z/localFreq,y/localFreq) * (base+multiplier*i);
//		}

		//"Localized roughness":
//		octaves = 4;
//		base = .5f;
//		multiplier = .5f;
//		baseFreq = 1f;
//		FreqMultiplier = .25f;
//		
//		for(int i = 0; i< octaves;i++){
//			float localFreq = baseFreq + baseFreq*FreqMultiplier*i;
//			d += SimplexNoise.noise(x/localFreq,z/localFreq,y/localFreq) * (base+multiplier*i);
//		}
		
		
//		if(d < 0){
//			return -1;
//		}else 
	
//		float yIso = (float) (5*Math.sin(x/5) + 5*Math.sin(z/5));
//		
//		return (float) (yIso-y);
	}



	@Override
	public BoundingBox getEffectiveVolume() {
		// TODO Auto-generated method stub
		return new BoundingBox(new Vector3f(-dimX,0,-dimZ),new Vector3f(dimX,dimY,dimZ));
	}

	@Override
	public boolean isOutside(float x, float y, float z) {
		return getDensity(x,y,z) <= 0;
	}


	@Override
	public Vector3f getSurfaceNormal(float x, float y, float z, Vector3f out) {
		double nx = getDensity(x + step, y, z) - getDensity(x - step, y, z);
	    double ny = getDensity(x, y + step, z) - getDensity(x, y - step, z);
	    double nz = getDensity(x, y, z + step) - getDensity(x, y, z - step);
	 
	    out.set((float)-nx, (float)-ny, (float)-nz);
	    out.normalizeLocal();
		return out;
	}

}
