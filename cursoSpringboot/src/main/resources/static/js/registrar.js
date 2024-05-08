
// Call the dataTables jQuery plugin
$(document).ready(function() {
    // on ready
});

/* Para esta Request utilizamos como Referencia el metodo 
    POST -  ya que esta se utiliza para para poder agregar 
    o insertar una nueva entidad en la base de datos.
     
    El método json() no es una función de la clase JSON en JavaScript, sino un método disponible 
    en el objeto Response proporcionado por la API Fetch para manejar respuestas HTTP. 
    Este método es utilizado para extraer el cuerpo de una respuesta HTTP como un objeto JavaScript deserializado.
    Cuando realizas una solicitud HTTP con Fetch API, obtienes un objeto Response. 
    Si la respuesta tiene un cuerpo en formato JSON, puedes usar el método json() 
    para leer ese cuerpo y devolver una promesa que resuelve con los datos deserializados 
    como un objeto JavaScript.

    La forma en la cual se envian los datos del usuario que se esta registrando es 
    por medio el metadato del body, hacemos uso el objeto JSON y el metodo stringify
    para poder convertir el objeto datos en una cadena de texto en formato json
    para poder mandar estos datos al backend
*/

/* ahora ya solo falta agregarle el evento a el boton registrar */
const boton = document.getElementById('boton').addEventListener('click',()=>{
    RegistrarUsuarios();
});

async function RegistrarUsuarios() {

    let datos = {};
    datos.nombre = document.getElementById('txtnombre').value;
    datos.apellido = document.getElementById('txtapellido').value;
    datos.email = document.getElementById('txtemail').value;
    datos.password = document.getElementById('txtpassword').value;

    let repetirpass = document.getElementById('txtpasswordRepeat').value;

    // verifcamos que las contraseñas sean iguales.
    if(repetirpass != datos.password){
        
        alert('contraseñas diferentes !!');
        // limpiamos el password usando value
        document.getElementById('txtpassword').value = '';
        document.getElementById('txtpasswordRepeat').value= '';

    }else{

        const request = await fetch('api/usuarios', {
            method: 'POST',
            headers: {
              'Accept' : 'application/json',
              'Content-Type' : 'application/json'
            },
        
            body : JSON.stringify(datos)
          });

          alert('Se ha registrado correctamente !');
        //   redirigimos al usuario a el login.
          location.href = 'login.html';
        }
    }

 
