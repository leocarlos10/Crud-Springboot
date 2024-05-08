package com.cursojava.curso.controller;

import com.cursojava.curso.DAO.UsuarioDao;
import com.cursojava.curso.models.Usuario;
import com.cursojava.curso.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

// esta clase nos ayuda a validar el inicio de sesion del usuario
@RestController
public class AutController {

    @Autowired
    private UsuarioDao usuarioDao;

    /* agregamos autowired porque en la clase
    * estamos utilizanod @Component que permite utilizar
    * @Value para poder cargar las variables desde las properties*/
    @Autowired
    private JWTUtil jwtUtil;

    @RequestMapping(path = "api/login",method = RequestMethod.POST)
    public String login( @RequestBody  Usuario user){
        /* aqui debemos verificar el inicio de sesion*/
        Usuario usuarioLogueado = usuarioDao.obtenerUsersesion(user);
        // si la sesion es correcta creamos el JWT
        if(usuarioLogueado != null){
            // creamos y retornamos el token
           String token = jwtUtil.create(String.valueOf(usuarioLogueado.getId()), usuarioLogueado.getEmail());
           return token;
        }
        return "fail";
    }
}
