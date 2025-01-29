package budhioct.dev.rest.contoller;

import budhioct.dev.dto.ProductDTO;
import budhioct.dev.rest.config.RestResponse;
import budhioct.dev.service.ProductService;
import budhioct.dev.utilities.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/products")
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class ProductRestController {

    @Autowired
    private ProductService productService;

    @GetMapping(
            path = "/fetch",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyAuthority('user:read', 'admin:read')")
    public ResponseEntity<Map<String, List<ProductDTO.ProductResponse>>> listProduct(
            @RequestParam(value = "search", required = false) String search
    ){
        List<ProductDTO.ProductResponse> products = productService.listProduct(search);
        Map<String, List<ProductDTO.ProductResponse>> response = new HashMap<>();
        response.put("products", products);
        return ResponseEntity.ok(response);
    }

    @GetMapping(
            path = "/fetch/best",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyAuthority('user:read', 'admin:read')")
    public ResponseEntity<Map<String, List<ProductDTO.ProductResponse>>> bestProduct(){
        List<ProductDTO.ProductResponse> products = productService.bestProduct();
        Map<String, List<ProductDTO.ProductResponse>> response = new HashMap<>();
        response.put("products", products);
        return ResponseEntity.ok(response);
    }

    @GetMapping(
            path = "{id}/detail",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyAuthority('user:create', 'admin:create')")
    public RestResponse.object<ProductDTO.ProductResponse> detailProduct(@PathVariable(name = "id") Long id){
        ProductDTO.ProductResponse product = productService.detailProduct(id);
        return RestResponse.object.<ProductDTO.ProductResponse>builder()
                .data(product)
                .status_code(Constants.OK)
                .message(Constants.ITEM_EXIST_MESSAGE)
                .build();
    }

    @PostMapping(
            path = "/create",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyAuthority('user:create', 'admin:create')")
    public ResponseEntity<RestResponse.object<ProductDTO.ProductResponse>> createProduct(@RequestBody ProductDTO.ProductRequest request) {
        ProductDTO.ProductResponse product = productService.createProduct(request);
        RestResponse.object<ProductDTO.ProductResponse> build = RestResponse.object.<ProductDTO.ProductResponse>builder()
                .data(product)
                .status_code(Constants.CREATED)
                .message(Constants.CREATE_MESSAGE)
                .build();
        return new ResponseEntity<>(build, HttpStatus.CREATED);
    }

    @DeleteMapping(
            path = "/{id}/remove",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAnyAuthority('user:delete', 'admin:delete')")
    public RestResponse.object<String> removeProduct(@PathVariable(value = "id" ) long id) {
        productService.removeProduct(id);
        return RestResponse.object.<String>builder()
                .data("")
                .status_code(Constants.OK)
                .message(Constants.DELETE_MESSAGE)
                .build();
    }

}
