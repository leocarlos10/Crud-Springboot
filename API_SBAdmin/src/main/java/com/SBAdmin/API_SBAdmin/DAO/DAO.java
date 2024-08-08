package com.SBAdmin.API_SBAdmin.DAO;

import com.SBAdmin.API_SBAdmin.Modelo.Cuenta;
import com.SBAdmin.API_SBAdmin.Modelo.Usuario;

import java.util.List;

public interface DAO <T>{

    List<T> get(Long id);

    boolean eliminar(Long id);

    void registrar(T entidad);

    T obtener(T entidad);

    Long obtener(String atributo);

    void actualizar(T entidad);

}
