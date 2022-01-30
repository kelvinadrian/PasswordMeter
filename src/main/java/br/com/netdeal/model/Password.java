package br.com.netdeal.model;

public class Password {

    private String senha;

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getTamanhoSenha(){
        if(senha == null){
            return 0;
        }else{
            return senha.length();
        }
    }
}
