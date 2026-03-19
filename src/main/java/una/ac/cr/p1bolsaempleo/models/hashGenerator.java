package una.ac.cr.p1bolsaempleo.models;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class hashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("1234")); // clave para oferente
        System.out.println(encoder.encode("abcd")); // clave para empresa
    }
}
