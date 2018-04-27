
/*
 * Authors:
 * Cal Mezzell
 * Shane McDermott
 * Jason Jacobs
 */

public class Block {
	private long address;
	private long size;
	
	public Block (long address, long size) {
		this.address = address;
		this.size = size;
	}
	
	public long getAddress() {
		return address;
	}
	public void setAddress(long address) {
		this.address = address;
	}
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
}
