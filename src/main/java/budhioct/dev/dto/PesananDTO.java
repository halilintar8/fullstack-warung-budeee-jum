package budhioct.dev.dto;

import budhioct.dev.entity.Keranjang;
import budhioct.dev.entity.Pesanan;
import budhioct.dev.entity.Product;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PesananDTO {

    @Getter
    @Setter
    @Builder
    public static class PesananResponse {
        private Long id;
        private String nama;
        private String noMeja;
        private List<KeranjangResponse> keranjangs;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime createdAt;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    @Builder
    public static class KeranjangResponse {
        private Long id;
        private Integer jumlah_pemesanan;
        private String keterangan;
        private ProductResponse products;
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
    public static class PesananRequest{
        private List<Long> keranjang_ids;
        private String nama;
        @NotBlank
        private String noMeja;
    }

    public static PesananDTO.PesananResponse toPesananResponse(Pesanan pes){
        return PesananResponse.builder()
                .id(pes.getId())
                .nama(pes.getNama())
                .noMeja(pes.getNoMeja())
                .keranjangs(pes.getKeranjangs().stream().map(PesananDTO::toKeranjangResponse).collect(Collectors.toList()))
                .createdAt(pes.getCreatedAt())
                .updatedAt(pes.getUpdatedAt())
                .build();
    }

    public static PesananDTO.KeranjangResponse toKeranjangResponse(Keranjang ker) {
        return PesananDTO.KeranjangResponse.builder()
                .id(ker.getId())
                .jumlah_pemesanan(ker.getJumlahPemesanan())
                .keterangan(ker.getKeterangan())
                .products(toProductResponse(ker.getProduct()))
                .build();
    }

    public static PesananDTO.ProductResponse toProductResponse(Product prod) {
        return PesananDTO.ProductResponse.builder()
                .id(prod.getId())
                .kode(prod.getKode())
                .nama(prod.getNama())
                .harga(prod.getHarga())
                .is_ready(prod.getIsReady())
                .gambar(prod.getGambar())
                .build();
    }

}
