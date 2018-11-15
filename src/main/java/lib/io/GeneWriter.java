package lib.io;

import com.fasterxml.classmate.AnnotationConfiguration;
import lib.hlt.Log;
import lib.models.genes.BotGenes;
import lib.models.genes.ShipGenes;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GeneWriter {

    public GeneWriter(){}

    public void writeGenes(BotGenes botGenes, ShipGenes shipGenes) {
        SessionFactory sessionFactory = new Configuration()
                .configure(new File("hibernate.cgf.xml"))
                .addAnnotatedClass(BotGenes.class)
                .addAnnotatedClass(ShipGenes.class)
                .buildSessionFactory();
        Session session = sessionFactory.openSession();
        writeBotGenes(botGenes, session);
        writeShipGenes(shipGenes, session);
        session.close();
    }

    private void writeBotGenes(BotGenes botGenes, Session session) {
        session.beginTransaction();
        session.save(botGenes);
        session.getTransaction().commit();
    }

    private void writeShipGenes(ShipGenes shipGenes, Session session) {
        session.beginTransaction();
        session.save(shipGenes);
        session.getTransaction().commit();
    }
}
