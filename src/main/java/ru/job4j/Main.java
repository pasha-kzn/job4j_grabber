package ru.job4j;

import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.service.Config;
import ru.job4j.grabber.service.SchedulerManager;
import ru.job4j.grabber.service.SuperJobGrab;
import ru.job4j.grabber.stores.JdbcStore;
import java.sql.DriverManager;
import java.sql.SQLException;

import ru.job4j.grabber.stores.MemStore;

import static ru.job4j.grabber.service.Config.log;

public class Main {
    public static void main(String[] args) {
        var config = new Config();
        config.load("application.properties");
        try (var connection = DriverManager.getConnection(
                config.get("url"),
                config.get("username"),
                config.get("password"));
        var scheduler = new SchedulerManager()) {
            var store = new JdbcStore(connection);
            var post = new Post();
            post.setTitle("Super Java Job");
            store.save(post);
            scheduler.init();
            scheduler.load(
                    Integer.parseInt(config.get("rabbit.interval")),
                    SuperJobGrab.class,
                    store);
            Thread.sleep(10000);
        } catch (Exception e) {
            log.error("When create a connection", e);
        }
    }
}