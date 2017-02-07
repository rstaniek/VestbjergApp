package com.teamSuperior.guiApp.controller;

import com.teamSuperior.core.model.Model;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.io.Serializable;
import java.util.List;

/**
 * CRUD (create, read, update, delete) controller
 */
public class Controller<T, Id extends Serializable> implements DAO<T, Id> {

    private final Class<T> clazz; // To store a generic class type
    private Session currentSession; // To store a MySQL database access session
    private Transaction currentTransaction; // Helper to manage database commits

    Controller(Class<T> clazz) {
        this.clazz = clazz;
    }

    private Session openCurrentSession() {
        currentSession = getSessionFactory().openSession();
        return currentSession;
    }

    private Session openCurrentSessionwithTransaction() {
        currentSession = getSessionFactory().openSession();
        currentTransaction = currentSession.beginTransaction();
        return currentSession;
    }

    private void closeCurrentSession() {
        currentSession.close();
    }

    private void closeCurrentSessionwithTransaction() {
        currentTransaction.commit();
        currentSession.close();
    }

    private static SessionFactory getSessionFactory() {
        Configuration configuration = new Configuration().configure();
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties());
        return configuration.buildSessionFactory(builder.build());
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
        openCurrentSessionwithTransaction();
        getCurrentSession().save(t);
        closeCurrentSessionwithTransaction();
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
        List<T> list = (List<T>) getCurrentSession().createQuery("from " + clazz.getName()).list();
        closeCurrentSession();
        return list;
    }

    public void update(T t) {
        openCurrentSessionwithTransaction();
        getCurrentSession().update(t);
        closeCurrentSessionwithTransaction();
    }

    public void delete(T t) {
        openCurrentSessionwithTransaction();
        getCurrentSession().delete(t);
        closeCurrentSessionwithTransaction();
    }

    public void deleteAll() {
        openCurrentSessionwithTransaction();
        List<T> people = getAll();
        for (T t : people) {
            delete(t);
        }
        closeCurrentSessionwithTransaction();
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
