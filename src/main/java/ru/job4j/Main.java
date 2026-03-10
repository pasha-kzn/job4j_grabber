package ru.job4j;

import ru.job4j.grabber.model.Post;
import ru.job4j.grabber.service.Config;
import ru.job4j.grabber.service.HabrCareerParse;
import ru.job4j.grabber.service.SchedulerManager;
import ru.job4j.grabber.service.SuperJobGrab;
import ru.job4j.grabber.service.Web;
import ru.job4j.grabber.stores.JdbcStore;
import ru.job4j.grabber.stores.Store;
import ru.job4j.grabber.utils.HabrCareerDateTimeParser;

import java.sql.DriverManager;
import java.util.List;

import static ru.job4j.grabber.service.Config.LOG;

public class Main {
    public static void main(String[] args) {
        var config = new Config();
        config.load("application.properties");
        try (var connection = DriverManager.getConnection(
                config.get("url"),
                config.get("username"),
                config.get("password"));
             var scheduler = new SchedulerManager()) {
            Store store = new JdbcStore(connection);
            List<Post> posts = new HabrCareerParse(new HabrCareerDateTimeParser()).fetch();
            posts.forEach(store::save);
            scheduler.init();
            scheduler.load(
                    Integer.parseInt(config.get("rabbit.interval")),
                    SuperJobGrab.class,
                    store);
            new Web(store).start(Integer.parseInt(config.get("server.port")));
        } catch (Exception e) {
            LOG.error("When create a connection", e);
        }
    }
}