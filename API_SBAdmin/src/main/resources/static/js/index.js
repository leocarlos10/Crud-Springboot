
// creando el maquetamiento de las ventadas dinamicas
// funcion para devolver los headers de las solicitudes
const getHeaders = ()=>{
    return {
        'Accept' : 'application/json',
        'Content-Type' : 'application/json',
        'Authorization': localStorage.token
    };
}


// section para mostrar los datos de la cuentas de los usuarios en una tabla.
document.addEventListener('DOMContentLoaded', (event) => {

    const tabla_usuarios = document.getElementById('tabla-usuarios');
    tabla_usuarios.addEventListener(('click'),() => {
       const contenedor = document.getElementById('contenido-main');
       contenedor.innerHTML = `
       <div class="contenedor1">
                <h1>Gestión de Usuarios</h1>
                <div class="contenedor-tabla">
                    <div class="info">
                        <h5>Listado de usuarios</h5>
                    </div>
                    <div class="contenedor-tabla-usuarios">
                        <table class="tabla-user" id="tabla-user"  >
                            <thead class="tabla-head">
                                <tr>
                                    <th class="head">ID</th>
                                    <th class="head">Nombre completo</th>
                                    <th class="head">Cuenta</th>
                                    <th class="head">Contraseña</th>
                                    <th class="head">Acciones</th>
                                </tr>
                            </thead>
                            <tbody class="tabla-body" id="tabla-body">
                                <tr>
                                    <td colspan = "5" class="info2"> No hay Informacion disponible</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>`;

        // Realizamos la solicitud al backend para traer todas las cuentas de la base de datos
        const CargarCuentas = async ()=>{

            const respuesta = await fetch('api/cuentas/'+localStorage.email,{
                method : 'GET',
                headers: getHeaders(),
            });

            // manejamos la respuesta para mostrar las cuentas en la tabla
            const cuentas = await respuesta.json();
            let info_estatus =  document.querySelector('.info2');

            // verificamos el codigo de estado de la respuesta
            if(respuesta.status == 404){
                info_estatus.innerHTML = 'El recurso no ha sido encontrado';
                // redirecionamos al usuario ala pagina de inicio de sesion despues de 4 segundos
                setTimeout(()=>{ location.href = 'iniciarsesion.html'},4000);

            }else if(!respuesta.ok){
                // en caso de recivir false quiere decir que el usuario no ha iniciado sesion
               info_estatus.innerHTML = 'Por favor inicie sesion para cargar esta informacion!';
            } else{

                console.log(cuentas)

                let listado='';
                /* Con Object.entries(cuentas).length == 0 comparamos si la respuesta que nos da
                 el backend contiene cuentas si esta vacia simplemente remplazamos en la tabla 
                 que no hay informacion disponible. */
                 
                if(!Object.entries(cuentas).length == 0){

                    for(let c of cuentas){
                    
                        const boton_eliminar = `
                            <a href="#" class="btn_eliminar" data-id="${c.id}" >
                            <span class="material-symbols-outlined ">delete</span>
                            </a>`;
    
                        const boton_editar = `
                            <a href="#" class="btn_editar" data-id="${c.id}" >
                                <span class="material-symbols-outlined">edit</span>
                            </a>`;
    
                        // cargamos los datos en el html de la tabla
                        let cuentasHTML = `
                            <tr>
                                <td>${c.id}</td>
                                <td>${c.nombre_usuario}</td>
                                <td>${c.cuenta}</td>
                                <td>${c.pass}</td>
                                <td class="acciones">
                                    ${boton_eliminar}
                                    ${boton_editar}
                                </td>
                            </tr>`;
                        
                        listado+=cuentasHTML;
    
                    }
                }else{

                    listado = `
                        <tr>
                            <td colspan = "5"> No hay Informacion disponible</td>
                        </tr>`;
                }

                // por ultimo cargamos todas la cuentas en la tabla
                const tabla = document.getElementById('tabla-body');
                tabla.innerHTML = listado;  
            }

            // agregamos el codigo para elminar una cuenta
            document.querySelectorAll('.btn_eliminar').forEach(btn =>{

                btn.addEventListener('click', async(e)=>{

                    e.preventDefault(); // evitamos el comportamiento por defecto del enlace
                    const id = btn.getAttribute('data-id');
                    // realizamos la request al backen para eliminar la cuenta
                    const respuesta = await fetch(`api/cuentas/${id}`,{
                        method: 'DELETE',
                        headers : getHeaders()
                    });

                    const messague = await respuesta.json();  
                    console.log(messague);

                    if(respuesta.status === 200){
                        alert(messague.mensaje);
                        CargarCuentas();
                    } else if(respuesta.status === 404){
                        alert(messague.mensaje);
                    } else if(respuesta.status === 401){
                        alert(messague.mensaje);
                    } else if(!respuesta.ok){
                        console.log(respuesta);
                    }
                });
            });
            
            // agregamos el codigo para el boton de editar
                // primero obtenemos los elementos necesarios
                const modal = document.getElementById('edit-modal');
                const btnCloseModal = document.querySelector('.close-btn');
                const editButtons = document.querySelectorAll('.btn_editar');
                console.log(modal,btnCloseModal,editButtons);

                // agregamos un evento de click para cada uno de los botones editar
                editButtons.forEach(btn =>{

                    btn.addEventListener('click',()=>{
                        const id = btn.getAttribute('data-id');
                        OpenModal(id);
                    });
                });

                const OpenModal = (id)=>{
                    modal.style.display = "block";
                    let edit_cuenta = {};

                    // recorremos la respuesta con un foreach 
                    for( let cuenta of cuentas){
                        if(cuenta.id == id){
                            edit_cuenta = cuenta;
                        }

                        // luego llenamos los inputs con los datos dela cuenta;
                        document.getElementById('nombre').value = edit_cuenta.nombre_usuario;
                        document.getElementById('cuenta').value = edit_cuenta.cuenta;
                        document.getElementById('contraseña').value = edit_cuenta.pass;
                    }

                    document.getElementById('btn_guardar_cambios').addEventListener('click',(e)=>{
                        e.preventDefault();
                        actualizarCuentas(edit_cuenta.id);
                    });
                }

                const CloseModal = ()=>{
                    modal.style.display = "none";
                }

                document.addEventListener("click", (event)=>{
                    if (event.target == modal) {
                        CloseModal();
                    }
                });

                btnCloseModal.addEventListener('click',()=>{
                    CloseModal();
                });

                // codigo para el mostrar y ocultar contraseña
                const pass = document.getElementById('contraseña');
                const btn_mostrar_ocultar = document.getElementById('btn_mostrar');

                btn_mostrar_ocultar.addEventListener('click', ()=>{
                    // aqui contralamos el cambio de password a text
                    if(pass.type === 'password'){
                        pass.type = 'text';
                        btn_mostrar_ocultar.innerHTML = `<span class="material-symbols-outlined">visibility_off</span>`;
                    }else if(pass.type === 'text'){
                        pass.type = 'password';
                        btn_mostrar_ocultar.innerHTML = `<span class="material-symbols-outlined">visibility</span>`;
                    }
                });
        }

         // ejecutamos el metodo
         CargarCuentas();

         // este el metodo encargado para actualizar las cuentas
        const actualizarCuentas = async (id)=>{
            
            let datos = {};
            datos.id = id;
            datos.nombre_usuario =  document.getElementById('nombre').value;
            datos.cuenta = document.getElementById('cuenta').value;
            datos.pass = document.getElementById('contraseña').value;

            const request = await fetch('api/cuentas/actualizar',{
                method: 'PUT',
                headers : getHeaders(),
                body: JSON.stringify(datos)
            });

            const mensaje = await request.json();
            console.log(mensaje);

            if(request.status == 200){
                /* si recibimos un codigo 200 quiere decir que si 
                se actualizo la cuenta en ese caso limpiamos el formulario 
                cerramos el modal y actualizamos las cuentas */
                const form = document.getElementById('edit-form');
                form.reset();
                const modal = document.getElementById('edit-modal');
                modal.style.display = "none";
                CargarCuentas();
                console.log(mensaje.mensaje);
            }else if(request.status == 401){
                alert(mensaje.mensaje);
            } else if(request.status == 500){
                alert(mensaje.mensaje);
            } else if(!request.ok){
                console.log(request.ok);
            }
        }
    });
});

