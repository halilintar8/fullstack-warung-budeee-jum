package budhioct.dev.dto;

import budhioct.dev.entity.Product;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class ProductDTO {

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
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime createdAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    @Builder
    public static class ProductRequest {
        @NotBlank
        private String kode;
        @NotBlank
        private String nama;
        @NotNull
        private Integer harga;
        @NotNull
        private Boolean isReady;
        @NotBlank
        private String gambar;
    }

    public static ProductResponse toProductResponse(Product prod) {
        return ProductResponse.builder()
                .id(prod.getId())
                .kode(prod.getKode())
                .nama(prod.getNama())
                .harga(prod.getHarga())
                .is_ready(prod.getIsReady())
                .gambar(prod.getGambar())
                .createdAt(prod.getCreatedAt())
                .updatedAt(prod.getUpdatedAt())
                .build();
    }

}
