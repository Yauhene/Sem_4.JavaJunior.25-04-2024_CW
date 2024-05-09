import org.hibernate.*;
import org.hibernate.cfg.*;
import org.hibernate.id.uuid.*;

import java.sql.*;
import java.util.*;

import static java.sql.DriverManager.getConnection;

public class Homework {
    static Map<String, String> mapGroups = new HashMap<String, String>();
    static Map<String, String[]> mapStudents = new HashMap<String, String[]>();

  /**
   * Перенести структуру дз третьего урока на JPA:
   * 1. Описать сущности Student и Group
   * 2. Написать запросы: Find, Persist, Remove
   * 3. ... поупражняться с разными запросами ...
   */


  public static void main(String[] args) throws SQLException, NoSuchFieldException {
      try (Connection connection = getConnection("jdbc:mysql://localhost:3306/sem_3_sql", "root", "61208619");
           Statement st = connection.createStatement()) {
//          createGroupTable(connection);
//          creteStudentTable(connection);
//        acceptConnection(connection);

//          prepareTables(connection);
          run(connection);
      }
  }

    private static void createGroupTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            // execute, executeUpdate, executeQuery

            // Создание таблицы групп
            //
            statement.execute("""
        create table group_table(
          id varchar(36) default (uuid()) primary key,
          group_name varchar(30)
        )
        """);
        }
    }

    private static void creteStudentTable(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            // execute, executeUpdate, executeQuery

            // Создание таблицы групп
            //
            statement.execute("""
        create table student(
          id varchar(36) default (uuid()) primary key,
          first_name varchar(30),
          second_name varchar(30),
          group_id varchar(36)
        )
        """);
        }
    }
    private static void run(Connection connection) throws SQLException, NoSuchFieldException {
        // Hikari Connection Pool

        Configuration configuration = new Configuration().configure();
        try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {

            System.out.println("---------------------------------------------------------hibernateCrud(sessionFactory);");
            hibernateCrud(sessionFactory, connection);
            System.out.println("AFTER---------------------------------------------------------hibernateCrud(sessionFactory);");



            System.out.println("\nПолучение списка студентов группы 'Java'");
            Group group;
            try (Session session = sessionFactory.openSession()) {
                group = session.find(Group.class, "3e0351a6-84d8-4ef9-8d4b-5593c45c58c2");
            }

            System.out.println(group.getStudents());


        }
    }

    private static void hibernateCrud(SessionFactory sessionFactory, Connection connection) throws SQLException {
        // ResultSet

        // Выполнение SQL-запроса (поиск группы с именем "Java")
        //
            try (Statement statement = connection.createStatement()) {
                ResultSet resultSet = statement.executeQuery("""
              select id, group_name
              from group_table
              """);
                boolean thereIs = false;
                String testName = "Java";
                while (resultSet.next()) {
                    String group_name = resultSet.getString("group_name");
                    String group_id = resultSet.getString("id");
                    if (group_name.equals(testName)) {
                        thereIs = true;
                        mapGroups.put(group_id, group_name);
                    }
                }
                if (!thereIs) {
                    //                 Создание группы Java group
                        Group groupJava = new Group();
                        groupJava.setId(UUID.randomUUID().toString());
                        groupJava.setGroup_name("Java");
                        System.out.println("After construction groupJava: " + groupJava);

                        try (Session session = sessionFactory.openSession()) {
                            Transaction tx = session.beginTransaction();
                            session.persist(groupJava); // По сути - INSERT
                            tx.commit(); // Подтверждаем изменения
                        }
                } else {

                }
            }

        // Выполнение SQL-запроса (поиск группы с именем "Test")
        //
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("""
              select id, group_name
              from group_table
              """);
            boolean thereIs = false;
            String testName = "Test";
            while (resultSet.next()) {
                String group_name = resultSet.getString("group_name");
                String group_id = resultSet.getString("id");
                if (group_name.equals(testName)) {
                    thereIs = true;
                    mapGroups.put(group_id, group_name);
//                    System.out.println("mapGroups: " + mapGroups);
                }
            }
            if (!thereIs) {
                //                 Создание группы Test group
                Group groupTest = new Group();
                groupTest.setId(UUID.randomUUID().toString());
                groupTest.setGroup_name("Test");
                System.out.println("After construction groupTest: " + groupTest);
                mapGroups.put(groupTest.getId(), groupTest.getGroup_name());
//                System.out.println("mapGroups: " + mapGroups);

                try (Session session = sessionFactory.openSession()) {
                    Transaction tx = session.beginTransaction();
                    session.persist(groupTest); // По сути - INSERT
                    tx.commit(); // Подтверждаем изменения
                }
            }
        }

        System.out.println("mapGroups: " + mapGroups);

        // Считываем таблицу student, создаем мапу
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("""
                    select id, first_name, second_name, group_id
                    from student
                    """);
            boolean thereIs = false;
            String testName = "Test";
            Student st = new Student();
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String first_name = resultSet.getString("first_name");
                String second_name = resultSet.getString("second_name");
                String gr_id = resultSet.getString("group_id");
                String[] student_info = new String[]{first_name, second_name, gr_id};
                mapStudents.put(id,student_info);
                st.setId(id);
                st.setFirstName(first_name);
                st.setSecondName(second_name);
//                st.setGroup(group);
            }
        }