// section para mostrar el contendio que registra una cuenta de un usuario
document.addEventListener('DOMContentLoaded', ()=>{

   const btn_ingresar = document.getElementById('Ingresar-user');
   
   btn_ingresar.addEventListener('click',()=>{
        const contenedor = document.getElementById('contenido-main');
        contenedor.innerHTML = `<div class="contenedor-base">
                    <div class="contenedor-colum-base">
                        <div class="card">
                            <div class="card-body">
                               <div class="contenedor-info-usuario">
                                <div class="titulo">
                                    <h1>Registra tu cuenta!</h1>
                                </div>
                                <form class="formulario" id="formulario-cuenta">
                                    <div class="form-group">
                                        <input type="text"  placeholder="Nombre completo" autofocus id="nombre_usuario">
                                        <input  type="text" required  placeholder="Cuenta" id="cuenta">
                                        <input type="password"  placeholder="Contraseña" id="pass">
                                    </div>
                                    <button class="btn" id="btn_ingresar">Ingresar</button>
                                    <div class="separetor"></div>
                                </form>
                               </div>
                            </div>
                        </div>
                    </div>
                </div>`;

        // agregamos el evento al boton para la hora de guardar la cuenta
        const boton = document.getElementById('btn_ingresar')
        boton.addEventListener('click',(e)=>{
            e.preventDefault(); // con esto prevenimos que se recargue la pagina
            // ejecutamos el metodo que guarda la cuenta en la DB
            guardar_cuenta();
            const formulario = document.getElementById('formulario-cuenta');
            formulario.reset();
        });

        const guardar_cuenta = async ()=>{
            const formulario = document.forms['formulario-cuenta'];

            let datos ={}
            datos.nombre_usuario = formulario.nombre_usuario.value;
            datos.cuenta = formulario.cuenta.value;
            datos.pass = formulario.pass.value;
            console.log(datos);

            //esto para verificar que el usuario este logueado
            if(!localStorage.token == ""){
                 // validamos los campos
                if(datos.nombre_usuario != "" && datos.cuenta != "" && datos.pass != ""){
                    // relizamos la request
                    const request = await fetch('api/cuentas/'+localStorage.email,{
                        method : 'POST',
                        headers: getHeaders(),
                        body: JSON.stringify(datos)
                    });
                    
                    alert('la cuenta se guardo correctamente');
                }else{
                    alert('por favor verifique que ningun campo este vacio !');
                }
            }else{
                alert(' Por favor Inicie sesion !');
                location.href = 'iniciarsesion.html';
            }
        }        
   });  
});

