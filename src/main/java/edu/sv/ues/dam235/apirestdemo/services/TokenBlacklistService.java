package edu.sv.ues.dam235.apirestdemo.services;

import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TokenBlacklistService {
    
    private final Set<String> blacklistedTokens = ConcurrentHashMap.newKeySet();
    
    /**
     * Agrega un token a la lista negra
     * @param token Token JWT a invalidar
     */
    public void addToBlacklist(String token) {
        if (token != null && !token.isEmpty()) {
            blacklistedTokens.add(token);
        }
    }
    
    /**
     * Verifica si un token está en la lista negra
     * @param token Token JWT a verificar
     * @return true si está en la lista negra, false en caso contrario
     */
    public boolean isTokenBlacklisted(String token) {
        return token != null && blacklistedTokens.contains(token);
    }
    
    /**
     * Obtiene el tamaño de la lista negra
     * @return Cantidad de tokens en la lista negra
     */
    public int getBlacklistSize() {
        return blacklistedTokens.size();
    }
    
    /**
     * Limpia la lista negra (útil para testing)
     */
    public void clearBlacklist() {
        blacklistedTokens.clear();
    }
}
