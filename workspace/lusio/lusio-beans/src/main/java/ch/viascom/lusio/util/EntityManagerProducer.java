package ch.viascom.lusio.util;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

@ApplicationScoped
public class EntityManagerProducer {

    @PersistenceUnit
    private EntityManagerFactory emf;

    @Produces
    public EntityManager create(){
        return emf.createEntityManager();
    }

    public void close(@Disposes EntityManager em) {
        em.close();
    }
}

