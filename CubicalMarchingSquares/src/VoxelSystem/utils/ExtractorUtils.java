package VoxelSystem.utils;

import VoxelSystem.Data.EditableVoxelCube;
import VoxelSystem.Data.VoxelCube;
import VoxelSystem.Data.Storage.OctreeNode;

import com.jme3.math.Vector3f;

/***
 * Static utility class for calculating a cubes isopoint
 *
 * TODO: implement to use no extra memory. Currently waste's 100's of bytes
 */
public class ExtractorUtils {
	private static final int MAX_ITERATIONS = 100;
	private static float forceRatio = 0.05f;//as per Schmitz's specs
	
	/**
	 * Computes the vertex for the cube, from Hermite data. Uses Leonardo
	 * Augusto Schmitz's excellent method, with exact normal at intersection
	 * points, to reduce complexity.
	 * 
	 * @param threshold
	 *            Force threshold. usually a factor (1/1000) of the box diagonal.
	 * 
	 * @return geometric error of isopoint. returns -1 when there is no isopoint for cube
	 * 
	 */
	public static float surfaceContour(VoxelCube vc, float threshold, Vector3f output) {
		threshold *= threshold;
		
		int edgeInfo = VoxelSystemTables.getEdgeFromMaterials(vc);
		if(edgeInfo == 0){
			return -1; // contains no intersections
		}
		
		Vector3f cubeCorner = vc.getCorner();
		float cubeLen = vc.getCubeLength();
		
		Vector3f tmp = new Vector3f();
		Vector3f intersectionTMP = new Vector3f();
		Vector3f normalTMP = new Vector3f();
		int numIntersections = Integer.bitCount(edgeInfo);
		
		//TODO: Constant memory if its not too much trouble...
		Vector3f forces[] = new Vector3f[]{
				new Vector3f(),new Vector3f(),new Vector3f(),new Vector3f(),
				new Vector3f(),new Vector3f(),new Vector3f(),new Vector3f()
		};
		
		// Initial position is mid point between intersections:
		output.set(0,0,0);
		for (int i = 0; i < 12; i++){ // 12 edges
			if ((edgeInfo & (1 << i)) == 0){
				continue;
			}
			
			intersectionTMP = vc.getIntersectionPoint(i, intersectionTMP);
			Vector3f normal = vc.getNormal(i,normalTMP);
			
			output.addLocal(intersectionTMP);
			//Only need to compute forces once per corner
			//Compute the force at each cube corner
			for(int z = 0; z < forces.length; z++){
				forces[z].addLocal(planeToPoint(intersectionTMP,normal, vc.getCorner(z, tmp)));
			}
			
		}
		output.divideLocal(numIntersections);
		
	
		int iteration;
		Vector3f force = new Vector3f(0,0,0);
		
		for (iteration = 0; iteration < MAX_ITERATIONS; iteration++) {
			force.set(0, 0, 0);

			//Compute force by tri-linearly interpolating from centroid (mass point)
			force.set(trilinear(output, forces, cubeCorner, cubeLen));
			
			//Now scale force by 5%
			force.multLocal(forceRatio);
			
			//Add force to centroid to obtain new centroid 
			output.addLocal(force);
			
			//do i return here? if needs to be clamped?
			
			//Check if force is within threshold
			if(force.lengthSquared() < threshold){
				break;
			}
			
		}
		

		//Clamps to box if outside cube
		clampToBox(output, cubeCorner,cubeLen);
		
		
		//Error as described by Schaefer
		//sum(n_i*(x-p_i ))^2 
		float error = 0;
		
		for (int i = 0; i < 12; i++){ // 12 edges
			if ((edgeInfo & (1 << i)) == 0){
				continue;
			}
			
			intersectionTMP = vc.getIntersectionPoint(i, intersectionTMP);
			Vector3f normal = vc.getNormal(i,normalTMP);
			
			float e = intersectionTMP.subtractLocal(output).dot(normal);
			error+= e*e;
		}
		
		return error;
	}
	
	/***
	 * Obtain the trilinear interpolation of the force at point P.
	 * p - the point to find the force for.
	 * forces - the forces at the cube corners
	 * corner - the position of the cube corers
	 */
	private static Vector3f trilinear(Vector3f p, Vector3f [] forces, Vector3f corner, float len){
		Vector3f c000 = forces[0];
		Vector3f c100 = forces[1];
		Vector3f c101 = forces[5];
		Vector3f c001 = forces[4];
		
		Vector3f c010 = forces[3];
		Vector3f c110 = forces[2];
		Vector3f c111 = forces[6];
		Vector3f c011 = forces[7];

		float xPart = (p.x - corner.x)/ (len);//dx
		Vector3f c00 = c000.mult(1f-xPart).add(c100.mult(xPart));//c000*(1-xPart)+c100*(xPart)
		Vector3f c01 = c001.mult(1f-xPart).add(c101.mult(xPart));//c001*(1-xPart)+c101*(xPart)

		Vector3f c10 = c010.mult(1f-xPart).add(c110.mult(xPart));//c010*(1-xPart)+c110*(xPart)
		Vector3f c11 = c011.mult(1f-xPart).add(c111.mult(xPart));;//c011*(1-xPart)+c111*(xPart)

		float yPart = (p.y - corner.y)/(len);//dy
		Vector3f c0 = c00.mult(1f-yPart).add(c10.mult(yPart));//c00*(1-yPart)+c10*(yPart)
		Vector3f c1 = c01.mult(1f-yPart).add(c11.mult(yPart));//c01*(1-yPart)+c11*(yPart)

		
		float zPart = (p.z - corner.z)/(len);//dz
		Vector3f c = c0.mult(1-zPart).add(c1.mult(zPart));

		return c;
	}
	
	
	/**
	 * @param P - point on plane
	 * @param n - normal of plane
	 * @param Q - point not on plane
	 * @return vector from point Q to plane
	 */
	private static Vector3f planeToPoint(Vector3f P, Vector3f n, Vector3f Q){
		Vector3f PQ = Q.subtract(P);//point on plane to Q
		Vector3f proj = PQ.subtract(n.mult(PQ.dot(n)));//w
		Vector3f D = PQ.negate().add(proj);
		return D;
	}
	
