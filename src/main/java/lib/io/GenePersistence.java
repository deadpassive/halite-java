package lib.io;

import lib.hlt.Log;
import lib.models.genes.BotGenes;
import lib.models.genes.ShipGenes;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.io.File;

public class GenePersistence {

    public void writeBotGenes(BotGenes botGenes) {
        SessionFactory sessionFactory= new Configuration()
                .configure(new File("hibernate.cgf.xml"))
                //.addAnnotatedClass(BotGenes.class)
                .buildSessionFactory();
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(botGenes);
            transaction.commit();
        } catch (HibernateException ex) {
            if (transaction != null)
                transaction.rollback();
        }
        sessionFactory.close();

    }

    public void writeShipGenes(ShipGenes shipGenes) {
        SessionFactory sessionFactory = new Configuration()
                .configure(new File("hibernate.cgf.xml"))
                //.addAnnotatedClass(ShipGenes.class)
                .buildSessionFactory();
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(shipGenes);
            transaction.commit();
        } catch (HibernateException ex) {
            if (transaction != null)
                transaction.rollback();
        }
        sessionFactory.close();
    }
}
