package com.SBAdmin.API_SBAdmin.DAO;

import com.SBAdmin.API_SBAdmin.Modelo.Usuario;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class UsuarioDAO  implements DAO <Usuario>{
    // creamos el EntityManager para realizar la comunicacion con la DB
    @PersistenceContext
    EntityManager entityManager;


    @Override
    public List<Usuario> get(Long id) {
        String query = "FROM Usuario";
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public boolean eliminar(Long id) {
        return false;
    }

    @Override
    public void registrar(Usuario user) {
        entityManager.merge(user);
    }

    @Override
    public Usuario obtener(Usuario user) {

        String query = "FROM Usuario WHERE email = :email ";
        List<Usuario> lista =  entityManager.createQuery(query)
                .setParameter("email",user.getEmail())
                .getResultList();
        // verificamos que la lista no este vacia
        if(!lista.isEmpty()){
            String passwordHash = lista.get(0).getPass();
            Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
            if(argon2.verify(passwordHash,user.getPass())){
                return lista.get(0);
            }
        }
        return null;
    }
        @Override
        public Long obtener(String email){
            String query = "FROM Usuario WHERE email = :email";
            Usuario user = null;
            try {
                 user =  (Usuario) entityManager.createQuery(query).setParameter("email",email).getSingleResult();
            }catch (NoResultException e){
                System.out.println("error"+e);
            }

            if(user != null){
                return user.getId();
            }
            return null ;
        }

    @Override
    public void actualizar(Usuario entidad) {

    }
}
