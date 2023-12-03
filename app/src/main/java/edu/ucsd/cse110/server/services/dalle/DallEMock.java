package edu.ucsd.cse110.server.services.dalle;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class DallEMock implements DallEInterface{
    
    private String storagePath = "./src/main/java/edu/ucsd/cse110/server/services/dalle/";
    
    @Override
    public BufferedImage promptDallE(String prompt) {
        try {
            File file = new File(storagePath + "mockImage.jpg");
            BufferedImage image = ImageIO.read(file);
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
