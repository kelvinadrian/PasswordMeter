package br.com.netdeal.espec;

import br.com.netdeal.model.Password;

import java.util.Map;

public interface PasswordValidationInterface {

    public Map<String, String> validarSenha(Password password);
}
