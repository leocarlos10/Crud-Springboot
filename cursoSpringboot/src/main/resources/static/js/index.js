
const txt_email = document.getElementById('email_usuario');

 const AgregarEmail_usuarioLogueado = ()=>{

  const email = localStorage.email;
  txt_email.innerHTML = email;
}

AgregarEmail_usuarioLogueado();

const cerrarsesion = ()=>{
    localStorage.token = '';
    localStorage.email= '';
}

const btn_cerrarSesion = document.getElementById('cerrar-sesion').addEventListener('click',()=>{cerrarsesion();});