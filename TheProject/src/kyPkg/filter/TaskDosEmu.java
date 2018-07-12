package kyPkg.filter;

import java.util.ArrayList;
import java.util.List;

import kyPkg.task.Abs_ProgressTask;
import kyPkg.uFile.DosEmu;

public class TaskDosEmu extends Abs_ProgressTask {
	private List<List<String>> comands;

	// -------------------------------------------------------------------------
	// 2012-02-09 yuasa （コピーして雛形として使用する）
	// -------------------------------------------------------------------------
	// コンストラクタ
	// -------------------------------------------------------------------------
	public TaskDosEmu() {
		super();
		this.comands = new ArrayList();
	}

	// XXX ロボコピーのような機能がほしいかな

	public void addCommand(List<String> list) {
		for (String str : list) {
			addCommand(str);
		}
	}

	public void addCommand(String str) {
		List commandList = cnvToCommandList(str);
		if (commandList != null)
			comands.add(commandList);
	}

	// -------------------------------------------------------------------------
	// スペースで区切っても問題が起きないケースのみを想定
	// -------------------------------------------------------------------------
	public static List cnvToCommandList(String pSrc) {
		List<String> commandList = new ArrayList();
		String[] array = pSrc.split(" ");
		String commandStr = array[0].toUpperCase();
		if (commandStr.startsWith("DEL") && array.length >= 2) {
			commandList.add("del");
			commandList.add(array[1]);
		} else if (commandStr.startsWith("COPY") && array.length >= 3) {
			commandList.add("copy");
			commandList.add(array[1]);
			commandList.add(array[2]);
		} else if (commandStr.startsWith("MOVE") && array.length >= 3) {
			commandList.add("move");
			commandList.add(array[1]);
			commandList.add(array[2]);
		} else {
			System.out.println("Nonsense =>" + pSrc);
			return null;
		}
		return commandList;
	}

	public void del(List<String> list) {
		for (String str : list) {
			del(str);
		}
	}
	public void del(String path) {
		List<String> commandList = new ArrayList();
		commandList.add("del");
		commandList.add(path);
		this.comands.add(commandList);
	}

	public void copy(String src, String dst) {
		List<String> commandList = new ArrayList();
		commandList.add("copy");
		commandList.add(src);
		commandList.add(dst);
		this.comands.add(commandList);
	}

	public void move(String src, String dst) {
		List<String> commandList = new ArrayList();
		commandList.add("move");
		commandList.add(src);
		commandList.add(dst);
		this.comands.add(commandList);
	}
//	rename => move?

	// -------------------------------------------------------------------------
	// 実行部分
	// -------------------------------------------------------------------------
	@Override
	public void execute() {
		super.start("TaskDosEmu",2048);//"## AFTERMATH ##"
		for (List<String> commandList : comands) {
			String commandStr = commandList.get(0);
			if (commandStr.equals("del")) {
				DosEmu.del(commandList.get(1));
			} else if (commandStr.equals("copy")) {
				String src = commandList.get(1);
				String dst = commandList.get(2);
				DosEmu.copy(src, dst);
			} else if (commandStr.equals("move")) {
				String src = commandList.get(1);
				String dst = commandList.get(2);
				DosEmu.move(src, dst);
			}
		}
		super.stop();// 正常終了
 
	}

	public static void main(String[] args) {
		tester();
	}

	// -------------------------------------------------------------------------
	// 使用例＞
	// -------------------------------------------------------------------------
	public static void tester() {
		TaskDosEmu task = new TaskDosEmu();
		task.copy("c:/test/*.*", "c:/test/chile/");
		task.move("c:/test/*.*", "c:/test/moveit/");
		task.copy("c:/test/moveit/*.*","c:/test/");
		
		task.del("c:/test/moveit/*.sql");
		task.del("c:/test/moveit/*.pre");
		task.del("c:/test/moveit/*.trn");
		task.del("c:/test/moveit/*.mon");
		
		// task.move("","");
		// task.del("");
		task.execute();
	}

}
