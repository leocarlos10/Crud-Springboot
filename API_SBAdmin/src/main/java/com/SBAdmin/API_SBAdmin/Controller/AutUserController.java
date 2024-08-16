package com.SBAdmin.API_SBAdmin.Controller;

import com.SBAdmin.API_SBAdmin.DAO.DAO;
import com.SBAdmin.API_SBAdmin.Modelo.Usuario;
import com.SBAdmin.API_SBAdmin.jwt.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AutUserController {

    @Autowired
    DAO<Usuario> dao;

    @Autowired
    JWTUtil jwt;

    @RequestMapping(value = "api/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody Usuario user){
        Map<String,String> respuesta = new HashMap<>();
        // primero verificamos los datos para el inicio de sesion.
        Usuario usuario = (Usuario) dao.obtener(user);
        if(usuario != null){
            String token = jwt.create(String.valueOf(usuario.getId()), usuario.getEmail());
            respuesta.put("respuesta",token);
            return  ResponseEntity.ok().body(respuesta);
        }else{
            respuesta.put("respuesta","Usuario o Contraseña invalidos");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
        }
    }
}
