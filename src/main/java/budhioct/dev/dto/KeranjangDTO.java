package budhioct.dev.dto;

import budhioct.dev.entity.Keranjang;
import budhioct.dev.entity.Product;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class KeranjangDTO {

    @Getter
    @Setter
    @Builder
    public static class KeranjangResponse {
        private Long id;
        private Integer jumlah_pemesanan;
        private String keterangan;
        private ProductResponse products;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime createdAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    @Builder
    public static class ProductResponse {
        private long id;
        private String kode;
        private String nama;
        private Integer harga;
        private Boolean is_ready;
        private String gambar;
    }

    @Getter
    @Setter
    @Builder
    public static class KeranjangRequest {
        @JsonIgnore
        private Long product_id;
        @NotNull
        private Integer jumlahPemesanan;
        private String keterangan;
    }

    @Getter
    @Setter
    @Builder
    public static class KeranjangDeleteRequest {
        @JsonIgnore
        private Long keranjang_id;
        @JsonIgnore
        private Long product_id;
    }

    public static KeranjangDTO.KeranjangResponse toKeranjangResponse(Keranjang ker) {
        return KeranjangDTO.KeranjangResponse.builder()
                .id(ker.getId())
                .jumlah_pemesanan(ker.getJumlahPemesanan())
                .keterangan(ker.getKeterangan())
                .products(toProductResponse(ker.getProduct()))
                .createdAt(ker.getCreatedAt())
                .updatedAt(ker.getUpdatedAt())
                .build();
    }

    public static KeranjangDTO.ProductResponse toProductResponse(Product prod) {
        return KeranjangDTO.ProductResponse.builder()
                .id(prod.getId())
                .kode(prod.getKode())
                .nama(prod.getNama())
                .harga(prod.getHarga())
                .is_ready(prod.getIsReady())
                .gambar(prod.getGambar())
                .build();
    }

}
