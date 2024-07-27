package com.jtbank.backend.service;

public interface IJWTService {
    String generateToken(String accountNumber);
    String validateToken(String token);
}
