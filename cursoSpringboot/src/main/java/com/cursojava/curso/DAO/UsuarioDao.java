package com.cursojava.curso.DAO;

import com.cursojava.curso.models.Usuario;


import java.util.List;

public interface UsuarioDao {

    List<Usuario> getUsuarios();

    void eliminar(Long id);

    void registrar(Usuario user);

    Usuario obtenerUsersesion(Usuario user);
}
