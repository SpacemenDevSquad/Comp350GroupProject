package edu.gcc.prij;

import edu.gcc.prij.utils.Availability;

public class Main {
    public static void main(String[] args) {
        Availability a = new Availability("{\"M\": [[480, 1080]],"+
                            "\"T\": [[480, 530], [600, 1080]],"+
                            "\"W\": [[480, 1080]],"+
                            "\"R\": [[480, 1080]],"+
                            "\"F\": [[480, 1080]]}");
        System.out.println(a);
    }
}