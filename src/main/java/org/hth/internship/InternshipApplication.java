package org.hth.internship;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.Scanner;

@SpringBootApplication
public class InternshipApplication implements ApplicationRunner {

    @Resource
    private Region<String, String> region;


    @Bean(name = "region")
    public Region createRegion() {
        ClientCache cache = new ClientCacheFactory()
                .addPoolLocator("localhost", 10334)
                .create();
        Region<String, String> region = cache
                .<String, String>createClientRegionFactory(ClientRegionShortcut.PROXY)
                .create("goat");
        return region;
    }

    public void printContentsOfRegion() {
        //loop over all keys on the servers and print out the keys and values.
        for (Object currentkey : region.keySetOnServer()) {
            System.out.println("currentkey = " + currentkey);
            System.out.println("region.get(currentkey) = " + region.get(currentkey));
        }
    }

    public void initializeDatabase() throws IllegalAccessException {
        //Loop over all of the fields in the "asciiart" class and store them in Apache Geode
        //   Technically we only need todo this once when we first start up Apache Geode to initialize the database.
        for (Field f : asciiart.class.getDeclaredFields()) {
            String name = f.getName();
            String value = (String) f.get(null);
            region.put(name, value);
        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        printContentsOfRegion();
        initializeDatabase();
        printContentsOfRegion();

        //loop forever
        while (true) {
            //read input from user
            Scanner scanner = new Scanner(System.in);
            String userinput = scanner.nextLine();
            //print out what the user typed and it's value
            System.out.println("userinput = " + userinput);
            String value = region.get(userinput);
            if (value == null){
                System.out.println("image not found");
            }else {
                System.out.println("region.get(userinput) = " + value);
            }
        }
    }

    public static void main(String[] args) {
        // main2(args);
        SpringApplication.run(InternshipApplication.class, args);
    }

    // the string allows the code to be run over and over again.
    public static void main2(String[] args) {

        for (long i = 1; i <= 50; i = i + 1) {

            System.out.println(asciiart.squerl);
            System.out.println(asciiart.tree);
            System.out.println(asciiart.doge);
            System.out.println(asciiart.frog);
            System.out.println(asciiart.octopus);
            System.out.println(asciiart.chicken);
            System.out.println(asciiart.frog2);
            System.out.println(asciiart.house);
            System.out.println("froghat = " + asciiart.froghat);

        }
    }
}

