import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/*
 * Authors:
 * Cal Mezzell
 * Shane McDermott
 * Jason Jacobs
 */

public class Memory {
	private long totalSize;
	private long minSize;
	private List<List<Block>> freeBlocks;
	/*
	 * freeBlocks goes from highest block size at 0 to lowest block size
	 * freeBlocks also just holds an address of the free block
	 */
	
	public Memory (long totalSize, long minSize) {
		this.totalSize = totalSize;
		this.minSize = minSize;
		
		freeBlocks = new ArrayList<>();
		
		for (long i = totalSize / minSize; i > 0; i /= 2L) {
			freeBlocks.add(new LinkedList<>());
		}
		
		freeBlocks.get(0).add(new Block(0L, totalSize));
	}
	
	public Block allocate (long size) {
		long newSize = minSize;
		int index = freeBlocks.size() - 1;
		
		while (newSize < size) {
			newSize *= 2;
			index -= 1;
		}
		
		int splitCount = 0;
		boolean free = false;
		
		while (index >= 0) {
			if (!freeBlocks.get(index).isEmpty()) {
				free = true;
				break;
			}
			
			index--;
			splitCount++;
		}
		
		if (free) {
			Block currentBlock = freeBlocks.get(index).remove(0);
			
			while (splitCount-- > 0) {
				Block freeBlock = new Block(currentBlock.getAddress() + currentBlock.getSize() / 2, currentBlock.getSize() / 2);
				currentBlock = new Block(currentBlock.getAddress(), currentBlock.getSize() / 2);

				freeBlocks.get(++index).add(freeBlock);
			}
			
			return currentBlock;
		}
		
		return null;
	}
	
	public void deallocate (Block block) {
		Block currentBlock = block;
		
		long searchSize = minSize;
		int index = freeBlocks.size() - 1;
		
		while (searchSize < currentBlock.getSize()) {
			searchSize *= 2;
			index -= 1;
		}
		
		while (true) {
			if (currentBlock.getSize() == totalSize) {
				freeBlocks.get(index).add(currentBlock);
				break;
			}
			
			long buddyAddress = findBuddyAddress(currentBlock.getSize(), currentBlock.getAddress());
			
			if (addressIsFree(buddyAddress, index)) {
				for (int i = 0; i < freeBlocks.get(index).size(); i++) {
					if (freeBlocks.get(index).get(i).getAddress() == buddyAddress) {
						freeBlocks.get(index).remove(i);
						break;
					}
				}
				
				long address = currentBlock.getAddress() < buddyAddress ? currentBlock.getAddress() : buddyAddress;
				Block newBlock = new Block(address, currentBlock.getSize() * 2);
				currentBlock = newBlock;
				index--;
			} 
			else {
				freeBlocks.get(index).add(currentBlock);
//				System.out.printf("Free block added in index %d of size %d", index, currentBlock.getSize());
				break;
			}
		}
	}
	
	private boolean addressIsFree (long address, int index) {
		boolean free = false;
		
		for (int i = 0; i < freeBlocks.get(index).size(); i++) {
			if (freeBlocks.get(index).get(i).getAddress() == address) {
				free = true;
				break;
			}
		}
		
		return free;
	}
	
	private long findBuddyAddress (long size, long address) {
		long buddyAddress = address + size;
		if ((address / size) % 2 == 1) {
			buddyAddress = address - size;
		}
		
		return buddyAddress;
	}
}
