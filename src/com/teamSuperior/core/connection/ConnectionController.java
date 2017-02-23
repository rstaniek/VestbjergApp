package com.teamSuperior.core.connection;

import com.teamSuperior.core.model.Model;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;

/**
 * CRUD (create, read, update, delete) controller
 */
public class ConnectionController<T, Id extends Serializable> implements IDataAccessObject<T, Id> {

    private final Class<T> clazz; // To store a generic class type
    private Session currentSession; // To store a MySQL database access session
    private Transaction currentTransaction; // Helper to manage database commits

    public ConnectionController(Class<T> clazz) {
        this.clazz = clazz;
    }

    private Session openCurrentSession() {
        currentSession = getSessionFactory().openSession();
        return currentSession;
    }

    private Session openCurrentSessionWithTransaction() {
        currentSession = getSessionFactory().openSession();
        currentTransaction = currentSession.beginTransaction();
        return currentSession;
    }

    private void closeCurrentSession() {
        currentSession.close();
    }

    private void closeCurrentSessionWithTransaction() {
        currentTransaction.commit();
        currentSession.close();
    }

    private static SessionFactory getSessionFactory() {
        final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    private Session getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
    }

    public Transaction getCurrentTransaction() {
        return currentTransaction;
    }

    public void setCurrentTransaction(Transaction currentTransaction) {
        this.currentTransaction = currentTransaction;
    }

    public void persist(T t) {
        openCurrentSessionWithTransaction();
        getCurrentSession().save(t);
        closeCurrentSessionWithTransaction();
    }

    public T getById(Id id) {
        openCurrentSession();
        @SuppressWarnings("unchecked")
        T t = (T) getCurrentSession().get(clazz, id);
        closeCurrentSession();
        return t;
    }

    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        openCurrentSession();

        CriteriaBuilder builder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<T> criteria = builder.createQuery(clazz);
        criteria.from(clazz);

        List<T> list = getCurrentSession().createQuery(criteria).getResultList();

        closeCurrentSession();

        return list;
    }

    @SuppressWarnings("unchecked")
    public List<T> listByArray(String propertyName, Object[] values) {
        openCurrentSession();
        List<T> list = (List<T>) getCurrentSession().createCriteria(clazz.getName())
                .add(Restrictions.in(propertyName, values))
                .list();
        closeCurrentSession();
        return list;
    }

    @SuppressWarnings("unchecked")
    public List<T> listByParam(String propertyName, Object value) {
        openCurrentSession();
        List<T> list = (List<T>) getCurrentSession().createCriteria(clazz.getName())
                .add(Restrictions.gt(propertyName, value))
                .list();
        closeCurrentSession();
        return list;
    }

    public void update(T t) {
        openCurrentSessionWithTransaction();
        getCurrentSession().update(t);
        closeCurrentSessionWithTransaction();
    }

    public void delete(T t) {
        openCurrentSessionWithTransaction();
        getCurrentSession().delete(t);
        closeCurrentSessionWithTransaction();
    }

    public void deleteAll() {
        openCurrentSessionWithTransaction();
        List<T> people = getAll();
        for (T t : people) {
            delete(t);
        }
        closeCurrentSessionWithTransaction();
    }

    public void printToJson(List<T> l) {

        int size = l.size();
        System.out.println("[");
        for (T i : l) {
            System.out.println(((Model) i).toJson());
            if (--size != 0) {
                System.out.print(",");
            }
        }
        System.out.print("]");
    }
}
