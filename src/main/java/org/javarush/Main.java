package org.javarush;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.javarush.dao.*;
import org.javarush.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {
    private final SessionFactory sessionFactory;

    private final ActorDAO actorDAO;
    private final AddressDAO addressDAO;
    private final CategoryDAO categoryDAO;
    private final CityDAO cityDAO;
    private final CountryDAO countryDAO;
    private final CustomerDAO customerDAO;
    private final FilmDAO filmDAO;
    private final FilmTextDAO filmTextDAO;
    private final InventoryDAO inventoryDAO;
    private final LanguageDAO languageDAO;
    private final PaymentDAO paymentDAO;
    private final RentalDAO rentalDAO;
    private final StaffDAO staffDAO;
    private final StoreDAO storeDAO;



    public Main(){

        sessionFactory = MySessionFactory.getSessionFactory();

        actorDAO = new ActorDAO(sessionFactory);
        addressDAO = new AddressDAO(sessionFactory);
        categoryDAO = new CategoryDAO(sessionFactory);
        cityDAO = new CityDAO(sessionFactory);
        countryDAO = new CountryDAO(sessionFactory);
        customerDAO = new CustomerDAO(sessionFactory);
        filmDAO = new FilmDAO(sessionFactory);
        filmTextDAO = new FilmTextDAO(sessionFactory);
        inventoryDAO = new InventoryDAO(sessionFactory);
        languageDAO = new LanguageDAO(sessionFactory);
        paymentDAO = new PaymentDAO(sessionFactory);
        rentalDAO = new RentalDAO(sessionFactory);
        staffDAO = new StaffDAO(sessionFactory);
        storeDAO = new StoreDAO(sessionFactory);
    }
    public static void main(String[] args) {

        Main main = new Main();

        Customer customer = main.createCustomer();

        main.customerReturnInventoryToStore();

        main.customerRentInventory(customer);

        main.newFilmWasMade();
    }

    private void newFilmWasMade() {
        Session session = sessionFactory.getCurrentSession();
        try(session) {
            session.beginTransaction();

            Language language = languageDAO.getItems(0, 20).stream().unordered().findAny().get();
            List<Category> categories = categoryDAO.getItems(0, 5);
            List<Actor> actors = actorDAO.getItems(0, 20);

            Film film = new Film();
            film.setActorSet(new HashSet<>(actors));
            film.setRating(Rating.NC17);
            film.setSpecialFeatures(Set.of(Feature.TRAILERS, Feature.COMMENTARIES));
            film.setLength((short) 456);
            film.setReplacementCost(BigDecimal.TEN);
            film.setRentalRate((BigDecimal.ZERO));
            film.setLanquage(language);
            film.setDescription("new great film");
            film.setTitle("scary my movie");
            film.setRentalDuration((byte) 44);
            film.setOriginalLanguage(language);
            film.setCategorySet(new HashSet<>(categories));
            film.setYear(Year.now());
            filmDAO.save(film);

            FilmText filmText = new FilmText();
            filmText.setFilm(film);
            filmText.setId(film.getId());
            filmText.setDescription("new great film");
            filmText.setTitle("scary my movie");
            filmTextDAO.save(filmText);

            session.getTransaction().commit();
        }
    }

    private void customerRentInventory(Customer customer) {
        Session session = sessionFactory.getCurrentSession();
        try(session) {
            session.beginTransaction();

            Film film = filmDAO.getFirstAvailableFilmForRent();
            Store store = storeDAO.getItems(0, 1).get(0);

            Inventory inventory = new Inventory();
            inventory.setFilm(film);
            inventory.setStore(store);
            inventoryDAO.save(inventory);

            Staff staff = store.getStaff();

            Rental rental = new Rental();
            rental.setRentalDate(LocalDateTime.now());
            rental.setCustomer(customer);
            rental.setInventory(inventory);
            rental.setStaff(staff);
            rentalDAO.save(rental);

             Payment payment = new Payment();
             payment.setRental(rental);
             payment.setPaymentDate(LocalDateTime.now());
             payment.setCustomer(customer);
             payment.setAmount(BigDecimal.valueOf(48.25));
             payment.setStaff(staff);
             paymentDAO.save(payment);

            session.getTransaction().commit();
        }
    }

    private void customerReturnInventoryToStore() {
        Session session = sessionFactory.getCurrentSession();
        try(session) {
            session.beginTransaction();

            Rental rental = rentalDAO.getAnyUnreturnedRental();
            rental.setReturnDate(LocalDateTime.now());

            rentalDAO.save(rental);

            session.getTransaction().commit();
        }
    }

    private Customer createCustomer() {
        Session session = sessionFactory.getCurrentSession();
        try(session) {
            session.beginTransaction();
            Store store = storeDAO.getItems(0, 1).get(0);

            City city = cityDAO.getByName("Aden");

            Address address = new Address();
            address.setAddress("Builder str, 48");
            address.setPhone("999-222-111");
            address.setCity(city);
            address.setDistrict("Aden");
            addressDAO.save(address);

            Customer customer = new Customer();
            customer.setActive(true);
            customer.setEmail("myname@gmail.com");
            customer.setAddress(address);
            customer.setStore(store);
            customer.setFirstName("Vasiliy");
            customer.setLastName("Petrovich");
            customerDAO.save(customer);

            session.getTransaction().commit();
            return customer;
        }
    }
}