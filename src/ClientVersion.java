public class ClientVersion {
	public static int sniffFileVersion = 0;
	
	public static final int V6_0_2_19033 = 19033;
	public static final int V6_0_2_19034 = 19034;

	public static final int V6_0_3_19103 = 19103;
	public static final int V6_0_3_19116 = 19116;
	public static final int V6_0_3_19243 = 19243;
	public static final int V6_0_3_19342 = 19342;

	public static final int V6_1_0_19678 = 19678;
	public static final int V6_1_0_19702 = 19702;

	public static final int V6_1_2_19802 = 19802;
	public static final int V6_1_2_19831 = 19831;
	public static final int V6_1_2_19865 = 19865;

	public static final int V6_2_0_20173 = 20173;
	public static final int V6_2_0_20182 = 20182;
	public static final int V6_2_0_20201 = 20201;
	public static final int V6_2_0_20216 = 20216;
	public static final int V6_2_0_20253 = 20253;
	public static final int V6_2_0_20338 = 20338;
	
	/**
	 * Check if added in version
	 * @param Integer version
	 * @return Boolean true if added
	 */
	public static Boolean addedInVersion(int version) {
		return sniffFileVersion >= version;
	}
	
	/**
	 * Check if removed in version
	 * @param version
	 * @return Boolean true if removed
	 */
	public static Boolean removedInVersion(int version) {
		return sniffFileVersion < version;
	}
}
