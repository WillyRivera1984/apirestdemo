package edu.sv.ues.dam235.apirestdemo.services;

import edu.sv.ues.dam235.apirestdemo.dtos.CreateProductDTO;
import edu.sv.ues.dam235.apirestdemo.dtos.ProductsDTO;
import edu.sv.ues.dam235.apirestdemo.dtos.ResponseDTO;
import java.util.List;

public interface ProductServices {
    public List<ProductsDTO> getALLProducts();
    public ResponseDTO createProduct(CreateProductDTO createProductDTO);
}