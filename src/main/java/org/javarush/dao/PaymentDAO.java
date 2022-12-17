package org.javarush.dao;

import org.javarush.domain.Payment;
import org.hibernate.SessionFactory;

public class PaymentDAO extends GenericDAO<Payment> {
    public PaymentDAO(SessionFactory sessionFactory) {
        super(Payment.class, sessionFactory);
    }
}
