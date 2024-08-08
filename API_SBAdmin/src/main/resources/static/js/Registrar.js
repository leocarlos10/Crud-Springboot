
// agregamos el evento al boton para registrar el usuario

const boton = document.getElementById('btn-Registrarse');

document.addEventListener(('DOMContentLoaded'),()=>{

    boton.addEventListener(('click'),()=>{
        RegistrarUsuario();
    });
});

const RegistrarUsuario = async ()=>{
    // guardamos los datos del usuario en un objeto
    let datos = {};
    datos.nombre = document.getElementById('nombre').value;
    datos.email = document.getElementById('email').value;
    datos.telefono = document.getElementById('telefono').value;
    datos.pass = document.getElementById('pass').value;

    if(datos.nombre == "" || datos.email == "" || datos.telefono == "" || datos.pass == ""){
        alert('por favor revise que ningun campo este vacio');
    } else{
        // luego realiamos una request utilizando fetch para mandar los datos al backend
    /*
        el fetch lleva la ruta, un objeto indicando el metodo HTTP, los headers indicando 
        que se va a comunicar en JSON
        y el body que haciendo uso de JSON.Stringfy(datos);
        convierte el objeto javascript a tipo json para que los resiva el backend
    */
    const request = await fetch('api/registrar',{
        method: 'POST',
        headers: {
            'Accept' : 'application/json',
            'Content-Type' : 'application/json'
          },
          body: JSON.stringify(datos)
    });
    
    alert('Se ha Registrado correctamente!');
    location.href = 'iniciarsesion.html';

    }
}