	/***
	 * Clamps point to bounding box 
	 * @param p - point to be clamped
	 * @param bb - bounding box
	 * @return
	 */
	private static Vector3f clampToBox(Vector3f p, Vector3f min, float len){
		if(p.x < min.x){
			p.x = min.x;
		}else if(p.x > min.x + len){
			p.x = min.x + len;
		}
		
		if(p.y < min.y){
			p.y = min.y;
		}else if(p.y > min.y + len){
			p.y = min.y + len;
		}
		
		
		if(p.z < min.z){
			p.z = min.z;
		}else if(p.z > min.z + len){
			p.z = min.z + len;
		}
		
		return p;
	}
	
	public static int getNearestType(Vector3f p, Vector3f[] cube, int materials[]){
		float min = Float.MAX_VALUE;
		int mat = -1;
		
		for(int i=0; i < cube.length; i++){
			if(materials[i] == -1){
				continue;
			}
			
			float d = cube[i].subtract(p).lengthSquared();
			if(d < min){
				min = d;
				mat = materials[i];
			}
		}
		
		return mat;
	}

	/***
	 * Checks the cases laid out in
	 * http://www.cs.wustl.edu/~taoju/research/dualContour.pdf
	 * section 4.1
	 * 
	 * Currently 80 comparisons must be made for each collapse.
	 * Checks if its topographically safe to collapse a node
	 * @param children
	 * @param cube
	 * @return
	 */
	public static boolean checkSchaeferTopology(OctreeNode[] children, EditableVoxelCube cube){
		//TODO: Check topology of leaves. Supposedly there is a table out there somewhere
		//of ambiguous cube cases......
				
		for(OctreeNode child : children){
			if(!child.topographicallySafe()){
				return false;
			}
		}
		
		//For each edge:
			//middle type must show at one of the corners
		for(int i=0; i<12; i++){
			int c0 = VoxelSystemTables.iTable[i*2];
			int c1 = VoxelSystemTables.iTable[i*2+1];
			
			c0 = cube.getType(c0);
			c1 = cube.getType(c1);
			
			
			int m = getMiddleType(i, children);
			if(c0!= m && m!=c1){
				return false;
			}
		}
		
		//For each face(6):
			//Check type in middle of face shows in corners of face
		//face 1 Z-
		int f = children[0].getType(2);
		if(f != cube.getType(0) && f != cube.getType(1) && f != cube.getType(2) && f != cube.getType(3)){
			return false;
		}
		
		//face 2 X-
		f = children[0].getType(7);
		if(f != cube.getType(0) && f != cube.getType(3) && f != cube.getType(7) && f != cube.getType(4)){
			return false;
		}
		
		
		//face 3 Z+
		f = children[4].getType(6);
		if(f != cube.getType(4) && f != cube.getType(7) && f != cube.getType(6) && f != cube.getType(5)){
			return false;
		}
		
		
		//face 4 X+
		f = children[5].getType(2);
		if(f != cube.getType(1) && f != cube.getType(2) && f != cube.getType(6) && f != cube.getType(5)){
			return false;
		}
		
		//face 5 Y+
		f = children[2].getType(6);
		if(f != cube.getType(3) && f != cube.getType(2) && f != cube.getType(6) && f != cube.getType(7)){
			return false;
		}
		
		//face 6 Y-
		f = children[0].getType(5);
		if(f != cube.getType(0) && f != cube.getType(1) && f != cube.getType(4) && f != cube.getType(5)){
			return false;
		}
		//End Face Check
		
		//Center type must show at 1 of the corners of cube
		int m = children[0].getType(6);
		for(int i =0; i < 8; i++){
			if(cube.getType(i) == m){
				return true;
			}
		}
		
		//:D easy peezy 
		return false;
	}
	
	
	/***
	 * Very likely to change. 
	 * @param edge
	 * @param children
	 * @return
	 */
	public static int getMiddleType(int edge, OctreeNode[]children){
		switch(edge){
			case 0:
				return children[1].getType(0);
			case 1:
				return children[3].getType(1);
			case 2:
				return children[3].getType(3);
			case 3:
				return children[2].getType(0);
			case 4:
				return children[5].getType(4);
			case 5:
				return children[5].getType(6);
			case 6:
				return children[6].getType(6);
			case 7:
				return children[4].getType(7);
			case 8:
				return children[4].getType(0);
			case 9:
				return children[1].getType(5);
			case 10:
				return children[3].getType(6);
			case 11:
				return children[2].getType(7);
			default:
				throw new RuntimeException("Not a valid edge");
		}
	}
}
