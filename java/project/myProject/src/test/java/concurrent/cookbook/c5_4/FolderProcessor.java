package concurrent.cookbook.c5_4;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 11:16 2018/8/13
 * @Modified By:
 */
public class FolderProcessor extends RecursiveTask<List<String>> {
    private static final long serialVersionUID = 1L;
    private String path;
    private String extension;

    public FolderProcessor(String path, String extension) {
        this.path = path;
        this.extension = extension;
    }

    @Override
    protected List<String> compute() {
        List<String> list = new ArrayList<>();
        List<FolderProcessor> tasks = new ArrayList<>();

        File file = new File(path);
        File[] content = file.listFiles();

        if(content != null) {
            for (int i = 0; i < content.length; i++) {
                if(content[i].isDirectory()) {
                    FolderProcessor task = new FolderProcessor(content[i].getAbsolutePath(),extension);

                    task.fork();//异步
                    tasks.add(task);
                } else {
                    if(checkFile(content[i].getName())) {
                        list.add(content[i].getAbsolutePath());//这里直接插入当前线程的列表
                    }
                }
            }
        }

        addResultsFromTasks(list,tasks);

        return list;
    }

    private boolean checkFile(String name) {
        return name.endsWith(extension);
    }

    private void addResultsFromTasks(List<String> list, List<FolderProcessor> tasks) {
        for (FolderProcessor item : tasks) {
            //这里，要将所有字任务的返回的列表，全部加进父任务中！！
            list.addAll(item.join());//这里线程等待，就可以使用工作窃取算法了
        }
    }

}
