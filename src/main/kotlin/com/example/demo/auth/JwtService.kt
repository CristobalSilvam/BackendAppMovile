package com.example.demo.auth

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Service
import java.util.Date
import javax.crypto.SecretKey

@Service
class JwtService {

    // CLAVE SECRETA: En producción usa una variable de entorno. Aquí usamos una fija para aprender.
    // Debe ser larga y segura (Hexadecimal de 256 bits o más)
    private val SECRET_KEY = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970"

    fun generateToken(username: String, role: String): String {
        return Jwts.builder()
            .setSubject(username) // El "dueño" del token (email)
            .claim("role", role)  // Guardamos el ROL dentro del token
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // Expira en 24 horas
            .signWith(getSigningKey(), SignatureAlgorithm.HS256)
            .compact()
    }

    private fun getSigningKey(): SecretKey {
        val keyBytes = Decoders.BASE64.decode(SECRET_KEY)
        return Keys.hmacShaKeyFor(keyBytes)
    }
}