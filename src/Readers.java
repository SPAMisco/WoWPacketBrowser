import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Readers {
	private static byte _bitPos = 0;
	private static byte _curBitVal = 0;
	
	/**
	 * Read next byte
	 * @param ByteArrayInputStream buff
	 * @return byte value
	 */
    public static byte readByte(ByteArrayInputStream buff) {
    	return (byte) buff.read();
    }
	
    /**
     * Read next unsigned 16bit integer
     * @param ByteArrayInputStream buff
     * @return short value
     */
    public static short readUInt16(ByteArrayInputStream buff) {
		byte[] b = new byte[2];
		buff.read(b, 0, 2);
    	return ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }
    
    /**
     * Read next unsigned 32bit integer
     * @param ByteArrayInputStream buff
     * @return int value
     */
    public static int readUInt32(ByteArrayInputStream buff) {
		byte[] b = new byte[4];
		buff.read(b, 0, 4);
    	return ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }
    
    /**
     * Read next signed 32bit integer
     * @param ByteArrayInputStream buff
     * @return int value
     */
    public static int readInt32(ByteArrayInputStream buff) {
		byte[] b = new byte[4];
		buff.read(b, 0, 4);
    	return ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }
    
    /**
     * Read next float point value 32bit
     * @param ByteArrayInputStream buff
     * @return float value
     */
    public static float readFloat(ByteArrayInputStream buff) {
		byte[] b = new byte[4];
		buff.read(b, 0, 4);
    	return ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getFloat();
    }
    
    /**
     * Read string
     * @param ByteArrayInputStream buff
     * @param integer len
     * @return String value
     */
    public static String readString(ByteArrayInputStream buff, int len) {
		byte[] b = new byte[len];
		buff.read(b, 0, len);
    	try {
			return new String(b, "ASCII");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    /**
     * Read 3D vector
     * @param ByteArrayInputStream buff
     * @return G3DVector vector
     */
    public static G3DVector readVector3(ByteArrayInputStream buff) {
    	return new G3DVector(Readers.readFloat(buff), Readers.readFloat(buff), Readers.readFloat(buff), 0);
    }
    
    /**
     * Read packed 3D vector
     * @param ByteArrayInputStream buff
     * @return G3DVector vector
     */
    public static G3DVector readPackedVector3(ByteArrayInputStream buff) {
        int packed = readInt32(buff);
        float x = ((packed & 0x7FF) << 21 >> 21)*0.25f;
        float y = ((((packed >> 11) & 0x7FF) << 21) >> 21)*0.25f;
        float z = ((packed >> 22 << 22) >> 22)*0.25f;
    	return new G3DVector(x, y, z, 0);
    }
    
    /**
     * Read packed guid
     * @param ByteArrayInputStream buff
     * @return ObjectGUID guid
     */
    public static ObjectGUID readPackedGUID(ByteArrayInputStream buff) {
        byte guidLowMask = (byte) buff.read();
        byte guidHighMask = (byte) buff.read();

        return new ObjectGUID(readPackedUInt64(buff, guidLowMask), readPackedUInt64(buff, guidHighMask));
    }
    
    /**
     * Read packed unsigned 64bit integer
     * @param ByteArrayInputStream buff
     * @param Byte mask
     * @return BigInteger value
     */
    public static BigInteger readPackedUInt64(ByteArrayInputStream buff, byte mask) {
        if (mask == 0)
            return new BigInteger("0");

        BigInteger res = new BigInteger("0");

        int i = 0;
        while (i < 8) {
            if ((mask & 1 << i) != 0) {
                res = res.add(BigInteger.valueOf((long)buff.read() << (i * 8)));
            }
            i++;
        }
        return res;
    }
    
    /**
     * Read single bit
     * @param ByteArrayInputStream buff
     * @return Boolean bit value
     */
    public static boolean ReadBit(ByteArrayInputStream buff) {
        ++_bitPos;

        if (_bitPos > 7)
        {
        	_bitPos = 0;
        	_curBitVal = (byte) buff.read();
        }

        return ((_curBitVal >> (7 - _bitPos)) & 1) != 0;
    }

    /**
     * Reset single bit reader pos
     */
    public static void ResetBitReader() {
        _bitPos = 8;
    }

    /**
     * Read number of bits
     * @param ByteArrayInputStream buff
     * @param Integer number of bits
     * @return
     */
    public static int ReadBits(ByteArrayInputStream buff, int bits) {
    	int value = 0;
        for (int i = bits - 1; i >= 0; --i)
            if (ReadBit(buff))
                value |= (int)(1 << i);

        return value;
    }
}
