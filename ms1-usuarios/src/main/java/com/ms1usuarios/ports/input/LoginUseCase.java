package com.ms1usuarios.ports.input;

import com.ms1usuarios.dto.LoginRequest;
import com.ms1usuarios.dto.LoginResponse;

public interface LoginUseCase {

    LoginResponse login(LoginRequest request);

}