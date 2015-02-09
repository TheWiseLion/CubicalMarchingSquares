package VoxelSystem.utils;

public class Utils {
	public static float roundNearestPow2(float x){
		float c = (float) Math.pow(2,Math.ceil(Math.log(x)/Math.log(2)));
		float f = (float) Math.pow(2,Math.floor(Math.log(x)/Math.log(2)));
		float r = Math.abs(f-x) < Math.abs(c-x) ? f : c;
		return r;
	}
	
	
	
}
