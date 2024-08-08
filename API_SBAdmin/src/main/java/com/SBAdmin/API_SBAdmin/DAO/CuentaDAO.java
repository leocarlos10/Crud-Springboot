package com.SBAdmin.API_SBAdmin.DAO;

import com.SBAdmin.API_SBAdmin.Modelo.Cuenta;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class CuentaDAO implements DAO <Cuenta>{

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Cuenta> get(Long id) {
        String query = " FROM Cuenta WHERE id_usuario = :id";
        return entityManager.createQuery(query).setParameter("id",id).getResultList();
    }

    @Override
    public boolean eliminar(Long id) {
        Cuenta cuenta = entityManager.find(Cuenta.class, id);
        if(cuenta != null){
            entityManager.remove(cuenta);
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void registrar(Cuenta cuenta) {
        entityManager.merge(cuenta);
    }

    @Override
    public Cuenta obtener(Cuenta cuenta) {
        return null;
    }

    @Override
    public Long obtener(String atributo) {
        return null;
    }

    @Override
    public void actualizar(Cuenta entidad) {
        Cuenta cuenta = entityManager.find(Cuenta.class,entidad.getId());
        if (cuenta != null){
            cuenta.setNombre_usuario(entidad.getNombre_usuario());
            cuenta.setCuenta(entidad.getCuenta());
            cuenta.setPass(entidad.getPass());
            entityManager.merge(cuenta);
        }else{
            System.out.println("La cuenta no se actualizo");
        }
    }
}
