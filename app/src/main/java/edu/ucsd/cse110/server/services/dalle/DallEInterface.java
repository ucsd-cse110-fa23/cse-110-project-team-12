package edu.ucsd.cse110.server.services.dalle;

import java.awt.image.BufferedImage;

public interface DallEInterface {
    public BufferedImage promptDallE(String prompt);
}
