package org.javarush.dao;

import org.javarush.domain.Category;
import org.hibernate.SessionFactory;

public class CategoryDAO extends GenericDAO<Category>{
    public CategoryDAO(SessionFactory sessionFactory) {
        super(Category.class, sessionFactory);
    }
}
