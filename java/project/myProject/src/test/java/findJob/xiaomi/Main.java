package findJob.xiaomi;

import java.util.*;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 19:33 2018/9/20
 * @Modified By:
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str;
        List<String> list = new ArrayList<>();
        SolutionXiao1 so = new SolutionXiao1();
        while (sc.hasNext()) {
            str = sc.nextLine();
            if(str.equals("END")) break;
            list.add(str);
        }
        so.getList(list);

    }
}

class SolutionXiao1{

    Map<Integer,String> map = new LinkedHashMap<>();

    public void getList(List<String> list) {
        boolean flag = true;
        for (int i = 0; i < list.size(); i++) {
            solution(list.get(i));
        }

        for (String str : map.values()) {
            if(!str.equals("flag")) {
                System.out.println(str);
                flag = false;
            }
        }

        if(flag) System.out.println("None");

    }
    public void solution(String str) {
        String[] array = str.split("#");
        doSolution(Integer.valueOf(array[0]),array[1],str);
    }

    private void doSolution(Integer jin, String sub,String str) {
        int value = 0;
        int len = sub.length();
        int num = 0;
        for (int i = 0; i < len; i++) {
            char temp = sub.charAt(i);
            if(temp>=65&&temp<=70) {
                num = temp -'A' + 10;
            } else if(temp>=97&&temp<=102) {
                num = temp - 'a' + 10;
            } else {
                num = temp - '0';
            }
            value += num * Math.pow(jin,len-1-i);
        }

        if(map.containsKey(value)) {
            map.put(value,"flag");
        } else {
            map.put(value,str);
        }
    }
}