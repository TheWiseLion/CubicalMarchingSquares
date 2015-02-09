import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;


public class SharpFeatureTest2D {
	public static void main(String ... args){
		//From Figure 4 of CMS paper
		//Testing our case analysis.
		Vector2f v[] = new Vector2f[]{
				new Vector2f(0,0),
				new Vector2f(0,1),
				new Vector2f(1,1),
				new Vector2f(1,0)
		};
		
		Vector2f n[] = new Vector2f[]{
				new Vector2f(-.035f,1).normalize(),
				new Vector2f(-.95f,.2f).normalize(),
				new Vector2f(.035f,-1).normalize(),
				new Vector2f(.95f,-.05f).normalize()
		};
		
//		Vector2f n[] = new Vector2f[]{
//				new Vector2f(.5f,.5f).normalize(),
//				new Vector2f(.5f,.5f).normalize(),
//				new Vector2f(-.5f,-.5f).normalize(),
//				new Vector2f(-.5f,-.5f).normalize()
//		};
		
		Vector2f I[] = new Vector2f[]{
				new Vector2f(0,.75f),
				new Vector2f(.35f,1f),
				new Vector2f(1f,.35f),
				new Vector2f(.75f,0f)
		};
		BoundingBox box=new BoundingBox();
		box.setCenter(new Vector3f(.5f,.5f,0));
		box.setXExtent(.5f);//(new Vector3f(.5f,.5f,0));
		box.setYExtent(.5f);//(new Vector3f(.5f,.5f,0));
		
		//Case 3.1
		Vector2f s1 = surfaceContour(v, new Vector2f[]{I[0],I[3]}, new Vector2f[]{n[0],n[3]}, box, .0000001f);
		Vector2f s2 = surfaceContour(v, new Vector2f[]{I[1],I[2]}, new Vector2f[]{n[1],n[2]}, box, .0000001f);
		//Case 3.2
		Vector2f s3 = surfaceContour(v, new Vector2f[]{I[0],I[1]}, new Vector2f[]{n[0],n[1]}, box, .0000001f);
		Vector2f s4 = surfaceContour(v, new Vector2f[]{I[2],I[3]}, new Vector2f[]{n[2],n[3]}, box, .0000001f);
		
		System.out.println(s1+" :: "+s2); //Matches image (a). Looks good to me.
		System.out.println(s3+" :: "+s4); //Matches image (a). Looks good to me.
		
		//Now. Determine which case to use.....
		//Weak case. Calculate reletive angles... just compare cos
			//case 1 dot product:
			float theta01 = (float) ((float) (180/Math.PI)*Math.acos(n[0].dot(n[3])/(n[0].length() * n[3].length())));
			float theta02 =  (float) ((float) (180/Math.PI)*Math.acos(n[1].dot(n[2])/(n[1].length() * n[2].length())));
			//case 2 dot product
			float theta11 = (float) ((float)(180/Math.PI)* Math.acos(n[0].dot(n[1])/(n[0].length() *n[1].length())));
			float theta12 =  (float) ((float)(180/Math.PI)* Math.acos(n[2].dot(n[3])/(n[2].length() *n[3].length())));
			
			System.out.println(n[0].dot(n[3])+" :: "+n[1].dot(n[2])+"  Degrees: "+theta01+"::"+theta02); //Matches image (a). Looks good to me.
			System.out.println(n[0].dot(n[1])+" :: "+n[2].dot(n[3])+"  Degrees: "+theta11+"::"+theta12); //Matches image (a). Looks good to me.
		//Strong case compare? Maybe only use if indeterminate? Could average angles? Coherence?
			//Check if line segments intersect.....
			
		//Consider using dot product between the two. The one with the lowest angle 
		//is the most correct.  
		
		//Sharp feature should be considered after this.
		
	}
	
	
	
	
	
	private static final int MAX_ITERATIONS = 1000;
	private static float forceRatio = 0.05f;
	
