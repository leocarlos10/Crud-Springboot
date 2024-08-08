package com.SBAdmin.API_SBAdmin.Controller;

import com.SBAdmin.API_SBAdmin.DAO.DAO;
import com.SBAdmin.API_SBAdmin.DAO.UsuarioDAO;
import com.SBAdmin.API_SBAdmin.Modelo.Cuenta;
import com.SBAdmin.API_SBAdmin.Modelo.Usuario;
import com.SBAdmin.API_SBAdmin.jwt.JWTUtil;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
public class CuentaController {

    @Autowired
    DAO<Cuenta> dao;

    @Autowired
    DAO<Usuario> daouser;

    @Autowired
    JWTUtil jwt;

    @RequestMapping(value = "api/cuentas/{email}", method = RequestMethod.POST)
    public void registrarCuenta( @PathVariable String email ,@RequestBody Cuenta cuenta, @RequestHeader(value = "Authorization") String token){
        if(jwt.validarToken(token)){
            Long id = daouser.obtener(email);
            if(id != null){
                cuenta.setId_usuario(id);
                dao.registrar(cuenta);
            }else{
                System.out.println("La cuenta no se registro!");
            }
        }
    }

    @RequestMapping(value = "api/cuentas/{email}", method = RequestMethod.GET)
    public ResponseEntity<List<Cuenta>> getCuentas( @PathVariable String email ,@RequestHeader(value = "Authorization") String token){
        // antes de devolver los usuarios verificamos que el usuario este logueado
        if(jwt.validarToken(token)){
            // obtenemos los datos de la DB
            // daouser.obtener(email) es para obtener el id del usuario logueado.
            List<Cuenta> cuentas = dao.get(daouser.obtener(email));
            // enviamos la respuesta ok con la lista de cuentas
            return ResponseEntity.ok(cuentas);
        }
        /*
        en caso de que el usuario no este logueado
        envia HttpStatus.UNAUTHORIZED: Es el código de estado HTTP 401,
         que indica que la solicitud no ha sido aplicada porque carece de
         credenciales de autenticación válidas.
        */
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

    @RequestMapping(value = "api/cuentas/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> EliminarCuenta( @PathVariable Long id, @RequestHeader(value = "Authorization") String token ){

        Map<String ,String> respuesta = new HashMap<>();
        // verificamos el jwt
        if(jwt.validarToken(token)){
           boolean estado =  dao.eliminar(id);
           if (estado) {
               // retorno un codigo 200
                respuesta.put("mensaje", "Cuenta eliminada correctamente");
               return ResponseEntity.ok().body(respuesta);
           }else {
               // retorno un codigo 404
               respuesta.put("mensaje","La cuenta no fue encontrada");
               return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
           }
        }else{
            // retorno un codigo 401
            respuesta.put("mensaje","Por favor Inicie sesion!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
        }
    }

    @RequestMapping(value = "api/cuentas/actualizar", method = RequestMethod.PUT)
    public ResponseEntity<?> actualizarCuenta(@RequestBody Cuenta cuenta, @RequestHeader(value = "Authorization") String token){

        // este map se utiliza para que cuando devolvamos la respuesta pueda leerla como un json
        Map<String ,String> respuesta = new HashMap<>();
        if(jwt.validarToken(token)){
            try {
                dao.actualizar(cuenta);
                respuesta.put("mensaje", "La cuenta fue actualizada correctamente");
                return ResponseEntity.ok().body(respuesta);
            }catch (Exception e){
                // Retorna 500 Internal Server Error con mensaje de error
                System.out.println("error al actualizar la cuenta" + e);
                respuesta.put("mensaje", "Error al actualizar la cuenta "+e);
                return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);

            }
        }else{
            // retorno un codigo 401 carencia de credenciales validas
            respuesta.put("mensaje","Por favor Inicie sesion!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(respuesta);
        }
    }
}
