import java.util.HashMap;

public class Opcode {
	public static final int SMSG_ON_MONSTER_MOVE = 0;
	public static final int SMSG_UPDATE_OBJECT = 1;
	
	public static HashMap<Integer, Integer> opcodes = new HashMap<Integer, Integer>();
	
	/**
	 * Init opcode table
	 * @param Integer build
	 */
	public static void initOpcodeTables(int build) {
		switch(build) {
			case ClientVersion.V6_0_2_19033:
			case ClientVersion.V6_0_2_19034:
				opcodes.put(0x04A4, SMSG_ON_MONSTER_MOVE);
				opcodes.put(0x03EF, SMSG_UPDATE_OBJECT);
				break;
			case ClientVersion.V6_0_3_19103:
			case ClientVersion.V6_0_3_19116:
			case ClientVersion.V6_0_3_19243:
			case ClientVersion.V6_0_3_19342:
				opcodes.put(0x0994, SMSG_ON_MONSTER_MOVE);
				opcodes.put(0x122C, SMSG_UPDATE_OBJECT);
				break;
			case ClientVersion.V6_1_0_19678:
			case ClientVersion.V6_1_0_19702:
				opcodes.put(0x0B09, SMSG_ON_MONSTER_MOVE);
				opcodes.put(0x1762, SMSG_UPDATE_OBJECT);
				break;
			case ClientVersion.V6_1_2_19802:
			case ClientVersion.V6_1_2_19831:
			case ClientVersion.V6_1_2_19865:
				opcodes.put(0x0EA9, SMSG_ON_MONSTER_MOVE);
				opcodes.put(0x1CB2, SMSG_UPDATE_OBJECT);
				break;
			case ClientVersion.V6_2_0_20173:
			case ClientVersion.V6_2_0_20182:
			case ClientVersion.V6_2_0_20201:
			case ClientVersion.V6_2_0_20216:
			case ClientVersion.V6_2_0_20253:
			case ClientVersion.V6_2_0_20338:
				opcodes.put(0x0C28, SMSG_ON_MONSTER_MOVE);
				opcodes.put(0x0D36, SMSG_UPDATE_OBJECT);
				break;
			default:
				break;
		}
	}
	
	/**
	 * Get opcode
	 * @param opcode
	 * @return Integer opcode
	 */
	public static int getOpcode(final int opcode) {
		return opcodes.get(opcode);
	}
	
	/**
	 * Check if opcode exists
	 * @param Integer opcode
	 * @return Boolean true if opcode exists
	 */
	public static boolean existOpcode(final int opcode) {
		return opcodes.containsKey(opcode);
	}
}
