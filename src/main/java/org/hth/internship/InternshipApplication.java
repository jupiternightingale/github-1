package org.hth.internship;

import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;
import java.util.Scanner;

@SpringBootApplication
public class InternshipApplication {
    public static void main(String[] args) {
        // main2(args);
        ClientCache cache = new ClientCacheFactory()
                .addPoolLocator("localhost", 10334)
                .create();
        Region<String, String> region = cache
                .<String, String>createClientRegionFactory(ClientRegionShortcut.PROXY)
                .create("goat");

        //loop over all keys on the servers and print out the keys and values.
        for (Object currentkey : region.keySetOnServer()) {
            System.out.println("currentkey = " + currentkey);
            System.out.println("region.get(currentkey) = " + region.get(currentkey));
        }
        //the command will send the keys and values up into a sever/region where it is stored .
        region.put("octopus", asciiart.octopus);
        region.put("tree", asciiart.tree);
        //pringint out what ever is nicole
        System.out.println("region.get(\"nicole\") = " + region.get("nicole"));
        for (Object currentkey : region.keySetOnServer()) {
            System.out.println("currentkey = " + currentkey);
            System.out.println("region.get(currentkey) = " + region.get(currentkey));
        }

        //loop forever
        while (true) {
            //read input from user
            Scanner scanner = new Scanner(System.in);
            String userinput = scanner.nextLine();
            //print out what the user typed and it's value
            System.out.println("userinput = " + userinput);
            System.out.println("region.get(userinput) = " + region.get(userinput));
        }
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