	/**
	 * Computes the vertex for the cube, from Hermite data. Uses Leonardo
	 * Augusto Schmitz's excellent method, with exact normal at intersection
	 * points, to reduce complexity.
	 */
	public static Vector2f surfaceContour(Vector2f [] cubeEdges, Vector2f [] intersections, Vector2f [] normals, BoundingBox bb, float threshold) {
		threshold *= threshold;
	
		// Initial position is mid point between intersections:
		Vector2f masspoint = new Vector2f();
		for (Vector2f v: intersections) {
			masspoint.addLocal(v);
		}
		masspoint.divideLocal(intersections.length);
		Vector2f particlePosition = new Vector2f(masspoint);
		
		Vector2f forces[] = new Vector2f[4];
		
		//Only need to compute forces once per intersection
		//Compute the force at each cube corner
		for(int i = 0; i < forces.length; i++){
			Vector2f force = new Vector2f(0,0);
			//Sum of the forces between cube corner and distance to each intersection plane
			for(int z = 0; z < intersections.length; z++){
				force.addLocal(planeToPoint(intersections[z],normals[z],cubeEdges[i]));
			}
			forces[i] = force;
		}
		
		int iteration;
		Vector2f force = new Vector2f(0,0);
		
		for (iteration = 0; iteration < MAX_ITERATIONS; iteration++) {
			force.set(0, 0);

			//Compute force by tri-linearly interpolating from centroid (mass point)
			force.set(bilinear(particlePosition,forces,cubeEdges));
			
			//Now scale force by 5%
			force.multLocal(forceRatio);
			
			//Add force to centroid to obtain new centroid 
			particlePosition.addLocal(force);
			
			//do i return here? if needs to be clamped?
			
			//Check if force is within threshold
			if(force.lengthSquared() < threshold){
				break;
			}
			
		}
		
		//TODO: duh. add clamping
		//Check if centroid needs to be clamped
//		if(!bb.contains(particlePosition)){
//			particlePosition = clampToBox(particlePosition, bb);
//		}
		
		return particlePosition;
	}
	
	/***
	 * Obtain the trilinear interpolation of the force at point P.
	 * p - the point to find the force for.
	 * forces - the forces at the cube corners
	 * corner - the position of the cube corers
	 */
	private static Vector2f bilinear(Vector2f p, Vector2f [] forces, Vector2f[] corners){
		Vector2f Q11 = forces[0];
		Vector2f Q12 = forces[1];
		Vector2f Q22 = forces[2];
		Vector2f Q21 = forces[3];
		float x1 = corners[0].x;
		float x2 = corners[3].x;
		
		float y1 = corners[0].y;
		float y2 = corners[1].y;
		
		//P = ((y2 – y)/(y2 – y1))*R1 + ((y – y1)/(y2 – y1))*R2
		float len = (x2-x1);
		Vector2f R1 = Q11.mult((x2-p.x)/len).add(Q21.mult((p.x-x1)/len));
		Vector2f R2 = Q12.mult((x2-p.x)/len).add(Q22.mult((p.x-x1)/len));
		Vector2f P = R1.mult( (y2-p.y)/len ).add( R2.mult( (p.y-y1)/len ) );
		return P;
	}
	
	
	/**
	 * @param P - point on plane
	 * @param n - normal of plane
	 * @param Q - point not on plane
	 * @return vector from point Q to plane
	 */
	private static Vector2f planeToPoint(Vector2f P, Vector2f n, Vector2f Q){
		Vector2f PQ = Q.subtract(P);//point on plane to Q
		Vector2f proj = PQ.subtract(n.mult(PQ.dot(n)));//w
		Vector2f D = PQ.negate().add(proj);
		return D;
	}
	
	/***
	 * Clamps point to bounding box 
	 * @param p - point to be clamped
	 * @param bb - bounding box
	 * @return
	 */
	private static Vector3f clampToBox(Vector3f p, BoundingBox bb){
		Vector3f min = bb.getMin(new Vector3f());
		Vector3f max = bb.getMax(new Vector3f());
		if(p.x < min.x){
			p.x = min.x;
		}else if(p.x > max.x){
			p.x = max.x;
		}
		
		if(p.y < min.y){
			p.y = min.y;
		}else if(p.y > max.y){
			p.y = max.y;
		}
		
		
		if(p.z < min.z){
			p.z = min.z;
		}else if(p.z > max.z){
			p.z = max.z;
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
}