//             Заселение группы "Java" студентами
//             student_1 и student_2
        Student student_1 = new Student();
        Student student_2 = new Student();
        if (mapGroups.containsValue("Java")) // проверка наличия группы в мапе групп
            for (Map.Entry item : mapGroups.entrySet()) {
                if (item.getValue().equals("Java")) {
                    try (Session session = sessionFactory.openSession()) {
                        Group group =  session.find(Group.class, item.getKey());
                        // studentIdByName() - поиск id студента по содержимому записи в таблице
                        if (studentIdByName("Igor", "Andreev").equals("oops")) {
                            student_1.setId(String.valueOf(UUID.randomUUID()));
                            student_1.setFirstName("Igor");
                            student_1.setSecondName("Andreev");
                            student_1.setGroup(group);
                            // подготовка массива с данными студента для внесения его в мапу студентов
                            String[] st_info = new String[]{student_1.getFirstName(),student_1.getSecondName(), student_1.getGroup().getId()};
                            mapStudents.put(student_1.getId(), st_info);
                            System.out.println("First is done");
                            try (session) {
                                Transaction tx = session.beginTransaction();
                                session.persist(student_1); // INSERT
                                tx.commit();
                            }
                        }
                        System.out.println("Karabas Barabas id = " + studentIdByName("Karabas", "Barabas"));
                        if (studentIdByName("Karabas", "Barabas").equals("oops")) {
//                            Student student_2 = new Student();
                            student_2.setId(String.valueOf(UUID.randomUUID()));
                            student_2.setFirstName("Karabas");
                            student_2.setSecondName("Barabas");
                            student_2.setGroup(group);
                        String[] st_info = new String[]{student_2.getFirstName(),student_2.getSecondName(), student_2.getGroup().getId()};
                            mapStudents.put(student_2.getId(), st_info);
                            System.out.println("Second is done");
//                            showStudents();
                        try (session) {
                            Transaction tx = session.beginTransaction();
                            session.persist(student_2); // INSERT
                            tx.commit();
                        }
                        }

                    }
                }
            }
        createStudent("Test", "Феофан", "Огурцов", sessionFactory);
        createStudent("Test", "Артем", "Голопушко", sessionFactory);
//        showStudents();


//
//        showList(sessionFactory);
//
        // SELECT
        try (Session session = sessionFactory.openSession()) {
            Group group = session.find(Group.class, "6b37546e-9bab-4fd9-8e35-7096c2daddb5"); // SELECT
            System.out.println("\nGroup  = session.find(Group.class, Group group = session.find(Group.class, \"6b37546e-9bab-4fd9-8e35-7096c2daddb5\"));-----");
            System.out.println(group);
        }
//
        try (Session session = sessionFactory.openSession()) {
            Group group = session.find(Group.class, "6b37546e-9bab-4fd9-8e35-7096c2daddb5");
            Transaction tx = session.beginTransaction();
            session.merge(group); // UPDATE
            tx.commit();
        }

        try (Session session = sessionFactory.openSession()) {
//            Group group = session.find(Group.class, "6b37546e-9bab-4fd9-8e35-7096c2daddb5");
//              Transaction tx = session.beginTransaction();
//              session.remove(group); // DELETE
//              tx.commit();
        }

        // SELECT
        try (Session session = sessionFactory.openSession()) {
            Group savedGroup = session.find(Group.class, "6b37546e-9bab-4fd9-8e35-7096c2daddb5");
            System.out.println("\nGroup with id = 6b37546e-9bab-4fd9-8e35-7096c2daddb5: " + savedGroup);
        }
        showList(sessionFactory);
    }

    private static long findGroupByName(String gr_name) {
      long gr_count = 0;


      return gr_count;
    }


    /**
     * Функция поиска id студента по значениям полей его записи
      * @param studentFirstName - значение поля "first_name"
     * @param studentSecondName - значение поля "second_name"
     * @return
     */
    private static String studentIdByName(String studentFirstName, String studentSecondName) {
      String result = "oops"; // значение результата функции при неудачном поиске
        for (Map.Entry item: mapStudents.entrySet()){
            String[] strArray = (String[]) item.getValue();
            if ((strArray[0].equals(studentFirstName) && (strArray[1].equals(studentSecondName)))) {
                result = (String) item.getKey();
            }
        }
      return result;
    }

    /**
     * Метод вывода списка студентов из мапы в консоль
     */
    private static void showStudents() {
        System.out.println("\nСписок студентов: ");
        System.out.println(mapStudents.keySet().size());
        for (Map.Entry item: mapStudents.entrySet()){
            String[] strArray = (String[]) item.getValue();
            System.out.println(item.getKey() + " " +
                    strArray[0] + " " +
                    strArray[1] + " " +
                    strArray[2]);
        }
    }

    private static void createStudent(String groupName, String fName, String sName,SessionFactory sessionFactory) {
        Student student = new Student();
        if (mapGroups.containsValue(groupName)) // проверка наличия группы в мапе групп
            for (Map.Entry item : mapGroups.entrySet()) {
                if (item.getValue().equals(groupName)) {
                    try (Session session = sessionFactory.openSession()) {
                        Group group =  session.find(Group.class, item.getKey());
                        // studentIdByName() - поиск id студента по содержимому записи в таблице
                        if (studentIdByName(fName, sName).equals("oops")) {
                            student.setId(String.valueOf(UUID.randomUUID()));
                            student.setFirstName(fName);
                            student.setSecondName(sName);
                            student.setGroup(group);
                            // подготовка массива с данными студента для внесения его в мапу студентов
                            String[] st_info = new String[]{student.getFirstName(),student.getSecondName(), student.getGroup().getId()};
                            mapStudents.put(student.getId(), st_info);
                            try (session) {
                                Transaction tx = session.beginTransaction();
                                session.persist(student); // INSERT
                                tx.commit();
                            }
                        }

                    }
                }
            }

    }
    private static void showList(SessionFactory sFactory) {
        try (Session session = sFactory.openSession()) {
            System.out.println("\nList, all groups: ----------");
            List<Group> groups = session.createQuery("select g from Group g ", Group.class).getResultList();
            System.out.println(groups);
        }
    }
}
