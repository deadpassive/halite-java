package lib.io;

import lib.models.GameDetails;
import lib.models.genes.BotGenes;
import lib.models.genes.ShipGenes;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.logging.Level;

public class GeneWriter {

    public GeneWriter(){}

    public void writeGenes(BotGenes botGenes, ShipGenes shipGenes, GameDetails gameDetails) {
        // Reduce hibernate logging so that stdout is kept clear for halite.exe
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);

        SessionFactory sessionFactory = new Configuration()
                .addAnnotatedClass(BotGenes.class)
                .addAnnotatedClass(ShipGenes.class)
                .addAnnotatedClass(GameDetails.class)
                .buildSessionFactory();
        Session session = sessionFactory.openSession();

        writeBotGenes(botGenes, session);
        writeShipGenes(shipGenes, session);
        writeGameDetails(gameDetails, session);

        session.close();
        sessionFactory.close();
    }

    private void writeGameDetails(GameDetails gameDetails, Session session) {
        session.beginTransaction();
        session.save(gameDetails);
        session.getTransaction().commit();
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
