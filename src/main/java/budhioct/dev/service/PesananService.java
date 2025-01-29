package budhioct.dev.service;

import budhioct.dev.dto.PesananDTO;

import java.util.List;

public interface PesananService {

    List<PesananDTO.PesananResponse> listPesanan();
    PesananDTO.PesananResponse createPesanan(PesananDTO.PesananRequest request);
}
