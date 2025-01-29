package budhioct.dev.service.impl;

import budhioct.dev.dto.KeranjangDTO;
import budhioct.dev.entity.Keranjang;
import budhioct.dev.entity.Product;
import budhioct.dev.repository.KeranjangRepository;
import budhioct.dev.repository.ProductRepository;
import budhioct.dev.service.KeranjangService;
import budhioct.dev.utilities.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeranjangServiceImpl implements KeranjangService {

    private final KeranjangRepository keranjangRepository;
    private final ProductRepository productRepository;
    private final ValidationService validation;

    @Transactional(readOnly = true)
    public List<KeranjangDTO.KeranjangResponse> listKeranjangs() {

        List<KeranjangDTO.KeranjangResponse> list = keranjangRepository.findAll()
                .stream()
                .map(KeranjangDTO::toKeranjangResponse)
                .toList();

        return list;
    }

    @Transactional
    public KeranjangDTO.KeranjangResponse createKeranjang(KeranjangDTO.KeranjangRequest request) {
        validation.validate(request);

        Product product = productRepository.findFirstById(request.getProduct_id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found"));

        Keranjang ker = new Keranjang();
        ker.setKeterangan(request.getKeterangan());
        ker.setJumlahPemesanan(request.getJumlahPemesanan());
        ker.setProduct(product);
        keranjangRepository.save(ker);

        return KeranjangDTO.toKeranjangResponse(ker);
    }

    @Transactional
    public void removeKeranjang(KeranjangDTO.KeranjangDeleteRequest request) {
        Product product = productRepository.findFirstById(request.getProduct_id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found"));

        Keranjang keranjang = keranjangRepository.findFirstByProductAndId(product, request.getKeranjang_id())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "keranjang not found"));

        keranjangRepository.delete(keranjang);
    }

}
