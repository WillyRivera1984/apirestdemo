package edu.sv.ues.dam235.apirestdemo.controllers;

import edu.sv.ues.dam235.apirestdemo.dtos.CreateProductDTO;
import edu.sv.ues.dam235.apirestdemo.dtos.ProductsDTO;
import edu.sv.ues.dam235.apirestdemo.dtos.ResponseDTO;
import edu.sv.ues.dam235.apirestdemo.services.ProductServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/products")
public class ProductController {
    final private ProductServices productServices;
    
    private ProductController(ProductServices productServices) {
        this.productServices = productServices;
    }
    
    @GetMapping
    public ResponseEntity<List<ProductsDTO>> getAllItems() {
        try {
            List<ProductsDTO> items = productServices.getALLProducts();
            if (items.isEmpty()) {
                return ResponseEntity.status(204).build();
            } else {
                return ResponseEntity.ok(items);
            }
        } catch (Exception e) {
            log.error("{}", e);
        }
        return null;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> createProduct(@RequestBody CreateProductDTO createProductDTO) {
        try {
            System.out.println("Producto DTO enviado: " + createProductDTO.toString());
            ResponseDTO response = productServices.createProduct(createProductDTO);

            if (response.isSuccess()) {
                return ResponseEntity.status(201).body(response);
            } else {
                return ResponseEntity.status(400).body(response);
            }
        } catch (Exception e) {
            log.error("Error en creación de producto: {}", e);
            return ResponseEntity.status(500).body(
                new ResponseDTO(false, "Error al crear el producto: " + e.getMessage())
            );
        }
    }
}
