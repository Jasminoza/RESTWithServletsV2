package org.yolkin.repository.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.yolkin.model.Event;
import org.yolkin.repository.EventRepository;
import org.yolkin.util.HibernateSessionFactoryUtil;

import java.util.List;

public class HibernateEventRepositoryImpl implements EventRepository {
    @Override
    public List<Event> getAll() {
        try (Session session = getSession()) {
            return session.createQuery(
                    "select e From Event as e " +
                            " OUTER join fetch e.file " +
                            " OUTER join fetch e.user",
                    Event.class
            ).list();
        }
    }

    @Override
    public Event create(Event event) {
        return saveEventToDB(event);
    }

    @Override
    public Event getById(Long id) {
        try (Session session = getSession()) {
            return session.createQuery(
                    "select e From Event as e " +
                            " OUTER join fetch e.file and " +
                            " OUTER join fetch e.user where e.id=" + id,
                    Event.class
            ).list().get(0);
        }
    }

    @Override
    public Event update(Event event) {
        return saveEventToDB(event);
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = null;

        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            session.remove(getById(id));
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    private Event saveEventToDB(Event event) {
        Transaction transaction = null;

        try (Session session = getSession()) {
            transaction = session.beginTransaction();
            event = session.merge(event);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }

        return ((event.getId() != null) ? event : null);
    }

    private Session getSession() {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession();
    }
}