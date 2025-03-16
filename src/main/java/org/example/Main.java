package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {
    private static SessionFactory sessionFactory;

    public static void main(String[] args) {
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Customer.class)
                .addAnnotatedClass(Product.class)
                .addAnnotatedClass(Purchase.class)
                .buildSessionFactory();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Введите команду:");
            String command = scanner.nextLine();

            if (command.startsWith("/showProductsByCustomer")) {
                String customerName = command.split(" ")[1];
                showProductsByCustomer(customerName);
            } else if (command.startsWith("/findCustomerByProductTitle")) {
                String productTitle = command.split(" ")[1];
                findCustomersByProductTitle(productTitle);
            } else if (command.startsWith("/removeCustomer")) {
                String customerName = command.split(" ")[1];
                removeCustomer(customerName);
            } else if (command.startsWith("/removeProduct")) {
                String productTitle = command.split(" ")[1];
                removeProduct(productTitle);
            } else if (command.startsWith("/buy")) {
                String customerName = command.split(" ")[1];
                String productTitle = command.split(" ")[2];
                buyProduct(customerName, productTitle);
            } else if (command.equals("exit")) {
                break;
            } else {
                System.out.println("Неизвестная команда");
            }
        }
        sessionFactory.close();
        scanner.close();
    }

    public static void showProductsByCustomer(String customerName){
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Customer customer = session.createQuery("FROM Customer WHERE name = :name", Customer.class)
                    .setParameter("name", customerName)
                    .uniqueResult();
            if(customer != null){
                System.out.println("Товары, купленные " + customerName + ":");
                for (Purchase purchase : customer.getPurchases()) {
                    System.out.println(purchase.getProduct().getTitle() + " (Цена при покупке: " + purchase.getPriceAtPurchase() + ")");
                }
            } else {
                System.out.println("Покупатель не найден");
            }
            transaction.commit();
        } catch (Exception e){
            if (transaction != null){
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null)
                session.close();
        }
    }

    public static void findCustomersByProductTitle(String productTitle){
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();

            Product product = session.createQuery("FROM Product WHERE name = :name", Product.class)
                    .setParameter("name", productTitle)
                    .uniqueResult();
            if (product != null){
                System.out.println("Покупатели товара " + productTitle + ":");
                for (Purchase purchase : product.getPurchases()){
                    System.out.println(purchase.getCustomer().getName() + "\n");
                }
            }else {System.out.println("Товар не найден");}
            transaction.commit();

        } catch (Exception e){
            if (transaction != null)
                transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null)
                session.close();
        }
    }
    public static void removeCustomer(String customerName) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Customer customer = session.createQuery("FROM Customer WHERE name = :name", Customer.class)
                    .setParameter("name", customerName)
                    .uniqueResult();
            if (customer != null) {
                session.delete(customer);
                System.out.println("Покупатель удален");
            } else {
                System.out.println("Покупатель не найден");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null)
                session.close();
        }
    }
    public static void removeProduct(String productTitle){
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Product product = session.createQuery("FROM Product WHERE title = :title", Product.class)
                    .setParameter("title", productTitle)
                    .uniqueResult();
            if (product != null) {
                session.delete(product);
                System.out.println("Товар удален");
            } else {
                System.out.println("Товар не найден");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null)
                session.close();
        }
    }

    public static void buyProduct(String customerName, String productTitle){
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            Customer customer = session.createQuery("FROM Customer WHERE name = :name", Customer.class)
                    .setParameter("name", customerName)
                    .uniqueResult();

            Product product = session.createQuery("FROM Product WHERE title = :title", Product.class)
                    .setParameter("title", productTitle)
                    .uniqueResult();

            if (customer != null && product != null) {
                Purchase purchase = new Purchase();
                purchase.setCustomer(customer);
                purchase.setProduct(product);
                purchase.setPriceAtPurchase(product.getPrice());
                purchase.setPurchaseDate(LocalDateTime.now());

                session.save(purchase);
                System.out.println("Покупка оформлена");
            } else {
                System.out.println("Покупатель или товар не найден");
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null)
                transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null)
                session.close();
        }
    }
}