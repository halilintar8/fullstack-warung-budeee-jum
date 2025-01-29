package budhioct.dev.repository;

import budhioct.dev.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findFirstByKode(String kode);
    Optional<Product> findFirstById(long id);
    @Query("SELECT p FROM Product p WHERE " +
            "lower(p.nama) LIKE lower(concat('%', :search, '%')) OR " +
            "cast(p.harga AS string) LIKE concat('%', :search, '%')")
    List<Product> searchProducts(@Param("search") String search);
    Optional<Product> findFirstByNama(String nama);

}
