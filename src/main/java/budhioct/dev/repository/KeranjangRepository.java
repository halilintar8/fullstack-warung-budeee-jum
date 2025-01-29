package budhioct.dev.repository;

import budhioct.dev.entity.Keranjang;
import budhioct.dev.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KeranjangRepository extends JpaRepository<Keranjang, Long> {

    Optional<Keranjang> findFirstByProductAndId(Product product, Long id);
}
