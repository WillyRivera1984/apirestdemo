package edu.sv.ues.dam235.apirestdemo.implementations;

import edu.sv.ues.dam235.apirestdemo.configs.CustomerDetailServices;
import edu.sv.ues.dam235.apirestdemo.dtos.RegisterDTO;
import edu.sv.ues.dam235.apirestdemo.dtos.ResponseDTO;
import edu.sv.ues.dam235.apirestdemo.dtos.TokenDTO;
import edu.sv.ues.dam235.apirestdemo.dtos.UserDTO;
import edu.sv.ues.dam235.apirestdemo.entities.User;
import edu.sv.ues.dam235.apirestdemo.repositories.UserRepository;
import edu.sv.ues.dam235.apirestdemo.services.AuthServices;
import edu.sv.ues.dam235.apirestdemo.services.TokenBlacklistService;
import edu.sv.ues.dam235.apirestdemo.utilities.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthServicesImpl implements AuthServices {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomerDetailServices customerDetailServices;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Override
    public TokenDTO login(String user, String pass) {
        TokenDTO token = new TokenDTO();
        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(user, pass)
                    );
            if (authentication.isAuthenticated()) {
                UserDetails usuarioDetail = (UserDetails)
                        authentication.getPrincipal();
                if (customerDetailServices.getUserDetail().getActive()) {
                    token = jwtUtil.generateToken(user, usuarioDetail);
                    return token;
                }
            }
        } catch (BadCredentialsException bad) {
            token.setMsj("Credenciales incorrectas!");
            return null;
        } catch (Exception e) {
            log.error("{}", e);
            token.setMsj("Error innesperado");
            return null;
        }
        return null;
    }

    @Override
    public ResponseDTO register(RegisterDTO registerDTO) {
        try {
            // Validar que todos los campos requeridos estén presentes
            if (registerDTO.getName() == null || registerDTO.getName().trim().isEmpty() ||
                registerDTO.getLastName() == null || registerDTO.getLastName().trim().isEmpty() ||
                registerDTO.getEmail() == null || registerDTO.getEmail().trim().isEmpty() ||
                registerDTO.getPassword() == null || registerDTO.getPassword().trim().isEmpty()) {
                return new ResponseDTO(false, "Todos los campos son requeridos");
            }

            // Validar que las contraseñas coincidan
            if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
                return new ResponseDTO(false, "Las contraseñas no coinciden");
            }

            // Validar que la contraseña tenga mínimo 6 caracteres
            if (registerDTO.getPassword().length() < 6) {
                return new ResponseDTO(false, "La contraseña debe tener mínimo 6 caracteres");
            }

            // Validar que el email no esté registrado
            User existingUser = userRepository.findByEmail(registerDTO.getEmail());
            if (existingUser != null) {
                return new ResponseDTO(false, "El correo electrónico ya está registrado");
            }

            // Crear nuevo usuario
            User newUser = new User();
            newUser.setName(registerDTO.getName());
            newUser.setLastName(registerDTO.getLastName());
            newUser.setEmail(registerDTO.getEmail());
            newUser.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
            newUser.setActive(true);

            // Guardar el usuario
            User savedUser = userRepository.save(newUser);

            // Preparar datos de respuesta (sin exponer la contraseña)
            UserDTO userDTO = new UserDTO(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getLastName(),
                savedUser.getEmail()
            );

            ResponseDTO responseData = new ResponseDTO(true, "Usuario registrado exitosamente");
            responseData.setData(userDTO);

            return responseData;

        } catch (Exception e) {
            log.error("Error registrando usuario: {}", e);
            return new ResponseDTO(false, "Error al registrar el usuario: " + e.getMessage());
        }
    }

    @Override
    public ResponseDTO logout(String token) {
        try {
            // Validar que el token no esté vacío
            if (token == null || token.trim().isEmpty()) {
                return new ResponseDTO(false, "Token no proporcionado");
            }

            // Remover el prefijo "Bearer " si existe
            String tokenToBlacklist = token;
            if (token.startsWith("Bearer ")) {
                tokenToBlacklist = token.substring(7);
            }

            // Validar que el token sea válido antes de agregarlo a la lista negra
            if (jwtUtil.isTokenExpired(tokenToBlacklist)) {
                return new ResponseDTO(false, "El token ya ha expirado");
            }

            // Agregar token a la lista negra
            tokenBlacklistService.addToBlacklist(tokenToBlacklist);

            log.info("Usuario desconectado exitosamente. Tokens en blacklist: {}", 
                    tokenBlacklistService.getBlacklistSize());

            return new ResponseDTO(true, "Sesión cerrada exitosamente");

        } catch (Exception e) {
            log.error("Error al cerrar sesión: {}", e);
            return new ResponseDTO(false, "Error al cerrar la sesión: " + e.getMessage());
        }
    }
}