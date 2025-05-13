package com.delorenzo.Cinema.conf;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String location = "target/classes/files";

    public void setLocation(String location) {
        this.location = location;
    }
}
