package validated;

import io.micronaut.runtime.Micronaut;

public class Application {
    public static void main(String[] args) {
        Micronaut.build(args)
                .packages("validated")
                .mainClass(Application.class)
                .start();
    }
}