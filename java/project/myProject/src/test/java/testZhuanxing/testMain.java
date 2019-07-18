package testZhuanxing;

/**
 * @Description:
 * @Author: wangkang
 * @Date: Created in 21:30 2019/1/20
 * @Modified By:
 */
public class testMain extends Protalbe{
    Brand b = new Brand();
    Cheese c = new Cheese();

    public testMain() {
        System.out.println("testMain");
    }

    public static void main(String[] args) {
        new testMain();
    }
}

class Meal {
    Brand bb = new Brand();
    Meal() {
        System.out.println("meal");
    }
}

class Brand {
    Brand() {
        System.out.println("Brand");
    }
}

class Cheese {
    Cheese() {
        System.out.println("Cheese");
    }
}


class Lunch extends Meal {
    Lunch() {
        System.out.println("Lunch");
    }
}

class Protalbe extends Lunch {
    Protalbe() {
        System.out.println("Protalbe");
    }
}