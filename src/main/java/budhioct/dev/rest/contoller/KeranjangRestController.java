package budhioct.dev.rest.contoller;

import budhioct.dev.dto.KeranjangDTO;
import budhioct.dev.rest.config.RestResponse;
import budhioct.dev.service.KeranjangService;
import budhioct.dev.utilities.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class KeranjangRestController {

    @Autowired
    KeranjangService keranjangService;

    @GetMapping(
            path = "/keranjangs/fetch",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyAuthority('user:read', 'admin:read')")
    public ResponseEntity<Map<String, List<KeranjangDTO.KeranjangResponse>>> listKeranjangs() {

        List<KeranjangDTO.KeranjangResponse> keranjangs = keranjangService.listKeranjangs();
        Map<String, List<KeranjangDTO.KeranjangResponse>> response = new HashMap<>();
        response.put("keranjangs", keranjangs);
        return ResponseEntity.ok(response);
    }

    @PostMapping(
            path = "/product/{id}/keranjangs/create",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyAuthority('user:create', 'admin:create')")
    public RestResponse.object<KeranjangDTO.KeranjangResponse> createKeranjang(@PathVariable(name = "id") Long id,
                                                                               @RequestBody KeranjangDTO.KeranjangRequest request) {
        request.setProduct_id(id);
        KeranjangDTO.KeranjangResponse keranjang = keranjangService.createKeranjang(request);
        return RestResponse.object.<KeranjangDTO.KeranjangResponse>builder()
                .data(keranjang)
                .status_code(Constants.CREATED)
                .message(Constants.CREATE_MESSAGE)
                .build();
    }

    @DeleteMapping(
            path = "/product/{product_id}/keranjangs/{keranjang_id}/remove",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyAuthority('user:delete', 'admin:delete')")
    public RestResponse.object<String> removeKeranjang(@PathVariable(name = "product_id") Long product_id,
                                                       @PathVariable(name = "keranjang_id") Long keranjang_id,
                                                       KeranjangDTO.KeranjangDeleteRequest request) {
        request.setProduct_id(product_id);
        request.setKeranjang_id(keranjang_id);
        keranjangService.removeKeranjang(request);
        return RestResponse.object.<String>builder()
                .data("")
                .status_code(Constants.OK)
                .message(Constants.DELETE_MESSAGE)
                .build();

    }

}
