// Call the dataTables jQuery plugin
$(document).ready(function() {
    // on ready
});

/* ahora ya solo falta agregarle el evento a el boton login */
const boton = document.getElementById('boton-iniciarsesion').addEventListener('click',()=>{
    iniciarsesion();
});

async function iniciarsesion() {

    let datos = {};
    datos.email = document.getElementById('txtemail').value;
    datos.password = document.getElementById('txtpassword').value;

    const request = await fetch('api/login', {
        method: 'POST',
        headers: {
          'Accept' : 'application/json',
          'Content-Type' : 'application/json'
        },
    
        body : JSON.stringify(datos)
      });

     /* como desde el backend nos estan enviando solo un texto 
        es decir la respuesta no es un json.
        utilizamos text(); para capturar la respuesta.

        para poder redirigir al usuario utilizamos la 
        API de location y el metodo href

        una vez generado el JWT en el backend se resive en la variable 
        respuesta para luego guradar la informacion en el localStorage
     */
      const respuesta = await request.text();

      if(respuesta != 'fail'){
         localStorage.token = respuesta;
         localStorage.email = datos.email; 
          window.location.href = 'usuarios.html';
      }else{
        alert('las credenciales son incorrectas, por favor intente nuevamente');
      }

    }
