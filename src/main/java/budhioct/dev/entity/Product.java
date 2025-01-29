package budhioct.dev.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners({AuditingEntityListener.class})
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String kode;

    @Column(name = "nama")
    private String nama;

    @Column(name = "harga")
    private Integer harga;

    @Column(name = "is_ready")
    private Boolean isReady;

    @Column(name = "gambar", columnDefinition = "text")
    private String gambar;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", insertable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Keranjang> keranjangs;

    public Product(String kode, String nama, Integer harga, Boolean isReady, String gambar) {
        this.kode = kode;
        this.nama = nama;
        this.harga = harga;
        this.isReady = isReady;
        this.gambar = gambar;
    }

}
