package application;

import java.util.List;
import java.util.stream.Stream;

public class Play_C2_Streams {
    public static void main(String[] args){
        Stream<String> names = List.of( "Hendricks", "Raymond", "Pena", "Gonzalez",
            "Nielsen", "Hamilton", "Graham", "Gill", "Vance", "Howe", "Ray", "Talley",
            "Brock", "Hall", "Gomez", "Bernard", "Witt", "Joyner", "Rutledge", "Petty",
            "Strong", "Soto", "Duncan", "Lott", "Case", "Richardson", "Crane", "Cleveland",
            "Casey", "Buckner", "Hardin", "Marquez", "Navarro").stream();
        
        Stream<String> names2 = List.of( "Hendricks", "Raymond", "Pena", "Gonzalez",
            "Nielsen", "Hamilton", "Graham", "Gill", "Vance", "Howe", "Ray", "Talley",
            "Brock", "Hall", "Gomez", "Bernard", "Witt", "Joyner", "Rutledge", "Petty",
            "Strong", "Soto", "Duncan", "Lott", "Case", "Richardson", "Crane", "Cleveland",
            "Casey", "Buckner", "Hardin", "Marquez", "Navarro").stream();
    
        
        System.out.println("Aufgabe 1:");    
        System.out.println("");    
        names
            .sorted((n1, n2) -> Integer.compare(n1.length(), n2.length()) == 0 ? n1.compareTo(n2) : Integer.compare(n1.length(), n2.length()))  // sort in descending order
            .forEach(n -> System.out.println(n)); 
        

        System.out.println("");
        System.out.println("Aufabe 2:");
        System.out.println("");

        names2
            .filter((n1 -> n1.charAt(n1.length() - 2) == 'e' && n1.charAt(n1.length() - 1) == 'z'))
            .forEach(n -> System.out.println(n));
    }   
}
