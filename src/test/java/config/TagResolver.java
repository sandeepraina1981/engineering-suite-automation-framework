package config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class TagResolver {

    public static void main(String[] args) throws IOException {

        String filePath = System.getProperty("user.dir") + "\\src\\test\\resources\\config.properties";
        FileInputStream fis = new FileInputStream(filePath);
        Properties prop = new Properties();
        prop.load(fis);


        String cliTags = prop.getProperty("tagExpression");
        if (cliTags != null && !cliTags.isBlank()) {
            System.out.println("✔ Using tags from CLI : " + cliTags);
            //return cliTags;
        }
        // Fallback default
        System.out.println("⚠ Using default tags from code : @smoke");
        // return "@smoke";
    }
}
