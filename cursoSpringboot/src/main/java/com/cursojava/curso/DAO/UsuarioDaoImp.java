package com.cursojava.curso.DAO;

import com.cursojava.curso.models.Usuario;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/* para poder utilizar la anotation Transactional
*   me toco agregar la siguiente dependencia al proyecto
*        <dependency>
			<groupId>org.springframework</groupId>
			<artifactId> spring-tx </artifactId>
			<version> 6.1.3 </version> -- aqui se indica la version de Spring que se esta usando
		</dependency>
		*
		* La anotación @PersistenceContext se utiliza en aplicaciones Java EE (Enterprise Edition) para inyectar un EntityManager en un bean de la aplicación.
          El EntityManager es una interfaz de JPA (Java Persistence API) que proporciona métodos para interactuar con la base de datos en el contexto de persistencia de JPA.
          * Permite realizar operaciones CRUD (crear, leer, actualizar, eliminar) en entidades de la base de datos, así como consultas JPQL (Java Persistence Query Language) para recuperar datos.
          Cuando se usa @PersistenceContext, el contenedor de la aplicación (como un servidor de aplicaciones Java EE o un contenedor de Spring) maneja la creación y gestión del EntityManager,
           lo que permite que el desarrollador no tenga que preocuparse por la creación y administración manual del EntityManager. Esto simplifica el código y mejora la legibilidad y mantenibilidad de la aplicación.
          Por ejemplo, en un servlet o un bean de un framework como Spring, puedes usar @PersistenceContext para inyectar un EntityManager como este:

         @Repository: Esta anotación se utiliza para indicar que una clase cumple la función de un repositorio de datos en una aplicación Spring.
         * Un repositorio es una abstracción de acceso a datos que proporciona métodos para realizar operaciones de lectura, escritura, eliminación y búsqueda
         *  en una fuente de datos, como una base de datos. Usualmente, se utiliza en clases que interactúan con la capa de persistencia de la aplicación, como clases de acceso a datos
         * (Data Access Objects - DAOs). La anotación @Repository se utiliza para marcar estas clases y permite a Spring detectarlas automáticamente durante la configuración de la aplicación.

        @Transactional: Esta anotación se utiliza para definir los límites de transacción para un método o clase en una aplicación Spring.
        * Al marcar un método o clase con @Transactional, Spring gestionará automáticamente las transacciones para ese método o clase.
        * Esto significa que se iniciará una transacción antes de que se ejecute el método anotado y se comprometerá (o revertirá en caso de error)
        * la transacción al finalizar la ejecución del método. Esto es útil para garantizar la integridad de los datos en la base de datos,
        *  ya que las operaciones dentro de un método anotado con @Transactional se ejecutarán de manera atómica y consistente.
        * La anotación @Transactional se puede aplicar a métodos individuales o a nivel de clase para que todas las llamadas a métodos dentro de esa clase se gestionen dentro de la misma transacción.
* */


@Repository
@Transactional
public class UsuarioDaoImp implements UsuarioDao {

    @PersistenceContext
     EntityManager entityManager;
@Override
    public List<Usuario> getUsuarios() {
        /* realizamos el query que es la solicitud a la base de datos
    pero como estamos usando hibernate
*   se hace con el nombre de la clase

    hay que tener en cuenta que en la clase usuario
    debe estar indicando de cual tabla debe traer los datos.
* */
        String query = "FROM Usuario";
        /*este query debe ser ejecutado por el entityManager
        *   createQuery() - hace una consulta ala base de datos
        *  recibe la consulta por paramatro de tipo de datos
        * String
        *
        * getResultList() - transforma el resultado en una lista
        * de objetos de tipo Usuario
        *
        * por ultimo simplemente le agregamos un return para
        * devolver el resultado.
        * */
         return entityManager.createQuery(query).getResultList();
    }

    /*para eliminar un usuario con Entity manager
*  primero tenemos que buscar el usuario a eliminar en la base de datos
* luego utilizar el metodo remove(); para eliminarlo
*
* metodos de entityManager
* find(); -
* recibe dos parametros
* 1er - la clase del datos a eliminar esto lo indicamos con
* nombre de la clase.class - este metodo devuelve la clase.
*
* 2do - el id del dato a eliminar
*
*  remove()
* - remueve un objeto de la base de datos
* recibe 1 solo parametro el objeto a eliminar.
*
* -----------------------------------------------
* El método merge() es útil en este contexto. Cuando invocas merge() en una entidad no gestionada
* y la entidad asociada ya existe en el contexto de persistencia, JPA "fusiona" los cambios de la entidad
* no gestionada en la entidad gestionada y sincroniza estos cambios con la base de datos. Si la entidad no
* existe en la base de datos, JPA la insertará como una nueva.
*
* este metodo se utiliza para crear y para actualizar entidades.
*
* En resumen cuando deserializamos una entidad es decir la traemos desde el frontend de la aplicacion
* por medio de una solicitud HTPP este metodo puede convertir esa entidad en la instancia de nuestro
* modelo en esta caso un objeto Usuario y agregarlo en la base de datos como uno nuevo.
*
* */
    @Override
    public void eliminar(Long id) {
        Usuario usuario = entityManager.find(Usuario.class,id);
        entityManager.remove(usuario);
    }

    @Override
    public void registrar(Usuario user) {
        entityManager.merge(user);
    }

    /* En este caso hacemos un query para poder comparar
        los datos que esta ingresanso el usuario desde el login
        con los datos guardados en la base de datos
        esto debe retornar un boolean

        CONSEJO DE SEGURIDAD
        para poder hacer la comparacion hay que asignar los valores del objeto
        user a el query la forma tradicional seria agregando "+user.valor+"
        pero esta forma no es segura ya que un hacker podria tomar esto
        y inciar sesion agregando el siguiente codigo
        String email = '' OR 1=1 --'; esto devolveria true siempre
        y podria acceder muy facilmente.

        entonces para evitar esto colocamos :email :password
        y luego se le agrega el valor con la funcion setParameter()
        esta funcion recibe 2 parametros que son el nombre del parametro al
        cual se le va agregar el valor y el valor del parametro.

          List<Usuario> lista =  entityManager.createQuery(query)
                 .setParameter("email",user.getEmail())
                 .setParameter("password",user.getPassword())
                 .getResultList();

          este codigo va a devolver una fila de la base de datos
          en caso de encontrar una coincidencia en los datos

          para poder hacer la verificacion de la contrasena indicada
          debemos usar el metodo de verify()
          1er parametro - la contrasena del usuario consultado en la base de datos
          2do parametro - la contrasena del usuario que esta iniciando sesion
    *
    * */
    @Override
    public Usuario obtenerUsersesion(Usuario user) {
// hacemos la query para verificar el email del usuario.
        String query = "FROM Usuario WHERE email = :email ";
        List<Usuario> lista =  entityManager.createQuery(query)
                 .setParameter("email",user.getEmail())
                 .getResultList();
//        verificamos que la lista este vacia asi evitamos una Exception
        if(lista.isEmpty()){
            return null;
        }
//        obtenemos el usuario consultado y hacemos la verificacion del password
        String passwordhash = lista.get(0).getPassword();
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        if(argon2.verify(passwordhash,user.getPassword())){
            return lista.get(0);
        }
        return null;
    }
}
