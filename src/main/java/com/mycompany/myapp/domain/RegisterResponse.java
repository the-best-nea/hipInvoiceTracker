package com.mycompany.myapp.domain;

import java.util.List;

public class RegisterResponse {

    List<RegisterItem> register;

    public List<RegisterItem> getRegister() {
        return register;
    }

    public RegisterResponse setRegister(List<RegisterItem> register) {
        this.register = register;
        return this;
    }
}
