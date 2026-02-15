package com.finance.auth.login;

import com.finance.auth.client.Client;
import com.finance.auth.client.ClientRepository;
import com.finance.auth.login.dto.SignInRequest;
import com.finance.auth.login.dto.SignInResponse;
import com.finance.auth.login.dto.SignUpRequest;
import com.finance.auth.login.dto.SignUpResponse;
import com.finance.common.exception.ClientNotFoundException;
import com.finance.common.exception.DuplicateUsernameException;
import com.finance.common.exception.InvalidPasswordException;
import com.finance.common.jwt.JwtUtil;
import com.finance.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class LoginService {
    private final ClientRepository clientRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder encoder;

    public LoginService(ClientRepository clientRepository, JwtUtil jwtUtil, PasswordEncoder encoder) {
        this.clientRepository = clientRepository;
        this.jwtUtil = jwtUtil;
        this.encoder = encoder;
    }

    public SignInResponse signIn(SignInRequest request) {
        Client client = clientRepository.findByClientId(request.clientId())
                .orElseThrow(() -> new ClientNotFoundException(request.clientId()));

        if (!encoder.matches(request.password(), client.getPassword())) {
            throw new InvalidPasswordException();
        }

        String token = jwtUtil.generateToken(request.clientId());
        return new SignInResponse(token);
    }

    public SignUpResponse signUp(SignUpRequest dto) {
        Client client = new Client(
                dto.clientId(),
                encoder.encode(dto.password()),
                dto.clientName()
        );

        if (clientRepository.existsByClientId(dto.clientId())) {
            throw new DuplicateUsernameException();
        }

        try {
            Client saved = clientRepository.save(client);
            return new SignUpResponse(
                    saved.getId(),
                    saved.getClientId(),
                    saved.getClientName(),
                    "signup success"
            );
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateUsernameException();
        }
    }
}