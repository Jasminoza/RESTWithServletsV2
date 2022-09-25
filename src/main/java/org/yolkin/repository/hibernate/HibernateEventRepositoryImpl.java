package org.yolkin.repository.hibernate;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.yolkin.model.EventEntity;
import org.yolkin.repository.EventRepository;
import org.yolkin.util.HibernateSessionFactoryUtil;

import java.util.List;

public class HibernateEventRepositoryImpl implements EventRepository {
    @Override
    public List<EventEntity> getAll() {
        try (Session session = getSession()) {
            return session.createQuery("select e From EventEntity e left join fetch e.user left join fetch e.file", EventEntity.class).list();
        }
    }

    @Override
    public EventEntity create(EventEntity eventEntity) {
        return saveEventToDB(eventEntity);
    }

    @Override
    public EventEntity getById(Long id) {
        try (Session session = getSession()) {
            List<EventEntity> eventEntities = session.createQuery(
                    "select e From EventEntity as e join fetch e.user join fetch e.file where e.id=" + id,
                    EventEntity.class
            ).list();

            return ((eventEntities.size() != 0) ? eventEntities.get(0) : null);
        }
    }

    @Override
    public EventEntity update(EventEntity eventEntity) {
        return saveEventToDB(eventEntity);
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

    private EventEntity saveEventToDB(EventEntity eventEntity) {
        Transaction transaction = null;

        try (Session session = getSession()) {
            transaction = session.beginTransaction();
//            event = session.merge(event);

            session.createNativeQuery(
                    String.format(
                            "INSERT INTO events " +
                                    "(date, user_id, event_type, file_id) VALUES (now(), %s, %s, %s)",
                            eventEntity.getUser().getId(), eventEntity.getEventType(), eventEntity.getFile().getId()),
                    EventEntity.class).executeUpdate();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }

        return ((eventEntity.getId() != null) ? eventEntity : null);
    }

    private Session getSession() {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession();
    }
}