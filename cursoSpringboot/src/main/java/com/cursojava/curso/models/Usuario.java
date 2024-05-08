package com.cursojava.curso.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/* con @Table(name = "nombre de la tabla")
*   le indicamos a cual tabla debe ir
*
* Al marcar una clase con @Entity, estás indicando que deseas
* que los objetos de esta clase se almacenen en la base de datos
* y se gestionen mediante JPA. JPA proporciona un mapeo objeto-relacional (ORM)
* que mapea las propiedades de la clase a las columnas de la tabla en la base de datos.
*
* Hibernate es un framework de mapeo objeto-relacional (ORM) para Java. En el contexto de Spring,
* Hibernate se utiliza para interactuar con bases de datos relacionales de manera más sencilla y orientada a objetos.
* Hibernate combina tanto la implementación de JPA como sus propias características adicionales que van más allá de las especificaciones de JPA.
*  Por lo tanto, al trabajar con Hibernate en un entorno de Spring, se puede utilizar tanto la API de JPA estándar como las características específicas
*  de Hibernate. En resumen, Hibernate puede combinarse con la biblioteca de JPA para proporcionar una solución más completa y potente para el mapeo objeto-relacional
* en aplicaciones Java.
*
* en esta clase tambien usamos una libreria llamada Lombok
* que permite utilizar anotaciones para simplificar el codigo de los
* getters setter. asegurate de tener le plugin en tu editor
* para que funcione correctamente.
* anexo el codigo de la dependencia
*        <dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<version>1.18.30</version>
			<scope>provided</scope>
		</dependency>
*
* con @Column(name = "nombre del atributo de la base de datos");
* con esto indicamos la relacion del atributo con la
* columna en la base de datos.
*
* NOTA : ala hora de guardar el usuario en la base de datos
* tuvimos un error, y es que le meotodo merge como es para agregar y actualizar
* entidades esta teniendo un conflicto con el id
* por tanto agregando la notacion  @GeneratedValue(strategy = GenerationType.IDENTITY)
* solucionamos este conflicto.
* */

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id // con esto le indicamos que esta es la clave primaria.
    @Setter @Getter @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter @Getter  @Column(name = "nombre")
    private String nombre;
    @Setter @Getter @Column(name = "apellido")
    private String apellido;
    @Setter @Getter @Column(name = "telefono")
    private String telefono;
    @Setter @Getter @Column(name = "email")
    private String email;
    @Setter @Getter @Column(name = "password")
    private String password;

    public Usuario( String nombre, String apellido, String telefono, String email, String pass) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
        this.password = pass;
    }

    /* para que hibernate puede traer a los usuarios de la base de datos
    *   necesita un constructor vacio para poder instanciarlos.
    * */
    public Usuario(){

    }
}


