package edu.sv.ues.dam235.apirestdemo.implementations;

import edu.sv.ues.dam235.apirestdemo.dtos.CreateProductDTO;
import edu.sv.ues.dam235.apirestdemo.dtos.ProductsDTO;
import edu.sv.ues.dam235.apirestdemo.dtos.ResponseDTO;
import edu.sv.ues.dam235.apirestdemo.entities.Product;
import edu.sv.ues.dam235.apirestdemo.repositories.ProductRepository;
import edu.sv.ues.dam235.apirestdemo.services.ProductServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ProductsImpl implements ProductServices {
    private final ProductRepository productRepository;
    
    private ProductsImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    
    @Override
    public List<ProductsDTO> getALLProducts() {
        List<ProductsDTO> result = new ArrayList<>();
        List<Product> items = this.productRepository.findAll();
        for (Product item : items) {
            result.add(new ProductsDTO(item.getCode(), item.getName(),
                    item.isStatus()));
        }
        return result;
    }

    @Override
    public ResponseDTO createProduct(CreateProductDTO createProductDTO) {
        try {
            // Validar que el nombre del producto no esté vacío
            if (createProductDTO.getName() == null || createProductDTO.getName().trim().isEmpty()) {
                return new ResponseDTO(false, "El nombre del producto es requerido");
            }

            // Validar longitud mínima del nombre
            if (createProductDTO.getName().length() < 3) {
                return new ResponseDTO(false, "El nombre del producto debe tener mínimo 3 caracteres");
            }

            // Validar longitud máxima del nombre
            if (createProductDTO.getName().length() > 255) {
                return new ResponseDTO(false, "El nombre del producto no puede exceder 255 caracteres");
            }

            // Crear nuevo producto
            Product newProduct = new Product();
            newProduct.setName(createProductDTO.getName());
            newProduct.setStatus(createProductDTO.isStatus());

            // Guardar el producto
            Product savedProduct = this.productRepository.save(newProduct);

            // Preparar respuesta
            ProductsDTO productDTO = new ProductsDTO(
                    savedProduct.getCode(),
                    savedProduct.getName(),
                    savedProduct.isStatus()
            );

            ResponseDTO response = new ResponseDTO(true, "Producto creado exitosamente");
            response.setData(productDTO);

            log.info("Producto creado exitosamente. ID: {}, Nombre: {}", 
                    savedProduct.getCode(), savedProduct.getName());

            return response;

        } catch (Exception e) {
            log.error("Error al crear producto: {}", e);
            return new ResponseDTO(false, "Error al crear el producto: " + e.getMessage());
        }
    }
}
