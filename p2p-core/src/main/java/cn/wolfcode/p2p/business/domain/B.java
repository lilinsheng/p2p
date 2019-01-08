package cn.wolfcode.p2p.business.domain;

public class B {
    public static void main(String[] args) {
        A.RED.print();
        A next = A.RED.getNext();
        next.print();
    }
}
