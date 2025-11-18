package edu.sv.ues.dam235.apirestdemo.services;

import edu.sv.ues.dam235.apirestdemo.dtos.RegisterDTO;
import edu.sv.ues.dam235.apirestdemo.dtos.ResponseDTO;
import edu.sv.ues.dam235.apirestdemo.dtos.TokenDTO;

public interface AuthServices {
    public TokenDTO login(String user, String pass);

    /* Se agregaron los metodo register para nuevos usuarios y logout para el cierre de sesion */
    
    public ResponseDTO register(RegisterDTO registerDTO);
    public ResponseDTO logout(String token);
}
