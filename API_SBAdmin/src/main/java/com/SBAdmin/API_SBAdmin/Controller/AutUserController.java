package com.SBAdmin.API_SBAdmin.Controller;

import com.SBAdmin.API_SBAdmin.DAO.DAO;
import com.SBAdmin.API_SBAdmin.Modelo.Usuario;
import com.SBAdmin.API_SBAdmin.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AutUserController {

    @Autowired
    DAO<Usuario> dao;

    @Autowired
    JWTUtil jwt;

    @RequestMapping(value = "api/login", method = RequestMethod.POST)
    public String login(@RequestBody Usuario user){
        // primero verificamos los datos para el inicio de sesion.
        Usuario usuario = (Usuario) dao.obtener(user);
        if(usuario != null){
            String token = jwt.create(String.valueOf(usuario.getId()), usuario.getEmail());
            return token;
        }
        return "fail";
    }
}
