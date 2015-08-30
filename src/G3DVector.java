public class G3DVector {
	public float x;
	public float y;
	public float z;
	public float o;
	public int delay;
	public int type;

	/**
	 * Vector constructor
	 * @param Float x
	 * @param Float y
	 * @param Float z
	 * @param Float o
	 */
	public G3DVector(float x, float y, float z, float o) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.o = o;
		this.delay = 0;
	}

	/**
	 * Get distance between vectors
	 * @param G3DVector vec
	 * @return Double distance
	 */
	public double Distance(G3DVector vec) {
		return Math.sqrt(Math.pow(x - vec.x, 2) + Math.pow(y - vec.y, 2) + Math.pow(z - vec.z, 2));
	}
}