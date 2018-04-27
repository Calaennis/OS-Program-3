import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/*
 * Authors:
 * Cal Mezzell
 * Shane McDermott
 * Jason Jacobs
 */

public class MemoryManager {

	private Memory memory;
	private List<DeferredAllocation> deferred;
	private Map<Integer, Block> allocations;
	
	
	public MemoryManager (long size, long minSize) {
		deferred = new LinkedList<>();
		allocations = new HashMap<>();
		memory = new Memory(size, minSize);
	}
	
	public boolean allocate (DeferredAllocation allocation) {
		Block block = memory.allocate(allocation.getSize());
		if (block != null) {
			allocations.put(allocation.getId(), block);
			System.out.printf("\tDeferred request %d allocated; addr = 0x%08X.\n", allocation.getId(), block.getAddress());
			return true;
		}
		return false;
	}
	
	public void allocate (int id, long size) {
		System.out.printf("Request ID %d: allocate %d bytes.\n", id, size);
		
		Block block = memory.allocate(size);
		if (block == null) {
			deferred.add(new DeferredAllocation(id, size));
			System.out.println("\tRequest deferred.");
		}
		else {
			allocations.put(id, block);
			System.out.printf("\tSuccess; addr = 0x%08X.\n", block.getAddress());
		}
	}
	
	public void deallocate (int id) {
		System.out.printf("Request ID %d: deallocate\n", id);
		
		memory.deallocate(allocations.remove(id));
		System.out.println("\tSuccess.");
		
		Iterator<DeferredAllocation> it = deferred.iterator();
		while (it.hasNext()) {
			DeferredAllocation allocation = it.next();
			if (allocate(allocation)) {
				it.remove();
			}
		}
	}
}
