public enum Opcode {
	SMSG_ON_MONSTER_MOVE (0x0EA9);

    private final int id;
    
    Opcode(int id) { 
    	this.id = id;
    }
    
    public int getValue() { 
    	return id; 
    }
}
