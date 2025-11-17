package edu.sv.ues.dam235.apirestdemo.controllers;

import edu.sv.ues.dam235.apirestdemo.dtos.LoginDTO;
import edu.sv.ues.dam235.apirestdemo.dtos.RegisterDTO;
import edu.sv.ues.dam235.apirestdemo.dtos.ResponseDTO;
import edu.sv.ues.dam235.apirestdemo.dtos.TokenDTO;
import edu.sv.ues.dam235.apirestdemo.services.AuthServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    final private AuthServices authServices;
    private AuthController(AuthServices authServices) {
        this.authServices = authServices;
    }
    
    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginDTO authRequest) {
        try {
            System.out.println("DTO enviado : " + authRequest.toString());
            TokenDTO token = authServices.login(authRequest.getUser(),
                    authRequest.getPass());
            if (token == null) {
                return ResponseEntity.status(401).build();
            } else {
                return ResponseEntity.ok(token);
            }
        } catch (Exception e) {
            log.error("{}", e);
        }
        return null;
    }
    
    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@RequestBody RegisterDTO registerDTO) {
        try {
            System.out.println("Registro DTO enviado: " + registerDTO.toString());
            ResponseDTO response = authServices.register(registerDTO);
            
            if (response.isSuccess()) {
                return ResponseEntity.status(201).body(response);
            } else {
                return ResponseEntity.status(400).body(response);
            }
        } catch (Exception e) {
            log.error("Error en registro: {}", e);
            return ResponseEntity.status(500).body(
                new ResponseDTO(false, "Error interno del servidor: " + e.getMessage())
            );
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<ResponseDTO> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || authHeader.isEmpty()) {
                return ResponseEntity.status(400).body(
                    new ResponseDTO(false, "Header Authorization no proporcionado")
                );
            }
            
            System.out.println("Logout solicitado con token");
            ResponseDTO response = authServices.logout(authHeader);
            
            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(400).body(response);
            }
        } catch (Exception e) {
            log.error("Error en logout: {}", e);
            return ResponseEntity.status(500).body(
                new ResponseDTO(false, "Error al cerrar sesión: " + e.getMessage())
            );
        }
    }
}