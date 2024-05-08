package com.cursojava.curso.controller;

import com.cursojava.curso.DAO.UsuarioDao;
import com.cursojava.curso.models.Usuario;
import com.cursojava.curso.utils.JWTUtil;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/*
    *  - Los controladores en el proyecto sirven para manejar la url
         es decir que si el usuario esta entrando a midominio/usuario/cuenta
        retorne un json, sirven para manejar las dirrecciones de url
     *
     * para indicar que esta clase es un controlador usamos
     * @RestController
     *
     * para devolver el recurso del metodo usamos
     * @RequestMapping("/listaU") puedes hacerlo asi
     * o tambien @RequestMapping(value = "listaU")
     *
     * la explicacion de la anotacion @AutoWired esta en notion.
     * pero en resuemen esta notacion va a instanciar el objeto
     * que nosotros necesitemos usar es decir estaremos implementado
     * la inyeccion de dependencias.
* */

/* para que el cliente pueda acceder a la informacion de un usuario
 *   debe aceder ala siguiente ruta usuario/id_del usuario
 *  para poder hacer que el id del usuario sea dinamico
 * se lo indicamos entre llaves {id}
 *
 * y en el parametro se debe poner @PathVariable
 * Esta anotación se utiliza para vincular variables de una URL con parámetros de un método del controlador en Spring.
 *--------------------------------------------------------------------------------------
 * El metodo getUsuarios por defecto esta utilizando una peticion get
 * ya que al no colocar method = RequestMethod.tipo de peticion
 * el metodo por defecto hace una peticion GET
 *
 * si queremos utilizar otro tipo de de metodo usamos el codigo anteriormente visto.
 * NOTA : si por ej estamos usando el metodo DELETE esto no significa que va a eliminar el usuario
 * automaticamente, eso depende de nosotros de la logica que ejecutemos en el metodo.
 *
 * Lo unico que sucede es que si entran por la url usando el metodo DELETE
 * van a tener acceso a lo que sea que devuelva la funcion
 * es decir se utiliza mas para diferenciar los metodos.
 *
 * -------------------------------------------------------------------------------------------------
 * Cuando se utiliza @RequestBody, Spring Framework tomará el cuerpo de la solicitud HTTP entrante
 * y lo convertirá automáticamente en un objeto Java, utilizando un convertidor adecuado basado en
 * el tipo de contenido (por ejemplo, JSON, XML, etc.) y la configuración de conversión del proyecto.
 *
 *
La deserialización es el proceso de convertir datos en un formato específico (como JSON, XML, bytes, etc.)
*  en un objeto o estructura de datos en un lenguaje de programación, como Java, C#, Python, etc.
*  Es el proceso inverso de la serialización, que implica convertir un objeto o estructura de datos en un
*  formato específico para que pueda ser transmitido o almacenado de manera eficiente.
 * */

/* haciendo uso de la libreria argon2 vamos a encriptar la contraseña
 *   antes de guardarla en la base de datos.
 *   instanciamos la clase  Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
 *   luego utilizamos el metodo hash para poder encriptar la contraseña
 *
 *      1er parametro - numero de iteraciones, es decir las veces que se repite la encriptacion
 *      cabe destacar que entre mas veces se encripte mas seguridad tiene, la desventaja de esto es que
 *      se vuelve mas lento el proceso de encriptado.
 *
 *      2do parametro  -  se le agrega 1024 esto hace refencia al espacio en memoria que se le otorga
 *
 *      3er parametro - numero de hilos, o tambien llamado paralelismo
 *      esto ayuda en caso de que tengas varias iteraciones ya que le va a otorgar al encriptado
 *      varios hilos es decir si le dices 5 iteraciones le va otorgar un hilo acada uno es esto va hacer que
 *      el proceso de encriptado sea mucho mas rapido.
 * */

/*
Documentacion de el JWT
  con getKey nos devuelve el id del usuario ya que cuando creamos el token ese
 * fue el valor que le pasamos en el primer paramatro
 * si usamos getValue() no retorna el email del usuario ya que ese fue el segundo valor que le
 * pasamos.
 * */

@RestController
public class UsuarioController {

    @Autowired
   UsuarioDao usuarioDao;

    @Autowired
    private JWTUtil jwtUtil;

    /* con la anotacion de requestHeader(value = "Authorizacion) capturamos el token para
        cargarlo en el parametro token.
    "*/
    @RequestMapping(path = "api/usuarios")
    public List<Usuario> getUsuarios(@RequestHeader(value = "Authorization") String token){

        if(!validarToken(token)){
            return null;
        }
        return usuarioDao.getUsuarios();
    }

//    metodo que valida el token
    public boolean validarToken(String token){
        String idusuario = jwtUtil.getKey(token);

        return idusuario !=null;
    }

    @RequestMapping(path = "api/usuarios", method = RequestMethod.POST)
    public void RegistarUsuarios( @RequestBody  Usuario user){
//        encriptacion de contraseña
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        String hash = argon2.hash(1,1024,1,user.getPassword());
        user.setPassword(hash);
        usuarioDao.registrar(user);
    }

  @RequestMapping(path = "api/usuarios/{id}", method = RequestMethod.DELETE)
  public void eliminar(@PathVariable Long id , @RequestHeader(value = "Authorization") String token){
      // si el usuario tiene una sesion tiene permiso para eliminar un usuario.
        if(validarToken(token)){
          usuarioDao.eliminar(id);
      }
  }
}
