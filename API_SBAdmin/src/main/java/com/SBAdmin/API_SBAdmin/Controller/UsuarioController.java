package com.SBAdmin.API_SBAdmin.Controller;

import com.SBAdmin.API_SBAdmin.DAO.DAO;
import com.SBAdmin.API_SBAdmin.Modelo.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

import java.util.List;

@RestController
public class UsuarioController {

    @Autowired
    DAO<Usuario> dao;

    @RequestMapping(value = "api/registrar",method = RequestMethod.POST)
    public void registrarUsuarios(@RequestBody Usuario user){
        // instanciamos la libreria de argon para encriptar la contraseña
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        String hash = argon2.hash(1,1024,1, user.getPass());
        user.setPass(hash);
        // registramos el usuario con el pash hasheado
        dao.registrar(user);
    }
}
