package selim.selim_enchants;

import java.util.List;
import java.util.PriorityQueue;

import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SelimEnchants.MOD_ID)
public class TickScheduler {

	private static int nextId = 0;
	private static final PriorityQueue<Task> TASKS = new PriorityQueue<>();

	public static int scheduleTask(Level world, int delay, Runnable runnable) {
		Task task = new Task(world, world.getGameTime() + delay, runnable);
		TASKS.add(task);
		return task.id;
	}

	public static void cancelTasks(List<Integer> taskIds) {
		for (Integer i : taskIds) {
			if (TASKS.isEmpty())
				break;
			cancelTask(i);
		}
	}

	public static boolean cancelTask(int taskId) {
		for (Task t : TASKS)
			if (t.id == taskId) {
				TASKS.remove(t);
				return true;
			}
		return false;
	}

	@SubscribeEvent
	public static void worldTick(TickEvent.LevelTickEvent event) {
		Task task = TASKS.peek();
		if (task != null && event.level.equals(task.level) && event.level.getGameTime() >= task.targetedTick) {
			task.runnable.run();
//			try {
			TASKS.poll();
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
	}

	private static final class Task implements Comparable<Task> {

		public final int id;
		public final Level level;
		public final long targetedTick;
		public final Runnable runnable;

		public Task(Level level, long targetedTick, Runnable runnable) {
			this.id = nextId++;
			this.level = level;
			this.targetedTick = targetedTick;
			this.runnable = runnable;
		}

		@Override
		public int compareTo(Task arg0) {
			return Long.compare(targetedTick, arg0.targetedTick);
		}

	}

}