//---

// eventos para el manejo del index.html//

// evento para el menu desplegable del usuario
document.addEventListener('DOMContentLoaded', (event) => {
    const menu = document.getElementById('img-user');
    const div_menu = document.getElementById('dropdown-menu');
    
    menu.addEventListener(('click'),()=>{
        div_menu.classList.toggle('dropdown-menu-toggle');
        
    });

    /* agregamos un evento al dom para que cuando el usuario
    haga click en cualquier parte fuera de la imagen y de el div_menu
    se cierre
    */
   document.addEventListener(('click'),(e)=>{
    if(!div_menu.contains(e.target) && !menu.contains(e.target)){
        div_menu.classList.add('dropdown-menu-toggle');
    }
   });
});
//---

// codigo para agregar el email en el span del usuario
const txt_email = document.getElementById('email_usuario');
const AgregarEmail_usuarioLogueado = ()=>{

  const email = localStorage.email;
  txt_email.innerHTML = email;
}
AgregarEmail_usuarioLogueado();

//---

// codigo para cerrar la sesion del usuario.
const cerrarsesion = ()=>{
    localStorage.token = '';
    localStorage.email= '';
    txt_email.innerHTML = '';
    location.href = 'index.html';
}

const btn_cerrarSesion = document.getElementById('cerrar-sesion').addEventListener(('click'),()=>{
    cerrarsesion();
});
//---


