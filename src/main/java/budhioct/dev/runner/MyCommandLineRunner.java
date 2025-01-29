package budhioct.dev.runner;

import budhioct.dev.entity.Product;
import budhioct.dev.entity.User;
import budhioct.dev.repository.KeranjangRepository;
import budhioct.dev.repository.PesananRepository;
import budhioct.dev.repository.ProductRepository;
import budhioct.dev.repository.UserRepository;
import budhioct.dev.security.role.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MyCommandLineRunner implements CommandLineRunner {

    private final KeranjangRepository keranjangRepository;
    private final PesananRepository pesananRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    List<User> users = List.of(
            new User("budhiocta@test.com", passwordEncoder.encode("password"), Role.USER),
            new User("user@test.com", passwordEncoder.encode("password"), Role.USER),
            new User("admin@test.com", passwordEncoder.encode("password"), Role.ADMIN)
    );

    List<Product> products = List.of(
            new Product("K-001", "Sate Ayam", 25000, true, "sate-ayam.jpg"),
            new Product("K-002", "Nasi Goreng", 15000, true, "nasi-goreng.jpg"),
            new Product("K-003", "Mie Goreng", 14000, true, "mie-goreng.jpg"),
            new Product("K-004", "Nasi Rames", 12000, true, "nasi-rames.jpg"),
            new Product("K-005", "Bakso & Mie Ayam", 18000, true, "bakso-mie-ayam.jpg"),
            new Product("K-006", "Bakso", 15000, true, "bakso.jpg"),
            new Product("K-007", "Mie Ayam", 12000, true, "mie-ayam.jpg"),
            new Product("K-008", "Soto Ayam", 20000, true, "soto-ayam.jpg"),
            new Product("K-009", "Rendang", 25000, true, "rendang.jpg"),
            new Product("K-010", "Gado-Gado", 15000, true, "gado-gado.jpg"),
            new Product("K-011", "Nasi Uduk", 12000, true, "nasi-uduk.jpg"),
            new Product("K-012", "Ayam Goreng", 20000, true, "ayam-goreng.jpg"),
            new Product("K-013", "Ayam Bakar", 22000, true, "ayam-bakar.jpg"),
            new Product("K-014", "Ikan Bakar", 25000, true, "ikan-bakar.jpg"),
            new Product("K-015", "Pecel Lele", 15000, true, "pecel-lele.jpg"),
            new Product("K-016", "Sayur Asem", 12000, true, "sayur-asem.jpg"),
            new Product("K-017", "Sate Kambing", 30000, true, "sate-kambing.jpg"),
            new Product("K-018", "Ikan Goreng", 20000, true, "ikan-goreng.jpg"),
            new Product("K-019", "Sate Padang", 25000, true, "sate-padang.jpg"),
            new Product("K-020", "Nasi Pecel", 15000, true, "nasi-pecel.jpg"),
            new Product("K-021", "Nasi Liwet", 20000, true, "nasi-liwet.jpg"),
            new Product("K-022", "Es Teh Manis", 5000, true, "es-teh-manis.jpg"),
            new Product("K-023", "Es Jeruk", 7000, true, "es-jeruk.jpg"),
            new Product("K-024", "Es Campur", 15000, true, "es-campur.jpg"),
            new Product("K-025", "Es Teler", 15000, true, "es-teler.jpg"),
            new Product("K-026", "Teh Hangat", 5000, true, "teh-hangat.jpg")
    );

    @Override
    public void run(String... args) throws Exception {
        userRepository.deleteAll();
        productRepository.deleteAll();
        keranjangRepository.deleteAll();
        pesananRepository.deleteAll();

        userRepository.saveAll(users);
        productRepository.saveAll(products);
    }

}
