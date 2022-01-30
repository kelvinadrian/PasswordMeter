package br.com.netdeal.controller;

import br.com.netdeal.espec.PasswordValidationInterface;
import br.com.netdeal.model.Password;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PasswordValidationController {

    @Autowired
    private PasswordValidationInterface passwordValidationInterface;

    @RequestMapping(value = "/validar", method = RequestMethod.POST, consumes = {
            MediaType.APPLICATION_JSON_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> validar(@RequestBody Password password) {
        Map<String, String> mapa = passwordValidationInterface.validarSenha(password);
        return ResponseEntity.ok(mapa);
    }
}
