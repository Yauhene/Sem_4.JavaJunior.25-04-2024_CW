

import org.hibernate.*;
import org.hibernate.cfg.*;

import javax.persistence.*;
import java.sql.*;
import java.util.*;

import static java.sql.DriverManager.getConnection;

public class Main_HW {
    public static void main(String[] args) throws SQLException {

//        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("javax.persistence");
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try (Connection connection = getConnection("jdbc:mysql://localhost:3306/sem_3_sql", "root", "61208619");
             Statement st = connection.createStatement()) {


//      prepareTables(connection);
//            run(connection);
            // Вместо run(connection):
            Configuration configuration = new Configuration().configure();
            try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {
                System.out.println("---------------------------------------------------------hibernateCrud(sessionFactory);");
                hibernateCrud(sessionFactory);
                System.out.println("AFTER---------------------------------------------------------hibernateCrud(sessionFactory);");
                try (Session session = sessionFactory.openSession()) {
                    Group group1 = new Group();
                    Group group2 = new Group();

                    Student student1 = new Student();
                    Student student2 = new Student();


                    Transaction tx = session.beginTransaction();
                    session.persist(group1); // По сути - INSERT
                    session.persist(group2); // По сути - INSERT

                    session.persist(student1);
                    session.persist(student2);
                    tx.commit(); // Подтверждаем изменения
                }
            }
        }

//              EntityTransaction transaction = entityManager.getTransaction();
//              transaction.begin();

//              Group group1 = new Group("java");
//              Group group2 = new Group("test");
//
//              entityManager.persist(group1);
//              entityManager.persist(group2);
//
//              Student student1 = new Student("иван", "иванов", group1);
//              Student student2 = new Student("олег", "попов", group2);
//
//              entityManager.persist(student1);
//              entityManager.persist(student2);
//
//              transaction.commit();
//
//              entityManager.close();
//              entityManagerFactory.close();
    }

    private static void hibernateCrud(SessionFactory sessionFactory) {
        User user = new User();
        user.setId(11L);
        user.setLogin("Igor");
        user.setActive(Boolean.TRUE);
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(user); // INSERT
            tx.commit();
        }

        showList(sessionFactory);

        // SELECT
        try (Session session = sessionFactory.openSession()) {
            User _user = session.find(User.class, 1L); // SELECT
            System.out.println("User _user = session.find(User.class, 1L);-----");
            System.out.println(_user);
        }

        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.merge(user); // UPDATE
            tx.commit();
        }

        try (Session session = sessionFactory.openSession()) {
//      Transaction tx = session.beginTransaction();
//      session.remove(user); // DELETE
//      tx.commit();
        }

        // SELECT
        try (Session session = sessionFactory.openSession()) {
            User savedUser = session.find(User.class, 4L);
            System.out.println("User with id = 4: " + savedUser);
        }
        showList(sessionFactory);
    }

    private static void showList(SessionFactory sFactory) {
        try (Session session = sFactory.openSession()) {
            System.out.println("\nList, all users: ----------");
            List<User> users = session.createQuery("select u from User u ", User.class).getResultList();
            System.out.println(users);
        }
    }
}
