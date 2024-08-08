
const boton = document.getElementById('btn');

document.addEventListener(('DOMContentLoaded'),()=>{

    boton.addEventListener(('click'),()=>{
        iniciarsesion();
    });
});


const iniciarsesion = async ()=>{

    let datos = {};
    datos.email = document.getElementById('email').value;
    datos.pass = document.getElementById('pass').value;

    // verificamos que los inputs no esten vacios
    if(datos.email == "" || datos.pass == ""){
        alert('Por favor verifique que los campos no esten vacios!');
    }else{

        // realizamos la peticion http al backend para loguear el usuario
        const request = await fetch('api/login',{
            method: 'POST',
            headers: {
                'Accept' : 'application/json',
                'Content-Type' : 'application/json'
              },
            body: JSON.stringify(datos)
        });

        // Luego capturamos la respuesta y verificamos que el usuario se haya logueado
        const respuesta = await request.text();

        if(respuesta != "fail"){
            localStorage.token = respuesta;
            localStorage.email = datos.email; 
            window.location.href = 'index.html';
        }else{
            alert('las credenciales son incorrectas, por favor intente nuevamente');
        }
    }
}