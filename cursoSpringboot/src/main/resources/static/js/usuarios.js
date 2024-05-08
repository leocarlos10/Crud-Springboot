// Call the dataTables jQuery plugin
$(document).ready(function() {
    cargarUsuarios();
  $('#usuarios').DataTable();
});

/* con esto le hacemos la peticion ala api que estamos construyendo 
  esta api hace una request ala base de datos devolviendo los usuarios
  en formato json y con js itermaos todos los usuarios
  para agregarlos en la tabla
*/
async function cargarUsuarios() {

  const request = await fetch('api/usuarios', {
    method: 'GET',
    headers: getHeaders()
  });
  // guardo la respuesta de la peticion
  const usuarios = await request.json();

  

  let listadoHtml = '';
  for (let usuario of usuarios) {
    let boton = '<a href="#" onclick = "eliminarusuario('+usuario.id+')" class="btn btn-danger btn-circle btn-sm"><i class="fas fa-trash"></i></a>';
    let telefono = usuario.telefono == null ? '-':usuario.telefono;
    let usuarioHtml = '<tr><td class="sorting_1">'+usuario.id+'</td><td>'+usuario.nombre+'</td><td>'+usuario.email+'</td><td>'+telefono+'</td><td>'+boton+'</td></tr>';

    listadoHtml += usuarioHtml;
  }

document.querySelector('#usuarios tbody').outerHTML = listadoHtml;
}

/* Para poder eliminar el usuario lo que hicimos fue lo siguiente
  1 guardamos el boton eliminar en la variable boton
  2 creamos una funcion flecha eliminarusuario y le agregamos 
  un evento al boton de eliminar con la funcion onclick
  3 como queremos que esto sea dinamico movimos el codigo dentro del for
  por lo que en cada iteracion va a tener el id del usuario 
  y en caso de que se ejecute el evento va a eliminar al usuario 
  correspondiente.
*/

function getHeaders(){
  return {
    'Accept' : 'application/json',
    'Content-Type' : 'application/json',
    'Authorization': localStorage.token
  }
}

const eliminarusuario = async (id)=>{
  
  const request = await fetch('api/usuarios/'+ id, {
    method: 'DELETE',
    headers: getHeaders()
  });

  // con esto actualizamos la pagina.
  location.reload();

}

const txt_email = document.getElementById('email_usuario');

const AgregarEmail_usuarioLogueado = ()=>{

  const email = localStorage.email;
  txt_email.innerHTML = email;
}

AgregarEmail_usuarioLogueado();


