package multiDynamic;


import javax.annotation.processing.AbstractProcessor;

class A {
    public String show(D obj) {    
        return ("A and D");    
    }    
    
    public String show(A obj) {    
        return ("A and A");    
    }     
    
}

class B extends A{
   public String show(B obj){
       return ("B and B");
   }

   public String show(A obj){
       return ("B and A");
   }
}
    
 class C extends B{    
    
}    
    
 class D extends B{    
    
}    
    
public class Test {
    public static void main(String[] args) {
        B bb = new B();
        A a1 = new A();
        A aa = (A)bb;//b->a!!!
        System.out.println(aa.show(a1));
        A a2 = new B();//这种情况只能执行，A,B都有的B中的方法，及B重写父类的方法，所以只可能执行，A--D 和 B--A
        				
        				//a2调用 show(b)方法，   这里的this是B对象，因为没有继承关系，所以this.show(b)不会调用。 在查找super.show(b),没有，再查this.(super(b)),这里有继承关系了所以成功调用
        			   //a2调用show(c)方法，这里的this是B对象，因为没有继承关系，直接导this.show(super(c))=this.show(b) 同上了
        			   //a2调用show(d)方法，这里this是B对象，调用super.show(d)  -->a and d
        
        B b = new B();  //直接就有 A中的所有方法，且只有用super才可以执行父类中 的被重写的方法  
        C c = new C();    
        D d = new D();    
            
        System.out.println("1--" + a1.show(b));    
        System.out.println("2--" + a1.show(c));    
        System.out.println("3--" + a1.show(d));    
        System.out.println("4--" + a2.show(b));  //4--B and A .首先a2是A引用，B实例，调用show（B b）方法，此方法在父类A中没有定义，所以B中方法show(B b)不会调用（多态必须父类中已定义该方法），再按优先级为：this.show(O)、super.show(O)、this.show((super)O)、super.show((super)O)，即先查this对象的父类，没有重头再查参数的父类。查找super.show((super)O)时，B中没有，再向上，找到A中show(A a),因此执行。  
  
        System.out.println("5--" + a2.show(c));  //同上  
        System.out.println("6--" + a2.show(d));  //A and D .查找B中没有show(D d)方法，再查A中，有，执行。  
        System.out.println("7--" + b.show(b));    
        System.out.println("8--" + b.show(c));  //B and B .  
        System.out.println("9--" + b.show(d));          
    }    
}    