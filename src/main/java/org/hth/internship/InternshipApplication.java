package org.hth.internship;

import org.apache.commons.io.FileUtils;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Scanner;

@SpringBootApplication
@RestController
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
        loadfromdisk();
        printContentsOfRegion();
        initializeDatabase();
        printContentsOfRegion();

        Runnable runnable = new Runnable() {
        //loop forever

            public void run() {
                while (true){
                    String userinput = getUserinput();
                    //print out what the user typed and it's value
                    if (userinput.equals("?")){
                        printKeysOnServer();
                    }else if (userinput.equals("help")){
                        System.out.println("If ? is pressed a list of images will pop up. For some of the images that have an underscore,\n" +
                                "you have to add it in or else the picture won't work.\n");
                    } else{
                        printuserinput(userinput);
                    }
                }
            }
        };
        new Thread (runnable).start();
    }

    private void printKeysOnServer() {
        System.out.println("list of images:");
        for (Object currentkey : region.keySetOnServer()) {
            System.out.println(currentkey);
        }
    }

    private String getUserinput() {
        //read input from user
        System.out.print("Name of art > ");
        Scanner scanner = new Scanner(System.in);
        String userinput = scanner.nextLine();
        return userinput;
    }

    @GetMapping("/getartbykey")
    public String getArtbyKey(String artKey){
        return region.get(artKey);
    }
    private void printuserinput(String userinput) {
        System.out.println("userinput = " + userinput);
        String value = getArtbyKey(userinput);
        if (value == null){
            System.out.println("image not found");
        }else {
            System.out.println("region.get(userinput) = \n" + value);
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

        }
    }
    public void loadfromdisk()throws Exception{
        for (File currentFile : new File("art").listFiles()){
            String art = FileUtils.readFileToString(currentFile, Charset.defaultCharset());
            region.put(currentFile.getName(), art);
        }
    }
}
