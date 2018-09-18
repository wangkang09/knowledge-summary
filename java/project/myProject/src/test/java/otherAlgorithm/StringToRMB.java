package otherAlgorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 15:27 2018/9/14
 * @Modified By:
 */
public class StringToRMB {
    public static void main(String[] args) {
        String re = new SolutionRMB().changeToRMB("10000001010");


        System.out.println(move0(re));
    }

    private static String move0(String re) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < re.length(); i++) {
            if(re.charAt(i)!='零') {
                sb.append(re.charAt(i));
            } else {
                char tem = re.charAt(i+1);
                if(tem=='零'|| tem == '万' || tem == '亿' || tem== '千' || tem == '百' || tem == '十' || tem == '元') continue;
                else sb.append(re.charAt(i));
            }
        }
        return sb.toString();
    }
}

class SolutionRMB {
    List<String> result = new ArrayList<>();
    String[] RmbNumber = {"","壹","贰","叁","肆","伍","陆","柒","捌","玖"};
    String[] RmbSuffix = {"","十","百","千"};
    String[] RmbSuffix1 = {"元","万","亿","万"};

    public String changeToRMB(String str) {

        int count = 0;
        //按四个一起分割
        while (str.length()>=5) {
            String temp = str.substring(str.length()-4,str.length());
            str = str.substring(0,str.length()-4);
            //每4个数为一组
            solution(temp,count++);
        }
        solution(str,count);
        StringBuilder sb = new StringBuilder();
        for (int i = result.size()-1; i >=0 ; i--) {
            sb.append(result.get(i));
        }
        return sb.toString();
    }

    private void solution(String temp, int count) {
        if(temp.equals("0000")) {
            result.add("零");
            return;
        }
        StringBuilder sb = new StringBuilder();
        int len = temp.length();

        for (int i = 0; i < len; i++) {
            if(temp.charAt(i) == '0') {
                sb.append("零");
            } else {
                sb.append(get0(len-i-1,temp.charAt(i)));
            }
        }
        sb.append(RmbSuffix1[count]);
        result.add(sb.toString());

    }


    private String get0(int len, char cc) {
        return RmbNumber[cc-'0'] + RmbSuffix[len];
    }


}

enum RMB {
    a("壹"),
    b("贰"),
    c("叁"),
    d("肆"),
    e("伍"),
    f("陆"),
    g("柒"),
    h("捌"),
    i("玖");
    private String rmb;
    private RMB(String rmb) {
        this.rmb = rmb;
    }
    public String getRmb() {
        return rmb;
    }
}

