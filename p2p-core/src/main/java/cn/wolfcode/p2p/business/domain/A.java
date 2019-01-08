package cn.wolfcode.p2p.business.domain;

public enum A {
    RED("红"),
    GREEN("绿"),
    YELLOW("黄");

    private String color;

    A(String color) {
        this.color=color;
    }

    public void print(){
        System.out.println(this.color);
    }

    public A getNext(){
        if (this==RED){
            return GREEN;

        }else if (this==GREEN){
            return YELLOW;
        }else {
            return RED;
        }
    }
}
