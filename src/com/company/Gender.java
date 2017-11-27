package com.company;

import java.util.Random;

public enum Gender {
    MALE, FEMALE, NONE;
    public static Gender getRandomGender() {
        Random random = new Random();
        switch (random.nextInt(2)) {
            case 0:
                return MALE;
            case 1:
                return FEMALE;
        }
        return NONE;
    }
}
