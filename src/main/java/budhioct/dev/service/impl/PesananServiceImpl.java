package budhioct.dev.service.impl;

import budhioct.dev.dto.PesananDTO;
import budhioct.dev.entity.Keranjang;
import budhioct.dev.entity.Pesanan;
import budhioct.dev.repository.KeranjangRepository;
import budhioct.dev.repository.PesananRepository;
import budhioct.dev.service.PesananService;
import budhioct.dev.utilities.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PesananServiceImpl implements PesananService {

    private final PesananRepository pesananRepository;
    private final KeranjangRepository keranjangRepository;
    private final ValidationService validation;

    @Transactional
    public List<PesananDTO.PesananResponse> listPesanan() {

        List<PesananDTO.PesananResponse> list = pesananRepository.findAll()
                .stream()
                .map(PesananDTO::toPesananResponse)
                .toList();

        if (list.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "keranjang not found");
        }

        return list;
    }

    @Transactional
    public PesananDTO.PesananResponse createPesanan(PesananDTO.PesananRequest request) {
        validation.validate(request);

        List<Keranjang> keranjangList = keranjangRepository.findAllById(request.getKeranjang_ids());
        if (keranjangList.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No keranjangs found for the provided IDs");
        }
        keranjangList.forEach(keranjang -> {
            if (keranjang.getPesanan() != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Keranjang already associated with a Pesanan");
            }
        });

        Pesanan pes = new Pesanan();
        pes.setNama(request.getNama());
        pes.setNoMeja(request.getNoMeja());
        keranjangList.forEach(pes::addKeranjang);
        pesananRepository.save(pes);

        return PesananDTO.toPesananResponse(pes);
    }
}
